(ns make-a-lisp-with-clojure.step3-env
  (:require [make-a-lisp-with-clojure.reader :as reader]
            [make-a-lisp-with-clojure.evaluator :as evaluator])
  (:gen-class))

(def base-environment
  {"+" +
   "-" -
   "/" /
   "*" *})

(def repl-environment (atom base-environment))

(defn READ [input-string]
  (reader/read-input-string input-string))

(defn EVAL [ast]
  (let [[result new-env] (evaluator/evaluate-ast ast @repl-environment)]
    (do
      (swap! repl-environment (constantly new-env))
      result)))

(defn PRINT [ast]
  (str ast))

(defn read-eval-print [a]
  (-> a
      (READ)
      (EVAL)
      (PRINT)))

(def prompt "user> ")

(defn repl-loop []
  (let [line (do (print prompt) (flush) (read-line))]
    (when line
      (println (read-eval-print line))
      (recur))))

(defn -main [& args]
  (repl-loop))
