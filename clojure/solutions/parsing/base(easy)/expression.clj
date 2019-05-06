(defn return [value tail] {:value value :tail tail})
(def valid? boolean)
(def tail :tail)
(def value :value)

(defn empty [value] (partial return value))
(def emptyObject (empty nil))
(def emptySeq (empty []))

(defn letter [template] (fn [[char & chars]] (if ((set template) char) (return char chars))))
(def digit (letter "1234567890."))
(def variable (letter "xyz"))
(def space (letter " "))
(def openBracket (letter "("))
(def closeBracket (letter ")"))
(def plus (letter "+"))
(def minus (letter "-"))
(def multiply (letter "*"))
(def divide (letter "/"))

(declare combine)
(declare word)
(declare either)

(defn parse [parser]
  (fn [expression]
    (let [skipSpace ((word str space) expression)]
      (if (valid? skipSpace)
        (parser (tail skipSpace))
        (parser expression)))))

(defn doFunc [function pair] (return (function (value pair)) (tail pair)))

(defn combine [combineFunc firstParser secondParser]
  (fn [expression]
    (let
      [firstParse ((parse firstParser) expression)
       secondParse (secondParser (tail firstParse))]
      (if (and (valid? firstParse) (valid? secondParse))
        (return
          (combineFunc (value firstParse) (value secondParse))
          (tail secondParse))))))

(defn either [firstParser secondParser]
  (fn [expression]
    (let
      [firstParse ((parse firstParser) expression)
       secondParse (secondParser expression)]
      (if (valid? firstParse)
        firstParse
        (if (valid? secondParse)
          secondParse)))))

(defn word [function parser]
  (fn [expression]
    (letfn
      [(recParser [pair]
         (let [parsed (parser (tail pair))]
           (if (valid? parsed)
             (recParser (return (str (value pair) (value parsed)) (tail parsed)))
             (if (valid? (value pair))
               (doFunc function pair)
               )
             )
           )
         )] (recParser (emptyObject expression)))))

(def number (either (word read-string digit)
  (combine (fn [x y] y) minus (word (fn [x] (read-string (str "-" x))) digit))))

(defn consistently [& parsers]
  (fn [expression]
    ((reduce (partial combine conj) (empty []) parsers) expression)
    )
  )

(defn combineWord [word]
  (apply consistently (reduce (fn [x y] (conj x (letter (str y)))) [] word)))

(def negate (combineWord "negate"))

(defn evaluate [expression varsValue] ((.evaluate expression) varsValue))
(defn toStringSuffix [expression] (.castSuffixString expression))

(definterface IOperation
  (evaluate [])
  (castSuffixString [])
  (diff []))

(deftype ConstantOperation [constValue]
  IOperation
  (evaluate [this] (fn [_] constValue))
  (castSuffixString [this] (format "%.1f" (double constValue))))

(defn Constant [constValue] (ConstantOperation. constValue))

(deftype VariableOperation [varName]
  IOperation
  (evaluate [this] (fn [varsValue] (varsValue varName)))
  (castSuffixString [this] varName))

(defn Variable [varName] (VariableOperation. varName))

(deftype CommonOperation [comparator stringOperation expressions]
  IOperation
  (evaluate [this] (fn [varsValue] (apply comparator (map (fn [expression] (evaluate expression varsValue)) expressions))))
  (castSuffixString [this] (str "(" (clojure.string/join " " (map (fn [expression] (toStringSuffix expression)) expressions)) " " stringOperation ")")))

(defn Add [& expressions] (CommonOperation.
  +
  "+"
  expressions))

(defn Subtract [& expressions] (CommonOperation.
  -
  "-"
  expressions))

(defn Multiply [& expressions] (CommonOperation.
  *
  "*"
  expressions))


(defn Divide [& expressions] (CommonOperation.
  (fn [a b] (/ (double a) (double b)))
  "/"
  expressions))

(defn Negate [expression] (CommonOperation.
  -
  "negate"
  (list expression)))

(defn doParseObjectSuffix [expression]
  (let
    [currNumber ((parse number) (tail expression))
     currVariable ((parse variable) (tail expression))
     currOpenBracket ((parse openBracket) (tail expression))
     currCloseBracket ((parse closeBracket) (tail expression))]
    (cond
      (valid? currNumber) (return (Constant (value currNumber)) (tail currNumber))
      (valid? currVariable) (return (Variable (str (value currVariable))) (tail currVariable))
      (valid? currCloseBracket) nil
      (valid? currOpenBracket)
      (letfn [(getInner [coll expr]
        (let [element (doParseObjectSuffix expr)]
          (if (valid? element)
            (let
              [currInnerNumber ((parse number) (tail expr))
               currPlus ((parse plus) (tail expr))
               currSubtract ((parse minus) (tail expr))
               currMultiply ((parse multiply) (tail expr))
               currDivide ((parse divide) (tail expr))
               currNegate ((parse negate) (tail expr))]
              (cond
                (valid? currPlus) (return (apply Add coll) (tail currPlus))
                (and (valid? currSubtract) (not (valid? currInnerNumber))) (return (apply Subtract coll) (tail currSubtract))
                (valid? currMultiply) (return (apply Multiply coll) (tail currMultiply))
                (valid? currDivide) (return (apply Divide coll) (tail currDivide))
                (valid? currNegate) (return (apply Negate coll) (tail currNegate))
                :else (getInner (conj coll (value element)) (emptyObject (tail element)))))
            (getInner coll (emptyObject (tail ((parse closeBracket) (tail expr)))))
            )
          )
        )] (getInner [] (emptyObject (tail currOpenBracket))))
      :else expression
      )))

(defn parseObjectSuffix [expression] (value (doParseObjectSuffix (emptyObject expression))))
