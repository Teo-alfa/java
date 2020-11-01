
; I will try to pass 11th hard and 12th easy for this day

; 10th hw (& hard)
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
    (constantly val)) 

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

(defn div [& arg] 
    {:pre [(every? number? arg)]}
    (if (= 1 (count arg))
        (/ (double (first arg)))
        (reduce (fn [x y] (/ (double x) (double y))) arg)
    )
)

(def add (fun +))
(def subtract (fun -))
(def multiply (fun *))
(def divide (fun div))
(def negate subtract)
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

; 11th hw (& hard)
(defn proto-get [this el] 
    (cond 
        (contains? this el) (el this) 
        (contains? this :proto) (proto-get (:proto this) el)))

(defn evaluate [this m] ((proto-get this :eval) this m))
(defn diff [this v] ((proto-get this :diff) this v))
(defn toString [this] ((proto-get this :toString) this))
(defn toStringSuffix [this] ((proto-get this :toStringSuffix) this))

(defn field [key]
  (fn [this] (proto-get this key)))

(def op (field :op))
(def func (field :f))
(def df_func (field :df))

(comment ":NOTE: pre-define constants and use `ZERO`, `ONE`, ...")
(declare ZERO, ONE)
(def Constant 
    (let [
        proto 
        {
            :eval (fn [this m] (:val this))
            :toString (fn [this] (format "%.1f" (double (:val this))))
            :toStringSuffix toString
            :diff (fn [this v] ZERO)
        }] 
        (fn [val] (assoc {:proto proto} :val val))))
(def ZERO (Constant 0))
(def ONE (Constant 1))
(def HALF (Constant 0.5))

(def Variable 
    (let [
        proto 
        {
            :eval (fn [this m] (first (map m [(:var this)])))
            :diff (fn [this v] (if (= (:var this) v) ONE ZERO))
            :toString (fn [this] (:var this))
            :toStringSuffix toString
        }]
        (fn [var] (assoc {:proto proto} :var var))))

