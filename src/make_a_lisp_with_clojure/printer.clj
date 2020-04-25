(ns make-a-lisp-with-clojure.printer
  (:require [clojure.string :as str]))

(defmulti print-ast-item first)

(defmethod print-ast-item :integer
  [integer-structure]
  (str (second integer-structure)))

(defmethod print-ast-item :symbol
  [string-structure]
  (second string-structure))

(defmethod print-ast-item :list
  [list-structure]
  (let [list-contents (second list-structure)
        list-contents-strings (map print-ast-item list-contents)]
    (str "(" (str/join " " list-contents-strings) ")")))

(defmethod print-ast-item :nil
  [_]
  "nil")

(defn print-ast
  [ast]
  (cond
    (nil? ast) ""
    :else (print-ast-item ast)))
