(ns make-a-lisp-with-clojure.reader)

;; next

;; peek

;; read-str
;;   calls tokenize with string, pass the list of tokens to read-form

;; read-form

;; read-list

;; read-atom

;; tokenize
(def token-regex #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:[\\].|[^\\\"])*\"?|;.*|[^\s\[\]{}()'\"`@,;]+)")

(defn tokenize-string
  [str]
  (map second (re-seq token-regex str)))