(def FunctionProto {
    :toStringSuffix (fn [this] (str "(" (clojure.string/join " " (mapv toStringSuffix (:terms this))) " " (op this) ")"))
    :toString (fn [this] (str "(" (op this) " " (clojure.string/join " " (mapv toString (:terms this))) ")"))
    :eval (fn [this m] (apply (func this) (mapv #(evaluate % m) (:terms this))))
    :diff (fn [this v] (let [f (:terms this) df (mapv #(diff % v) f)] ((df_func this) f df)))
})

(defn ConstuctorFactory [f df op] 
    (let [proto (assoc {:proto FunctionProto} :f f :df df :op op)] 
        (fn [& terms] 
            (assoc {:proto proto} :terms (vec terms))
        )
    )
)

(def Add 
    (ConstuctorFactory 
        + 
        (fn [f df] (apply Add df)) 
        "+"
    )
)
(def Subtract 
    (ConstuctorFactory 
        - 
        (fn [f df] (apply Subtract df)) 
        "-"
    )
)

(defn tail-diff [rule oper] 
    (letfn [
        (dif [[h & t] [dh & dt]]
            (let [vt (vec t) vdt (vec dt)]
                (cond 
                    (== 0 (count vt)) (rule ONE h ZERO dh)
                    (== 1 (count vt)) (rule h (first vt) dh (first vdt))
                    :else             (rule h (oper vt) dh (dif vt vdt))
                )
            )
        )]
        (fn [f df] (dif f df))
    )
)

(def Multiply 
    (ConstuctorFactory 
        * 
        (tail-diff 
            (fn [x y dx dy] (Add (Multiply x dy) (Multiply dx y)))
            (fn [x] (apply Multiply x)) 
        )
        "*"
    )
)

(def Divide
    (ConstuctorFactory
        div
        (tail-diff
            (fn [f g df dg]
                (Divide (Subtract (Multiply df g) (Multiply f dg)) (Multiply g g)))
            (fn [x] (apply Divide x))
        )
        "/"
    )
)

(def Negate
    (ConstuctorFactory
        #(- %)
        (fn [[f] [df]]
            (Negate df))
        "negate"
    )
)

(def Square
    (ConstuctorFactory
        #(* % %)
        (fn [[f] [df]]
            (Multiply (Constant 2) f df))
        "square"
    )
)

(def Module
    (ConstuctorFactory
        (fn [x] (if (> 0 x) (- x) x))
        (fn [[f] [df]] (Multiply df (Divide (Module f) f)))
        "module"
    )
)

(def Sqrt
    (ConstuctorFactory
        #(Math/sqrt (Math/abs (double %)))
        (fn [[f] [df]]
            (Multiply (Divide HALF (Sqrt f)) (Divide (Module f) f) df))
        "sqrt"
    )
)

(def Sum
    (ConstuctorFactory
        +
        (fn [f df] (apply Sum df)) 
        "sum"
    )
)

(def Avg
    (ConstuctorFactory
        (fn [& args] (/ (reduce + args) (count args)))
        (fn [f df] (apply Avg df)) 
        "avg"
    )
)

(def Exp
    (ConstuctorFactory
        (fn [x] (Math/exp x))
        (fn [[f] [df]] (Multiply df (Exp f)))
        "exp"
    )
)

(def Ln
    (ConstuctorFactory
        (fn [x] (Math/log (Math/abs x)))
        (fn [[f] [df]] (Divide df f))
        "ln"
    )
)

(def Pow
    (ConstuctorFactory
        (fn [x y] (Math/pow x y))
        (fn [[f g] [df dg]] (Multiply (Pow f g) (Add (Multiply (Ln f) dg) (Divide g f))))
        "**"
    )
)

(def Log
    (ConstuctorFactory
        (fn [x y] (div (Math/log (Math/abs y)) (Math/log (Math/abs x))))
        (fn [[f g] [df dg]] (Divide (Subtract (Multiply (Divide df f) (Ln g)) (Multiply (Divide dg g) (Ln f))) (Square (Ln g))))
        "//"
    )
)

(defn Complex-Fun [same-fun op]
    (let 
        [proto 
            (assoc {:proto FunctionProto} 
            :op op 
            :eval (fn [this m] (evaluate (same-fun (:terms this)) m))
            :diff (fn [this v] (diff (same-fun (:terms this)) v))
            )
        ]
        (fn [& terms] 
            (assoc {:proto proto} :terms (vec terms))
        )
    )
)

(def Sumexp 
    (Complex-Fun 
        (fn [f] (apply Sum (mapv #(Exp %) f))) 
        "sumexp"
    )
)
(def Softmax 
    (Complex-Fun 
        (fn [f] (Divide (Exp (first f)) (apply Sumexp f)))
        "softmax"
    )
)

(def operationsObject {
    "+"       Add,
    "-"       Subtract,
    "*"       Multiply,
    "/"       Divide,
    "negate"  Negate
    "sqrt"    Sqrt
    "square"  Square
    "sum"     Sum
    "avg"     Avg
    "exp"     Exp
    "ln"      Ln
    "sumexp"  Sumexp
    "softmax" Softmax
    "**"      Pow
    "//"      Log
})

(defn parseObject [expression] 
    (let [_expr (if (string? expression) (read-string expression) expression)]
        (letfn 
            [(parse [expr]
            {:pre [
                    (or 
                        (number? expr) 
                        (symbol? expr)
                        (and 
                            (seq? expr)
                            (contains? operationsObject (str (first expr)))
                        ))]} 
                (cond
                    (number? expr) (Constant expr)
                    (symbol? expr) (Variable (str expr))
                    (seq? expr) (apply (get operationsObject (str (first expr))) (map parse (rest expr)))))]
            (parse _expr))
    )
) 

; 12th hw 
(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)
(defn _show [result]
    (if (-valid? result) (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
                        "!"))
(defn tabulate [parser inputs]
    (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (_show (parser input)))) inputs))

(defn _empty [value] (partial -return value))

(defn _char [p]
    (fn [[c & cs]]
        (if (and c (p c)) (-return c cs))))

(defn _map [f result]
    (if (-valid? result)
        (-return (f (-value result)) (-tail result))))

(defn _filter [filt f result]
    (if (and (-valid? result) (-valid? (filt (-value result))))
        (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
    (fn [str]
        (let [ar ((force a) str)]
        (if (-valid? ar)
            (_map (partial f (-value ar))
            ((force b) (-tail ar)))))))

(defn _either [a b]
    (fn [str]
        (let [ar ((force a) str)]
            (if (-valid? ar) ar ((force b) str)))))


(defn _parser [p]
    (fn [input]
        (-value ((_combine (fn [v _] v) p (_char #{\u0000})) (str input \u0000)))))

(defn +char [chars] (_char (set chars)))
(defn +char-not [chars] (_char (comp not (set chars))))
(defn +map [f parser] (comp (partial _map f) parser))
(defn +filter [f g parser] (comp (partial _filter f g) parser))
(def +parser _parser)
(def +ignore (partial +map (constantly 'ignore)))

(defn iconj [coll value]
    (if (= value 'ignore) coll (conj coll value)))
(defn +seq [& ps]
    (reduce (partial _combine iconj) (_empty []) ps))
(defn +seqf [f & ps] (+map (partial apply f) (apply +seq ps)))
(defn +seqn [n & ps] (apply +seqf (fn [& vs] (nth vs n)) ps))

(def +not (fn [p] (comp not p)))
(def +if-not (fn [p1 p2] (fn [input] (if (-valid? ((+not p1) input)) (p2 input)))))

(defn +or [p & ps]
    (reduce _either p ps))
(defn +opt [p]
    (+or p (_empty nil)))
(defn +star [p]
    (letfn [(rec [] (+or (+seqf cons p (delay (rec))) (_empty ())))] (rec)))
(defn +plus [p] (+seqf cons p (+star p)))
(defn +str [p] (+map (partial apply str) p))

(def *digit (+char "0123456789"))
(def *number (+str (+seq (+opt (+char "-")) (+str (+plus *digit)))))
(def *inumber (+map read-string *number))
(def *dnumber (+map read-string (+str (+seq *number (+char ".") (+str (+plus *digit))))))

(def *space (+char " \t\n\r"))
(def *ws (+ignore (+star *space)))

(def *all-chars (mapv char (range 32 128)))
(def *letter (+char (apply str (filter #(Character/isLetter %) *all-chars))))
(def *fun_letter (+or *letter (+char "/+-~!?*'^$<>_")))

(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))

(def *fun (+str (+seqf cons *fun_letter (+star (+or *fun_letter *digit)))))
(def *isfun (+filter #(contains? operationsObject %) (fn [v] v) *fun))
(def *operation (+map #(operationsObject %) *isfun))

(def *term 
    (+seqn 0 
    *ws 
    (+or 
        *dnumber 
        *identifier 
        (+seq 
            (+ignore (+char "(")) 
            *ws (+plus (+seq 
                (delay (+if-not *operation *term)) *ws)) 
            *operation 
            *ws 
            (+ignore (+char ")"))
        )
    ) 
    *ws)
)

(defn transform [val]
    (cond 
        (number? val) (Constant val)
        (string? val) (Variable val) 
        :else (if (= (count val) 1) (transform (first val)) (let [[terms & oper] val] (apply (first oper) (mapv transform terms))))))

(defn *formula [input] (let [val (-value (*term input))] (transform val)))
(def parseObjectSuffix *formula)
