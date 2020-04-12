(ns make-a-lisp-with-clojure.evaluator)

(defn lookup-symbol [[_ symbol-value] env]
  (if-let [value (env symbol-value)]
    value
    (throw (Exception. (str "'" symbol-value "' not found")))))

(declare evaluate-ast)

(defn evaluate-ast-item [ast env]
  (case (first ast)
    :symbol (lookup-symbol ast env)
    :integer (second ast)
    :list (map #(first (evaluate-ast % env)) (second ast))
    ast))

(defn evaluate-ast [ast env]
  (let [item-type (first ast)]
    (cond
      (nil? ast)
      [nil env]

      (not= :list item-type)
      [(evaluate-ast-item ast env) env]

      (= :list item-type)
      (let [[func & args] (evaluate-ast-item ast env)]
        [(apply func args) env]))))
