(ns make-a-lisp-with-clojure.evaluator)

(comment "
  evaluate-ast-item(ast,env):
    switch type(ast):
      symbol:      return lookup(env, ast) OR raise "'" + ast + "' not found"
      list,vector: return ast.map((x) -> evaluate-ast(x,env))
      hash:        return ast.map((k,v) -> list(k, evaluate-ast(v,env)))
      _default_:   return ast

  evaluate-ast(ast,env):
    if not list?(ast): return evaluate-ast-item(ast, env)
    if empty?(ast): return ast
    f, args = evaluate-ast-item(ast, env)
    return apply(f, args)
")

(defn lookup-symbol [[_ symbol-value] env]
  (if-let [value (env symbol-value)]
    value
    (throw (Exception. (str "'" symbol-value "' not found")))))

(declare evaluate-ast)

(defn evaluate-ast-item [ast env]
  (case (first ast)
    :symbol (lookup-symbol ast env)
    :integer (second ast)
    :list (map #(evaluate-ast % env) (second ast))
    ast))

(defn evaluate-ast [ast env]
  (let [item-type (first ast)]
    (cond
      (nil? ast) nil
      (not= :list item-type) (evaluate-ast-item ast env)
      (= :list item-type)
      (let [[func & args] (evaluate-ast-item ast env)]
        (apply func args)))))
