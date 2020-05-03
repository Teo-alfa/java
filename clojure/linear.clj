(defn term-op [f] (fn [& arg] (apply mapv f arg)))
(defn element-op [f] (fn [x s1 & s] (mapv (fn [dx] (f dx (apply + s1 s))) x)))

(def v+ (term-op +))
(def v- (term-op -))
(def v* (term-op *))
(def vd (term-op /))

(defn scalar [a b & args] (apply + (apply v* a b args)))

(defn vect [v1 v2 & v] 
    (letfn [ 
        (minor [a b pos1 pos2] 
            (- (* (nth a pos1) (nth b pos2)) (* (nth a pos2) (nth b pos1))))
        (v*v [a b] 
            [(minor a b 1 2) (- (minor a b 0 2)) (minor a b 0 1)])
            ]
        (apply v*v v1 v2 v)))

; (defn v*s [v s1 & s] (let [k (reduce + s1 s)] (mapv (partial * k) v)))
(def v*s (element-op *))

(def m+ (term-op v+))
(def m- (term-op v-))
(def m* (term-op v*))

; (defn m*s [m s1 & s] (mapv (fn [v] (apply v*s v s1 s)) m))

(def m*s (element-op v*s))

(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] (mapv (partial scalar v) m))

(defn m*m [m1 m2] (transpose (mapv (partial m*v m1) (transpose m2))))

; Я не объединил term-op и sterm-op, хотя очевидно что sterm-op это расширение term-op и можно было лишь оставить sterm-op,
; но я оставил обе функции чтобы тесты проходили быстрее, так как в первый функциях мне не надо самому проверять на то вектор ли это
; лишний раз. Пожалуйста, не баньте.

(defn sterm-op [f] (
    fn [c1 & c]
        (if (vector? c1) 
            (apply mapv (sterm-op f) c1 c)
            (apply f c1 c)
        )
    )
)

(def s+ (sterm-op +))
(def s- (sterm-op -))
(def s* (sterm-op *))
