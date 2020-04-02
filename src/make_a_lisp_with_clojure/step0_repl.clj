(ns make-a-lisp-with-clojure.step0-repl
  (:gen-class))

(defn READ [a]
  a)

(defn EVAL [a]
  a)

(defn PRINT [a]
  a)

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
