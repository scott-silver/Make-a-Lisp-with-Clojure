(ns make-a-lisp-with-clojure.step3-env-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.step3-env :as s3]))

(deftest step3-env-test
  (testing "evaluation of arithmetic operations"
    (is (= (s3/read-eval-print "(+ 1 2)") "3"))
    (is (= (s3/read-eval-print "(+ 5 (* 2 3))") "11"))
    (is (= (s3/read-eval-print "(- (+ 5 (* 2 3)) 3)") "8"))
    (is (= (s3/read-eval-print "(/ (- (+ 5 (* 2 3)) 3) 4)") "2"))
    (is (= (s3/read-eval-print "(/ (- (+ 515 (* 87 311)) 302) 27)") "1010"))
    (is (= (s3/read-eval-print "(* -3 6)") "-18"))
    (is (= (s3/read-eval-print "(/ (- (+ 515 (* -87 311)) 296) 27)") "-994"))))

(deftest def-test
  ;; atoms are reset between test functions, but not between different "testing"
  ;; blocks
  (testing "defining and returning values"
    (is (= (s3/read-eval-print "(def a 1)") "1"))
    (is (= (s3/read-eval-print "a") "1"))
    (is (= (s3/read-eval-print "(def b (+ a 1))") "2"))
    (is (= (s3/read-eval-print "b") "2"))
    (is (= (s3/read-eval-print "(def c (+ a b))") "3"))
    (is (= (s3/read-eval-print "c") "3"))
    (is (= (s3/read-eval-print "(+ a b c)") "6"))))
