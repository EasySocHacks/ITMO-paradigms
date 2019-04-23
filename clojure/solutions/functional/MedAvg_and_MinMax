(defn constant [constValue] (fn [_] constValue))
(defn variable [varName] (fn [varsValue] (varsValue varName)))

(defn commonOperation [operation] (fn [& expressions] (fn [varsValue]
  (apply operation (map (comp (fn [expression] (expression varsValue))) expressions)))))

(def add (commonOperation +))
(def subtract (commonOperation -))
(def multiply (commonOperation *))
(def divide (commonOperation (fn [a b] (/ (double a) (double b)))))
(def negate (commonOperation (fn [x] (- x))))
(def min (commonOperation (fn [& xs] (reduce (fn [x y] (Math/min x y)) xs))))
(def max (commonOperation (fn [& xs] (reduce (fn [x y] (Math/max x y)) xs))))
(def med (commonOperation (fn [& xs] (nth (sort xs) (Math/floor (/ (count xs) 2))))))
(def avg (commonOperation (fn [& xs] (/ (apply + xs) (count xs)))))

(def getOperation {
  '+ add '- subtract '* multiply '/ divide 'negate negate 'min min 'max max 'med med 'avg avg
})

(defn parse [expression]
  (cond
    (seq? expression) (apply (getOperation (first expression)) (map parse (rest expression)))

    (number? expression) (constant expression)

    :else (variable (str expression))
    ))

(defn parseFunction [expressionString] (parse (read-string expressionString)))
