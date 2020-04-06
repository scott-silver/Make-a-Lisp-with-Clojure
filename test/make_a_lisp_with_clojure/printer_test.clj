(ns make-a-lisp-with-clojure.printer-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.printer :as p]))

(deftest print-ast-test
  (testing "it returns an empty string when called with nil"
    (is (= "" (p/print-ast nil))))

  (testing "it returns `()` for an empty list"
    (is (= "()" (p/print-ast [:list []]))))

  (testing "it handles nested lists"
    (is (= "((()))" (p/print-ast [:list [[:list [[:list []]]]]]))))

  (testing "it returns the values of symbols"
    (is (= "a" (p/print-ast [:symbol "a"])))
    (is (= "abc" (p/print-ast [:symbol "abc"])))
    (is (= "+" (p/print-ast [:symbol "+"])))
    (is (= "-" (p/print-ast [:symbol "-"]))))

  (testing "it returns stringified integers"
    (is (= "0" (p/print-ast [:integer 0])))
    (is (= "1" (p/print-ast [:integer 1])))
    (is (= "-123" (p/print-ast [:integer -123]))))

  (testing "it spaces out the contents of lists"
    (is (= "(a b c)"
           (p/print-ast
            [:list [[:symbol "a"] [:symbol "b"] [:symbol "c"]]])))
    (is (= "(1 2 3)"
           (p/print-ast
            [:list [[:integer 1] [:integer 2] [:integer 3]]])))
    (is (= "(a (b) c)"
          (p/print-ast
           [:list [[:symbol "a"] [:list [[:symbol "b"]]] [:symbol "c"]]])))))
