(ns make-a-lisp-with-clojure.printer-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.printer :as p]))

(deftest print-data-structures-test
  (testing "it returns an empty string for an empty data-structure list"
    (is (= "" (p/print-data-structures []))))

  (testing "it returns `()` for an empty list"
    (is (= "()" (p/print-data-structures [[:list []]]))))

  (testing "it handles nested lists"
    (is (= "((()))" (p/print-data-structures [[:list [[:list [[:list []]]]]]]))))

  (testing "it returns the contents of strings"
    (is (= "abc" (p/print-data-structures [[:string "abc"]])))
    (is (= "a b c" (p/print-data-structures [[:string "a"] [:string "b"] [:string "c"]]))))

  (testing "it spaces out the contents of lists"
    (is (= "(a b c)"
           (p/print-data-structures
            [[:list [[:string "a"] [:string "b"] [:string "c"]]]])))
    (is (= "(a (b) c)"
          (p/print-data-structures
            [[:list [[:string "a"] [:list [[:string "b"]]] [:string "c"]]]])))))
