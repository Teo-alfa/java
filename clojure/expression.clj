;  | | | | | | |  |
;  \/\/\/\/\/\/\/\/
;  FOR DELAY OR EASY (MORE FOR DELAY)

(defn fun [f] 
    (fn [& arg] 
        (fn [m] (apply f (mapv (fn [a] (a m)) arg)))))

(defn constant [val] (fn [m] val)) 
(defn variable [var] (fn [m] (first (map m [var]))))

(defn div [x y] (/ (double x) (double y)))

(def add (fun +))
(def subtract (fun -))
(def multiply (fun *))
(def divide (fun div))
(def negate (fun -))
(def pw (fun (fn [x y] (Math/pow x y))))
(def lg (fun 
            (fn [x y] 
                (div (Math/log (Math/abs y)) (Math/log (Math/abs x))))))

(def operations {
    '+ add, 
    '- subtract, 
    '* multiply, 
    '/ divide, 
    'negate negate, 
    'pw pw, 
    'lg lg}
)

(defn parseFunction [expression] (cond
                             (number? expression) (constant expression)
                             (symbol? expression) (variable (str expression))
                             (string? expression) (parseFunction (read-string expression))
                             (seq? expression) (apply (get operations (first expression)) (map parseFunction (rest expression)))))