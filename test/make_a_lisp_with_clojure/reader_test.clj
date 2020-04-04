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
