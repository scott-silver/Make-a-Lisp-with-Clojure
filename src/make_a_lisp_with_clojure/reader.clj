(ns make-a-lisp-with-clojure.reader)

;; just treats everything as a string right now
(defn consume-atom [atom]
  [:string atom])

;; consume-list should be given the remainder of a list, (not the opening
;; paren). so if the list of tokens is '( \( \a \) ), then you should only pass
;; '( \a \) ) to consume-list
(defn consume-list
  ([tokens]
   (consume-list tokens []))
  ([tokens accumulated-list]
   (let [first-token (first tokens)]
    (cond
      ;; should throw an error if it runs out of tokens (because it means you're
      ;; missing a close-paren
      (empty? tokens)
      (throw (Exception. "missing close-parens"))

      ;; return accumulated list when you hit a close parens
      (= ")" first-token)
      [[:list accumulated-list] (rest tokens)]

      ;; recur on consume-list when you discover a sub list
      (= "(" first-token)
      (let [[list remaining-tokens] (consume-list (rest tokens) accumulated-list)]
        (recur remaining-tokens (conj accumulated-list list)))

      ;; pass all other tokens to consume-atom
      :else
      (recur (rest tokens) (conj accumulated-list (consume-atom first-token)))))))


;; tokenize
(def token-regex #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:[\\].|[^\\\"])*\"?|;.*|[^\s\[\]{}()'\"`@,;]+)")

(defn tokenize-string
  [str]
  (map second (re-seq token-regex str)))
