(ns make-a-lisp-with-clojure.evaluator
  (:require [com.walmartlabs.cond-let :refer [cond-let]]))

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
    (cond-let
      (nil? ast)
      [nil env]

      (not= :list item-type)
      [(evaluate-ast-item ast env) env]

      :let [list-contents (second ast)
            first-list-item (first list-contents)]

      (and
       (= :list item-type)
       (= first-list-item [:def]))
      (let [symbol-item (second list-contents)
            symbol-name (second symbol-item)
            value (first (evaluate-ast (nth list-contents 2) env))]
        [value (assoc env symbol-name value)])

      (and
       (= :list item-type)
       (not= first-list-item [:def]))
      (let [[func & args] (evaluate-ast-item ast env)]
        [(apply func args) env]))))
