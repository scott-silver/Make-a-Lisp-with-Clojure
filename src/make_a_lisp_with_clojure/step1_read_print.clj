(ns make-a-lisp-with-clojure.step1-read-print
  (:require [make-a-lisp-with-clojure.reader :as reader]
            [make-a-lisp-with-clojure.printer :as printer])
  (:gen-class))

(defn READ [input-string]
  (reader/read-input-string input-string))

(defn EVAL [a]
  a)

(defn PRINT [data-structures]
  (printer/print-data-structures data-structures))

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
