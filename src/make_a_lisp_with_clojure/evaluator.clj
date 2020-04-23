(ns make-a-lisp-with-clojure.evaluator
  (:require [com.walmartlabs.cond-let :refer [cond-let]]))

(defn lookup-symbol [[_ symbol-value] env]
  (if-let [value (env symbol-value)]
    value
    (throw (Exception. (str "Symbol '" symbol-value "' not found")))))

(declare evaluate-ast)

(defn evaluate-ast-item [ast env]
  (case (first ast)
    :nil nil
    :true true
    :false false
    :symbol (lookup-symbol ast env)
    :integer (second ast)
    :list (map #(first (evaluate-ast % env)) (second ast))
    ast))

(declare accumulate-env)

(defn evaluate-ast [ast env]
  (let [item-type (first ast)]
    (cond-let
      (nil? ast)
      [nil env]

      (not= :list item-type)
      [(evaluate-ast-item ast env) env]

      ;; list types
      :let [list-contents (second ast)
            first-list-item (first list-contents)]

      ;; def
      (and
       (= :list item-type)
       (= first-list-item [:def]))
      (let [symbol-item (second list-contents)
            symbol-name (second symbol-item)
            value (first (evaluate-ast (nth list-contents 2) env))]
        [value (assoc env symbol-name value)])

      ;; let
      (and
       (= :list item-type)
       (= first-list-item [:let]))
      (let [redef-list (second list-contents) ;; [[:let] [:list [a b c d]] => [:list [a b c d]]
            redef-list-items (second redef-list) ;; [:list [a b c d]] => [a b c d]
            redef-key-value-pairs (partition 2 redef-list-items) ;; [a b c d] => [[a b] [c d]]
            let-env (reduce accumulate-env env redef-key-value-pairs)
            value (first (evaluate-ast (nth list-contents 2) let-env))]
        [value env])

      ;; if
      (and
       (= :list item-type)
       (= first-list-item [:if]))
      (let [condition (get list-contents 1)
            evaluated-condition (first (evaluate-ast condition env))
            consequent (get list-contents 2)
            alternative (get list-contents 3)]
        (if evaluated-condition
            (evaluate-ast consequent env)
            (evaluate-ast alternative env)))

      (and
       (= :list item-type)
       (not= first-list-item [:def]))
      (let [[func & args] (evaluate-ast-item ast env)]
        [(apply func args) env]))))

(defn accumulate-env
  "for use with `reduce`: (reduce accumulate-env env key-expression-pairs)

  takes an env and a key-expression pair, evaluates the expression and returns a
  new env with the symbol name mapped to the evaluted expression

  env - an env object: ({:outer {} :data {}})

  key-expression-pair - a tuple containing a symbol and an evaluable ast expression
  [[:symbol `a`] [:integer 1]]
  [[:symbol `a`]
   [:list [[:symbol `plus`] [:integer 1] [:integer 2]]]
  "
  [env [symbol-item expression]]
  (let [[evaluated-value _] (evaluate-ast expression env)
        symbol-name (second symbol-item)]
    (assoc env symbol-name evaluated-value)))
