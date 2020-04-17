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

(deftest let-test
  (testing "allows you to define local values with let"
    (is (= (s3/read-eval-print "(let (a 1) a)") "1"))))

;; from mal
(deftest mal-step3-tests
  (testing "Testing REPL_ENV"
    (is (= (s3/read-eval-print "(+ 1 2)") "3"))
    (is (= (s3/read-eval-print "(/ (- (+ 5 (* 2 3)) 3) 4)") "2")))

  (testing "Testing def"
    (is (= (s3/read-eval-print "(def x 3)") "3"))
    (is (= (s3/read-eval-print "x") "3"))
    (is (= (s3/read-eval-print "(def x 4)") "4"))
    (is (= (s3/read-eval-print "x") "4"))
    (is (= (s3/read-eval-print "(def y (+ 1 7))") "8"))
    (is (= (s3/read-eval-print "y") "8")))

  (testing "Verifying symbols are case-sensitive"
    (is (= (s3/read-eval-print "(def mynum 111)") "111"))
    (is (= (s3/read-eval-print "(def MYNUM 222)") "222"))
    (is (= (s3/read-eval-print "mynum") "111"))
    (is (= (s3/read-eval-print "MYNUM") "222")))

  ;; (testing "Check env lookup non-fatal error"
  ;;   (is (thrown-with-msg? #"not found" (s3/read-eval-print "(abc 1 2 3)")))))

  ;; (testing "Check that error aborts def!"
  ;;   (is (thrown-with-msg? #"not found" (s3/read-eval-print "(abc 1 2 3)")))))
  ;; Check that error aborts def!
  ;; (def! w 123)
  ;; (def! w (abc))
  ;; w
  ;; =>123

  (testing "Testing let"
    (is (= (s3/read-eval-print "(let (z 9) z)") "9"))
    (is (= (s3/read-eval-print "(let (x 9) x)") "9"))
    ;; callback to the value defined on line 38
    (is (= (s3/read-eval-print "x") "4"))

    (is (= (s3/read-eval-print "(let (z (+ 2 3)) (+ 1 z))") "6"))
    (is (= (s3/read-eval-print "(let (p (+ 2 3) q (+ 2 p)) (+ p q))") "12"))

    (is (= (s3/read-eval-print "(def y (let (z 7) z))") "7"))
    (is (= (s3/read-eval-print "y") "7")))

  (testing "Testing outer environment"
    (is (= (s3/read-eval-print "(def a 4)") "4"))
    (is (= (s3/read-eval-print "(let (q 9) q)") "9"))
    (is (= (s3/read-eval-print "(let (q 9) a)") "4"))
    (is (= (s3/read-eval-print "(let (z 2) (let (q 9) a))") "4"))))
