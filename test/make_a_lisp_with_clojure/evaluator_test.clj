(ns make-a-lisp-with-clojure.evaluator-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.evaluator :as e]))

(deftest lookup-symbol-test
  (testing "returns value from env"
    (is (= (e/lookup-symbol [:symbol "a"] {"a" 1}) 1))
    (is (= (e/lookup-symbol [:symbol "plus"] {"plus" +}) +)))

  (testing "raises when missing"
    (is (thrown-with-msg?
         Exception #"'b' not found"
         (e/lookup-symbol [:symbol "b"] {"a" 1})))
    (is (thrown-with-msg?
         Exception #"'plus' not found"
         (e/lookup-symbol [:symbol "plus"] {"minus" -})))))

(deftest evaluate-ast-item-test
  (testing "evaluates symbols"
    (is (= (e/evaluate-ast-item [:symbol "plus"] {"plus" +}) +))
    (is (= (e/evaluate-ast-item [:symbol "a"] {"a" 10}) 10)))

  (testing "evaluates integers"
    (is (= (e/evaluate-ast-item [:integer 10] {}) 10))
    (is (= (e/evaluate-ast-item [:integer -123] {}) -123)))

  (testing "evaluates nil"
    (is (= nil
           (e/evaluate-ast-item [:nil] {}))))

  (testing "evaluates bools"
    (is (= true
           (e/evaluate-ast-item [:true] {})))
    (is (= false
           (e/evaluate-ast-item [:false] {})))))

(deftest evaluate-ast-test
  (testing "returns nil (and an unchanged env) for a nil ast"
    (is (= (e/evaluate-ast nil {})
           [nil {}])))

  (testing "returns nil (and an unchanged env) for the nil atom"
    (is (= (e/evaluate-ast [:nil] {})
           [nil {}])))

  (testing "returns boolean value (and an unchanged env) for bool atoms"
    (is (= [true {}] (e/evaluate-ast [:true] {})))
    (is (= [false {}] (e/evaluate-ast [:false] {}))))

  (testing "returns symbol values (and an unchanged env)"
    (is (= (e/evaluate-ast [:symbol "plus"] {"plus" +})
           [+ {"plus" +}]))
    (is (= (e/evaluate-ast [:symbol "a"] {"a" 10})
           [10 {"a" 10}])))

  (testing "returns integers (and an unchanged env)"
    (is (= (e/evaluate-ast [:integer 10] {})
           [10 {}]))
    (is (= (e/evaluate-ast [:integer -123] {})
           [-123 {}])))

  (testing "applies first item in list to the remainder of the list (and does not change env)"
    (is (= (e/evaluate-ast
            [:list [[:symbol "plus"] [:integer 1] [:integer 2]]]
            {"plus" +})
           [3 {"plus" +}]))

    (is (= (e/evaluate-ast
            [:list [[:symbol "plus"] [:integer 1] [:integer 2] [:integer 3]]]
            {"plus" +})
           [6 {"plus" +}]))

    (is (= (e/evaluate-ast
            [:list [[:symbol "plus"]
                    [:integer 5]
                    [:list [[:symbol "multiply"]
                            [:integer 2]
                            [:integer 3]]]]]
            {"plus"     +
             "multiply" *})
           [11 {"plus" + "multiply" *}]))

    (is (= (e/evaluate-ast
            [:list [[:symbol "subtract"]
                    [:list [[:symbol "add"]
                            [:integer 5]
                            [:list [[:symbol "multiply"]
                                    [:integer 2]
                                    [:integer 3]]]]]
                    [:integer 3]]]
            {"subtract" -
             "add"      +
             "multiply" *})
           [8 {"subtract" -
               "add"      +
               "multiply" *}])))

  (testing "evaluates defs and adds them to the env"
    (is (= [1 {"a" 1}]
           (e/evaluate-ast
                [:list [[:def] [:symbol "a"] [:integer 1]]]
                {})))

    (is (= [3 {"plus" +
               "a" 1
               "b" 3}]
          (e/evaluate-ast
           [:list [[:def]
                   [:symbol "b"]
                   [:list [[:symbol "plus"]
                           [:symbol "a"]
                           [:integer 2]]]]]
           {"plus" +
            "a" 1}))))

  (testing "evaluates let definitions but does not add them to the env"
    (is (= [1 {}]
           (e/evaluate-ast
            [:list [[:let]
                    [:list [[:symbol "a"]
                            [:integer 1]]]
                    [:symbol "a"]]]
            {})))

    (is (= [3 {"plus" +}]
          (e/evaluate-ast
            [:list [[:let]
                    [:list [[:symbol "a"]
                            [:integer 1]
                            [:symbol "b"]
                            [:integer 2]]]
                    [:list [[:symbol "plus"]
                            [:symbol "a"]
                            [:symbol "b"]]]]]
            {"plus" +})))

    (is (= [6 {"plus" +}]
          (e/evaluate-ast
            [:list [[:let]
                    [:list [[:symbol "a"]
                            [:integer 1]
                            [:symbol "b"]
                            [:integer 2]
                            [:symbol "c"]
                            [:list [[:symbol "plus"]
                                    [:symbol "a"]
                                    [:symbol "b"]]]]]
                    [:list [[:symbol "plus"]
                            [:symbol "a"]
                            [:symbol "b"]
                            [:symbol "c"]]]]]
            {"plus" +}))))

  (testing "evalutes 'if' statements"
    (is (= [1 {}]
           (e/evaluate-ast
              [:list [[:if] [:true] [:integer 1] [:integer 0]]]
              {})))
    (is (= [0 {}]
          (e/evaluate-ast
            [:list [[:if] [:false] [:integer 1] [:integer 0]]]
            {}))))

  (testing "evalutes one-way 'if' statements"
    (is (= [nil {}]
          (e/evaluate-ast
            [:list [[:if] [:false] [:list [[:symbol "plus"] [:integer 1] [:integer 7]]]]]
            {})))))
