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
           '("abc" "123" "true" "false" "nil" "a1b2c3")))))

(deftest data-structure-for-token-list-test
  (testing "returns an empty vector for an empty list"
    (is (= (r/data-structure-for-token-list '()) [])))

  (testing "returns atoms for atomic types" ;; this description sucks
    (is (= (r/data-structure-for-token-list '("a")) [[:string "a"]]))
    (is (= (r/data-structure-for-token-list '("ab")) [[:string "ab"]]))
    (is (= (r/data-structure-for-token-list '("a" "b")) [[:string "a"] [:string "b"]]))))

  ;; (testing "returns a :list for an empty list"
  ;;   (is (= (r/data-structure-for-token-list '("(" ")")) [[:list []]]))))

(deftest consume-list-test
  (testing "throws an error if the list of tokens is missing a close-paren"
    (is (thrown-with-msg? Exception #"missing close-parens" (r/consume-list '()))))

  (testing "returns an empty list when it hits a close-paren `)`"
    (is (= (r/consume-list '(")"))
           [[:list []] '()])))

  (testing "returns items before close-paren as contents of list"
    (is (= (r/consume-list '("a" ")"))
           [[:list [[:string "a"]]] '()])))

  (testing "returns items after close-parens as the second return value"
    (is (= (r/consume-list '(")" "a" "b" "c"))
          [[:list []] '("a" "b" "c")]))

    (is (= (r/consume-list '("a" ")" "b" "c"))
          [[:list [[:string "a"]]] '("b" "c")])))

  (testing "returns nested lists"
    (is (= (r/consume-list '("(" ")" ")"))
           [[:list [[:list []]]] '()]))

    (is (= (r/consume-list '("(" "(" ")" ")" ")"))
           [[:list [[:list [[:list []]]]]] ()]))

    (is (= (r/consume-list '("(" "a" ")" ")"))
           [[:list [[:list [[:string "a"]]]]] '()]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")"))
           [[:list [[:list [[:string "a"]]] [:string "b"]]] '()]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c"))
           [[:list [[:list [[:string "a"]]] [:string "b"]]] '("c")]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c" "d"))
           [[:list [[:list [[:string "a"]]] [:string "b"]]] '("c" "d")]))

    (is (= (r/consume-list '("(" "a" ")" "b" ")" "c" "d" "(" ")"))
           [[:list [[:list [[:string "a"]]] [:string "b"]]] '("c" "d" "(" ")")]))))

