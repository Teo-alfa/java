(defn mapply "M(ap) + (ap)ply: apply result of mapv" [f] (fn [& arg] (apply mapv f arg)))

(def v+ (mapply +))
(def v- (mapply -))
(def v* (mapply *))
(def vd (mapply /))

(defn scalar [a b & args] (apply + (apply v* a b args)))

(defn vect [v1 v2 & v] 
    (letfn [ 
        (minor [a b pos1 pos2] 
            (- (* (nth a pos1) (nth b pos2)) (* (nth a pos2) (nth b pos1))))
        (v*v [a b] 
            [(minor a b 1 2) (- (minor a b 0 2)) (minor a b 0 1)])
            ]
        (apply v*v v1 v2 v)))


(defn v*s [v s] (mapv (fn [k] (* k s)) v))

(def m+ (mapply v+))
(def m- (mapply v-))
(def m* (mapply v*))

(defn m*s [m s] (mapv (fn [v] (v*s v s)) m))
(defn transpose [m] (apply mapv vector m))
(defn m*v [m v] (mapv (fn [x] (scalar x v)) m))

(defn m*m [m1 m2] (transpose (mapv (fn [v] (m*v m1 v)) (transpose m2))))

; Я не объединил mapply и smapply, хотя очевидно что smapply это расширение mapply и можно было лишь оставить smapply,
; но я оставил обе функции чтобы тесты проходили быстрее, так как в первый функциях мне не надо самому проверять на то вектор ли это
; лишний раз. Пожалуйста, не баньте.

(defn smapply [f] (
    fn [c1 & c]
        (if (vector? c1) 
            (apply mapv (smapply f) c1 c)
            (apply f c1 c)
        )
    )
)

(def s+ (smapply +))
(def s- (smapply -))
(def s* (smapply *))
