(ns make-a-lisp-with-clojure.reader)

;; consume-tokens, consume-list and consume-atom work in tandem to turn a list
;; of tokens into a data structure that represents its meaning.
;;
;; for example:
;;
;; '("a") => [[:string "a"]]
;; '("a" "(" ")") => [[:string "a"] [:list []]]
;; '("(" "a" ")") => [[:list [[:string "a"]]]]
;; '("(" "(" ")" ")") => [[:list [[:list []]]]]
;;
;; (see reader_test.clj for more examples)
;;
;; consume-tokens takes a list of tokens and loops through it. when it hits an
;; "atom" (a single, un-nested unit), it calls consume-atom on it. it adds the
;; result to the accumulated list, and then recurs with the remaining tokens.

;; when consume-tokens hits an open-parens, it passes the rest of the list
;; (after the open-parens) to consume-list. consume-list starts a sub-list of
;; atoms and other sub-lists until it hits a close-parens. when it hits a
;; close-parens, it returns the accumulated sub-list and the remaining-tokens
;; that came after the close-parens. consume-tokens recurs on the
;; remaining-tokens, conj'ing the sub-list onto the accumulated list
;;
;; consume-tokens continues until it runs out of tokens, then returns its
;; accumulated result

(def integer-regex #"-?\d+") ;; one or more digits, potentially with a leading `-`
(def symbol-regex #"[^\s]*") ;; any string that contains no whitespace

(defn consume-atom [atom]
  (if-let [int-string (re-matches integer-regex atom)]
    [:integer (read-string int-string)]
    (if-let [symbol-string (re-matches symbol-regex atom)]
      [:symbol symbol-string])))

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
      (let [[list remaining-tokens] (consume-list (rest tokens))]
        (recur remaining-tokens (conj accumulated-list list)))

      ;; pass all other tokens to consume-atom
      :else
      (recur (rest tokens) (conj accumulated-list (consume-atom first-token)))))))

(defn consume-tokens [tokens]
  (loop [remaining-tokens tokens
         accumulated-result []]
    (let [first-token (first remaining-tokens)]
      (cond
        ;; throw an error if you hit a close-parens ")"
        (= ")" first-token)
        (throw (Exception. "unexpected close-parens"))

        (empty? remaining-tokens) accumulated-result

        ;; consume lists
        (= "(" first-token)
        (let [[list tokens-after-list] (consume-list (rest remaining-tokens))]
          (recur tokens-after-list (conj accumulated-result list)))
        ;;
        (not= "(" first-token)
        (recur (rest remaining-tokens) (conj accumulated-result (consume-atom first-token)))))))

(def token-regex #"[\s,]*(~@|[\[\]{}()'`~^@]|\"(?:[\\].|[^\\\"])*\"?|;.*|[^\s\[\]{}()'\"`@,;]+)")

(defn tokenize-string
  [str]
  (map second (re-seq token-regex str)))

(defn read-input-string
  [str]
  (let [token-list (tokenize-string str)]
    (consume-tokens token-list)))
