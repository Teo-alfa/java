;  | | | | | | |  |
;  \/\/\/\/\/\/\/\/
;  FOR DELAY OR EASY (MORE FOR DELAY)
(defn fun [f]
    {:pre [(fn? f)]}
    (fn [& arg]
        {:pre 
            [(and 
                (>= (count arg) 1) 
                (every? fn? arg))]}
        (fn [m] 
            (def marg (mapv #(% m) arg))
            {:pre [(and 
                (map? m) 
                (every? number? marg))]
             :post [(fn [post] (number? post))]} 
            (apply f marg)
        )
    )
)

(defn constant [val] 
    {:pre [(number? val)]}
    (fn [m] {:pre [(map? m)] :post [(number? %)]} val)) 

(defn variable [var] 
    {:pre [(string? var)]}
    (fn [m] 
        {:pre [
            (and 
                (map? m) 
                (contains? m var) 
                (every? number? (vals m)))]
         :post [(number? %)]} 
        (first (map m [var]))
    )
)

(defn div [x y] 
    {:pre [(every? number? [x y])]}
    (/ (double x) (double y)))

(def add (fun +))
(def subtract (fun -))
(def multiply (fun *))
(def divide (fun div))
(def negate (fun -))
(def pw (fun (fn [x y] (Math/pow x y))))
(def lg (fun 
            (fn [x y] 
                (div (Math/log (Math/abs y)) (Math/log (Math/abs x))))))
(def med (fun (fn [& x] (nth (sort x) (/ (count x) 2)))))
(def avg (fun (fn [& x] (div (reduce + x) (count x)))))

(def operations {
    '+ add, 
    '- subtract, 
    '* multiply, 
    '/ divide, 
    'negate negate, 
    'pw pw, 
    'lg lg
    'med med
    'avg avg}
)

(defn parseFunction [expression] 
    {:pre [
        (or 
            (number? expression) 
            (symbol? expression) 
            (string? expression) 
            (and 
                (seq? expression)
                (contains? operations (first expression))
            ))]}
    (cond
        (number? expression) (constant expression)
        (symbol? expression) (variable (str expression))
        (string? expression) (parseFunction (read-string expression))
        (seq? expression) (apply (get operations (first expression)) (map parseFunction (rest expression)))))