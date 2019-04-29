(declare ZERO)
(declare ONE)
(declare TWO)

(defn evaluate [expression varsValue] ((.evaluate expression) varsValue))
(defn toString [expression] (.castString expression))
(defn diff [expression diffVariable] ((.diff expression) diffVariable))

(definterface IOperation
  (evaluate [])
  (castString [])
  (diff []))

(deftype ConstantOperation [constValue]
  IOperation
  (evaluate [this] (fn [_] constValue))
  (castString [this] (format "%.1f" (double constValue)))
  (diff [this] (fn [_] ZERO)))

(defn Constant [constValue] (ConstantOperation. constValue))

(def ZERO (Constant 0))
(def ONE (Constant 1))
(def TWO (Constant 2))

(deftype VariableOperation [varName]
  IOperation
  (evaluate [this] (fn [varsValue] (varsValue varName)))
  (castString [this] varName)
  (diff [this] (fn [diffVariable] (if (= diffVariable varName) ONE ZERO))))

(defn Variable [varName] (VariableOperation. varName))

(deftype CommonOperation [comparator stringOperation diffComparator expressions]
  IOperation
  (evaluate [this] (fn [varsValue] (apply comparator (map (fn [expression] (evaluate expression varsValue)) expressions))))
  (castString [this] (str "(" stringOperation " " (clojure.string/join " " (map (fn [expression] (toString expression)) expressions)) ")"))
  (diff [this] diffComparator))

(defn Add [& expressions] (CommonOperation.
  +
  "+"
  (fn [diffVariable] (Add (diff (nth expressions 0) diffVariable) (diff (nth expressions 1) diffVariable)))
  expressions))

(defn Subtract [& expressions] (CommonOperation.
  -
  "-"
  (fn [diffVariable] (Subtract (diff (nth expressions 0) diffVariable) (diff (nth expressions 1) diffVariable)))
  expressions))

(defn Multiply [& expressions] (CommonOperation.
  *
  "*"
  (fn [diffVariable] (Add (Multiply (diff (nth expressions 0) diffVariable) (nth expressions 1))
                         (Multiply (nth expressions 0) (diff (nth expressions 1) diffVariable))))
  expressions))


(defn Divide [& expressions] (CommonOperation.
  (fn [a b] (/ (double a) (double b)))
  "/"
  (fn [diffVariable] (Divide (Subtract (Multiply (diff (nth expressions 0) diffVariable) (nth expressions 1))
                                      (Multiply (diff (nth expressions 1) diffVariable) (nth expressions 0)))
                            (Multiply (nth expressions 1) (nth expressions 1))))
  expressions))

(defn Negate [expression] (CommonOperation.
  -
  "negate"
  (fn [diffVariable] (Negate (diff expression diffVariable)))
  (list expression)))

(defn Square [expression] (CommonOperation.
  (fn [x] (Math/pow x 2))
  "square"
  (fn [diffVariable] (Multiply (Multiply TWO expression) (diff expression diffVariable)))
  (list expression)))

(defn Sqrt [expression] (CommonOperation.
  (fn [x] (Math/sqrt (Math/abs x)))
  "sqrt"
  (fn [diffVariable] (Divide (Multiply expression (diff expression diffVariable)) (Multiply TWO (Sqrt (Multiply expression (Multiply expression expression))))))
  (list expression)))

(def getOperation {
  '+ Add '- Subtract '* Multiply '/ Divide 'negate Negate 'square Square 'sqrt Sqrt
  })

(defn parse [expression]
  (cond
    (seq? expression) (apply (getOperation (first expression)) (map parse (rest expression)))

    (number? expression) (Constant expression)

    :else (Variable (str expression))
    ))

(defn parseObject [expressionString] (parse (read-string expressionString)))
