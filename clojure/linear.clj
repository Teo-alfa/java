(defn same? [& args]
    (and 
        (>= (count args) 1)  
        (vector? (first args))
        (every? 
            (fn [a] 
                (and 
                    (vector? a)
                    (== (count a) (count(first args)))
                    (or 
                        (and 
                            (number? (first a))
                            (every? (partial number?) a))
                        (and 
                            (vector? (first a))
                            (apply same? a))))) args)))

(defn term-op [f] 
    (fn [& args] 
        {:pre [(apply same? args)]}
        (apply mapv f args)))

(defn element-op [f] 
    (fn [x & s]
        {:pre [(and (same? x) (every? (partial number?) s))]}
        (let [k (if (== (count s) 0) 1 (apply * s))](mapv #(f % k) x))))

(defn is-v-struct [f]
    (fn [el]
        (and 
            (vector? el)
            (every? f el))))

(def v? (is-v-struct number?))
(def v+ (term-op +))
(def v- (term-op -))
(def v* (term-op *))
(def vd (term-op /))

(defn scalar [& args] 
    {:pre [(every? v? args)]}
    (apply + (apply v* args)))

(defn vect [& v] 
    {:pre [(and (>= (count v) 1) (every? v? v) (== (count (first v)) 3) (apply same? v))]}
    (letfn [ 
        (minor [a b pos1 pos2]
            (- (* (nth a pos1) (nth b pos2)) (* (nth a pos2) (nth b pos1))))
        (v*v [a b]
            [(minor a b 1 2) (- (minor a b 0 2)) (minor a b 0 1)])
    ]
        (reduce v*v v)))

(def v*s (element-op *))

(def m? (is-v-struct v?))
(def m+ (term-op v+))
(def m- (term-op v-))
(def m* (term-op v*))

(def m*s (element-op v*s))

(defn transpose [m] 
    {:pre [(m? m)]}
    (apply mapv vector m))

(defn m*v [m v] 
    {:pre [(and (m? m) (v? v) (== (count v) (count (first m))))]}
    (mapv (partial scalar v) m))

(defn m*m [& m] 
    (:pre [(every? m? m)])
    (reduce #(transpose (mapv (partial m*v %1) (transpose %2))) m))

(defn s-same? [& args]
    (or
        (every? number? args)
        (if (every? v? args)
            (apply same? args)
            (and 
                (every? vector? args)
                (every? true? (apply mapv s-same? args))
            )
        )
    )
)

(defn sterm-op [f] 
    (fn [& c]
        {:pre [(apply s-same? c)]}
        (if (vector? (first c)) 
            (apply mapv (sterm-op f) c)
            (apply f c)
        )
    )
)

(def s+ (sterm-op +))
(def s- (sterm-op -))
(def s* (sterm-op *))

(defn t-same? [& args]
    (if (every? v? args) 
        (apply same? args)
        (and
            (every? #(== (count %) (count (first args))) args) 
            (every? #(apply t-same? %) args)
            (every? true? (apply mapv t-same? args))
        )
    )
)

(defn tterm-op [f] 
    (fn [& t]
        {:pre [(apply t-same? t)]}
        (apply (sterm-op f) t)))

(def t+ (tterm-op +))
(def t- (tterm-op -))
(def t* (tterm-op *))

