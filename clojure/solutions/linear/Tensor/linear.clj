(defn isValidVector [vector] (and (vector? vector) (every? number? vector)))
(defn isValidVectors [vectors] (every? true? (mapv isValidVector vectors)))
(defn isSameSizeVectors [vectors] (or (number? vector) (apply = (mapv count vectors))))

(defn isValidTensors [tensors] (or (every? vector? tensors) (every? number? tensors)))
(defn isSameSizeTensors [tensors] (or (every? number? tensors) (apply = (mapv count tensors))))
(defn isEqualsTensors [tensors] (or (every? number? tensors)
    (every? true? (apply mapv (fn [& subTensors] (and
         (isValidTensors subTensors)
         (isSameSizeTensors subTensors)
         (isEqualsTensors subTensors))) tensors))))

(defn isValidSubTensors [tensors] (every? true? (mapv (fn [tensor] (and
    (isSameSizeTensors tensor)
    (isValidTensors tensor)
    (isEqualsTensors tensor))) tensors)))

(defn v? [operation] (fn [& vectors]
    {:pre [(isValidVectors vectors) (isSameSizeVectors vectors)]}
    (apply mapv operation vectors)))

(def v+ (v? +))
(def v* (v? *))
(def v- (v? -))
(defn v*s [vector & scalars] {:pre [(isValidVector vector)]} (mapv (fn [x] (apply * x scalars)) vector))
(defn scalar [& vectors] (apply + (apply v* vectors)))
(defn vect
    ([vector] {:pre [(isValidVector vector)]} vector)
    ([vector1 vector2]
        {:pre [(isValidVector vector1) (isValidVector vector2) (isSameSizeVectors (vector vector1 vector2))]}
        [(- (* (vector1 1) (vector2 2)) (* (vector1 2) (vector2 1)))
         (- (* (vector1 2) (vector2 0)) (* (vector1 0) (vector2 2)))
         (- (* (vector1 0) (vector2 1)) (* (vector1 1) (vector2 0)))
         ])
    ([vector1 vector2 & vectors]
        (apply vect (vect vector1 vector2) vectors)))

(defn v*v [vector1 vector2] (reduce + (v* vector1 vector2)))

(defn m? [operation] (fn [& matrixs] (apply mapv operation matrixs)))

(def m+ (m? v+))
(def m- (m? v-))
(def m* (m? v*))
(defn m*s [matrix & scalars] (mapv (fn [vector] (apply v*s vector scalars)) matrix))
(defn m*v [matrix vector] (mapv (fn [row] (v*v row vector)) matrix))
(defn m*m
    ([matrix] matrix)
    ([matrix1 matrix2] (mapv (fn [row] (apply mapv (fn [& xs] (v*v row (apply vector xs))) matrix2)) matrix1))
    ([matrix1 matrix2 & matrixs] (apply m*m (m*m matrix1 matrix2) matrixs)))
(defn transpose [matrix] (apply mapv vector matrix))

(defn t? [operation]
    (fn solve [& tensors]
        {:pre [(isValidTensors tensors) (isSameSizeTensors tensors) (isValidSubTensors tensors) (isEqualsTensors tensors)]}
        (apply mapv (fn [& args] (if (every? number? args) (apply operation args) (apply solve args))) tensors)))

(def t+ (t? +))
(def t- (t? -))
(def t* (t? *))
