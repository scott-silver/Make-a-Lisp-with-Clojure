(ns make-a-lisp-with-clojure.reader-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.reader :as r]))

(deftest tokenize-string-test
  (testing "ignores whitespace"
    (is (= (r/tokenize-string "      ") '())))

  (testing "ignores commas"
    (is (= (r/tokenize-string ",, ,,, ,") '())))

  (testing "captures `~@`"
    (is (= (r/tokenize-string "~@") '("~@"))))

  (testing "captures special characters"
    (is (= (r/tokenize-string "[]{}()'`~^@")
           '("[" "]" "{" "}" "(" ")" "'" "`" "~" "^" "@"))))

  (testing "captures comments as a single string"
    (is (= (r/tokenize-string ";abc def ghi")
           '(";abc def ghi"))))

  (testing "captures groups of characters and numbers"
    (is (= (r/tokenize-string "abc 123 true false nil a1b2c3")
           '("abc" "123" "true" "false" "nil" "a1b2c3"))))

  (testing "captures defs"
    (is (= (r/tokenize-string "(def a 6)")
          '("(" "def" "a" "6" ")")))))

(deftest consume-atom-test
  (testing "returns integer types"
    (is (= (r/consume-atom "0") [:integer 0]))
    (is (= (r/consume-atom "123") [:integer 123]))
    (is (= (r/consume-atom "-50") [:integer -50])))

  (testing "returns symbols"
    (is (= (r/consume-atom "+") [:symbol "+"]))
    (is (= (r/consume-atom "-") [:symbol "-"]))
    (is (= (r/consume-atom "a") [:symbol "a"]))
    (is (= (r/consume-atom "abc") [:symbol "abc"]))
    (is (= (r/consume-atom "abc123") [:symbol "abc123"]))
    (is (= (r/consume-atom "abc-def") [:symbol "abc-def"]))
    (is (= (r/consume-atom "-abc") [:symbol "-abc"]))
    (is (= (r/consume-atom "->>") [:symbol "->>"])))

  (testing "returns defs"
    (is (= (r/consume-atom "def") [:def]))))

(deftest consume-list-test
  (testing "throws an error if the list of tokens is missing a close-paren"
    (is (thrown-with-msg? Exception #"missing close-parens" (r/consume-list '()))))

  (testing "returns an empty list when it hits a close-paren `)`"
    (is (= (r/consume-list '(")"))
           [[:list []] '()])))

  (testing "returns items before close-paren as contents of list"
    (is (= (r/consume-list '("a" ")"))
           [[:list [[:symbol "a"]]] '()])))

  (testing "returns items after close-parens as the second return value"
    (is (= (r/consume-list '(")" "a" "b" "c"))
           [[:list []] '("a" "b" "c")]))

    (is (= (r/consume-list '("a" ")" "b" "c"))
           [[:list [[:symbol "a"]]] '("b" "c")])))

  (testing "returns nested lists"
    (is (= (r/consume-list '("(" ")" ")"))
          [[:list [[:list []]]] '()]))

    (is (= (r/consume-list '("(" "(" ")" ")" ")"))
           [[:list [[:list [[:list []]]]]] ()]))

    (is (= (r/consume-list '("(" "a" ")" ")"))
           [[:list [[:list [[:symbol "a"]]]]] '()]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")"))
           [[:list [[:list [[:symbol "a"]]] [:symbol "b"]]] '()]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c"))
           [[:list [[:list [[:symbol "a"]]] [:symbol "b"]]] '("c")]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c" "d"))
           [[:list [[:list [[:symbol "a"]]] [:symbol "b"]]] '("c" "d")]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c" "d" "(" ")"))
           [[:list [[:list [[:symbol "a"]]] [:symbol "b"]]] '("c" "d" "(" ")")])))

  (testing "returns defs"
    (is (= (r/consume-list '("def" "a" "1" ")"))
           [[:list [[:def] [:symbol "a"] [:integer 1]]] '()]))))

(deftest consume-tokens-test
  (testing "throws an error it encounters a close-paren"
    (is (thrown-with-msg? Exception #"unexpected close-parens" (r/consume-tokens '(")")))))

  (testing "returns nil for an empty list of tokens"
    (is (= (r/consume-tokens '()) nil)))

  (testing "consumes atoms"
    (is (= (r/consume-tokens '("a")) [:symbol "a"]))
    (is (= (r/consume-tokens '("ab")) [:symbol "ab"]))
    (is (= (r/consume-tokens '("+")) [:symbol "+"]))
    (is (= (r/consume-tokens '("36")) [:integer 36]))
    (is (= (r/consume-tokens '("-584")) [:integer -584])))

  (testing "consumes lists"
    (is (= (r/consume-tokens '("(" ")")) [:list []]))
    (is (= (r/consume-tokens '("(" "a" ")")) [:list [[:symbol "a"]]]))
    (is (= (r/consume-tokens '("(" "a" "b" "c" ")"))
           [:list [[:symbol "a"] [:symbol "b"] [:symbol "c"]]])))

  (testing "consumes lists within lists"
    (is (= (r/consume-tokens '("(" "(" ")" ")")) [:list [[:list []]]]))
    (is (= (r/consume-tokens '("(" "(" "a" ")" ")")) [:list [[:list [[:symbol "a"]]]]]))
    (is (= (r/consume-tokens '("("  "a" "(" "b" ")" ")"))
           [:list [[:symbol "a"] [:list [[:symbol "b"]]]]]))
    (is (= (r/consume-tokens '("("  "a" "(" "b" ")" "c" ")"))
           [:list [[:symbol "a"] [:list [[:symbol "b"]]] [:symbol "c"]]])))

  (testing "correctly handles combinations of lists and atoms"
    (is (= (r/consume-tokens '("(" "a" "(" "b" ")" "c" "d" "(" ")" ")"))
           [:list [[:symbol "a"]
                   [:list [[:symbol "b"]]]
                   [:symbol "c"]
                   [:symbol "d"]
                   [:list []]]]))))
