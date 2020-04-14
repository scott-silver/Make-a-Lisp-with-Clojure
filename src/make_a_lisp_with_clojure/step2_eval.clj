(ns make-a-lisp-with-clojure.step2-eval
  (:require [make-a-lisp-with-clojure.reader :as reader]
            [make-a-lisp-with-clojure.evaluator :as evaluator])
  (:gen-class))

(defn READ [input-string]
  (reader/read-input-string input-string))

(def repl-environment
  {"+" +
   "-" -
   "/" /
   "*" *})

(defn EVAL [ast env]
  (let [[result _] (evaluator/evaluate-ast ast env)]
    result))

(defn PRINT [ast]
  (str ast))

(defn read-eval-print [a]
  (-> a
      (READ)
      (EVAL repl-environment)
      (PRINT)))

(def prompt "user> ")

(defn repl-loop []
  (let [line (do (print prompt) (flush) (read-line))]
    (when line
      (println (read-eval-print line))
      (recur))))

(defn -main [& args]
  (repl-loop))
