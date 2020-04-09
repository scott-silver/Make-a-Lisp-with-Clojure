(ns make-a-lisp-with-clojure.step2-eval-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.step2-eval :as s2]))

(deftest step2-eval-test
  (testing "evaluation of arithmetic operations"
    (is (= (s2/read-eval-print "(+ 1 2)") "3"))
    (is (= (s2/read-eval-print "(+ 5 (* 2 3))") "11"))
    (is (= (s2/read-eval-print "(- (+ 5 (* 2 3)) 3)") "8"))
    (is (= (s2/read-eval-print "(/ (- (+ 5 (* 2 3)) 3) 4)") "2"))
    (is (= (s2/read-eval-print "(/ (- (+ 515 (* 87 311)) 302) 27)") "1010"))
    (is (= (s2/read-eval-print "(* -3 6)") "-18"))
    (is (= (s2/read-eval-print "(/ (- (+ 515 (* -87 311)) 296) 27)") "-994"))))
