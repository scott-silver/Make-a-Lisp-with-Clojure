(ns make-a-lisp-with-clojure.step4-if-fn-do-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.step4-if-fn-do :as s4]))

(deftest step4-test
  (testing "nil"
    (is (= "nil" (s4/read-eval-print "nil"))))

  (testing "booleans"
    (is (= "true" (s4/read-eval-print "true")))
    (is (= "false" (s4/read-eval-print "false"))))

  (testing "list functions"
    ;; list
    (is (= "()" (s4/read-eval-print "(list)")))
    ;; list?
    (is (= "true" (s4/read-eval-print "(list? (list))")))
    ;; empty?
    (is (= "true" (s4/read-eval-print "(empty? (list))")))
    (is (= "false" (s4/read-eval-print "(empty? (list 1))")))
    ;; populating lists
    (is (= "(1 2 3)" (s4/read-eval-print "(list 1 2 3)")))
    ;; count
    (is (= "3" (s4/read-eval-print "(count (list 1 2 3))")))
    (is (= "0" (s4/read-eval-print "(count (list))")))
    (is (= "0" (s4/read-eval-print "(count nil)"))))

  (testing "if form"
    (is (= "7" (s4/read-eval-print "(if true 7 8)")))
    (is (= "8" (s4/read-eval-print "(if false 7 8)")))
    (is (= "false" (s4/read-eval-print "(if false 7 false)")))
    (is (= "8" (s4/read-eval-print "(if true (+ 1 7) (+ 1 8))")))
    (is (= "9" (s4/read-eval-print "(if false (+ 1 7) (+ 1 8))")))
    (is (= "8" (s4/read-eval-print "(if nil 7 8)")))
    (is (= "7" (s4/read-eval-print "(if 0 7 8)")))
    (is (= "7" (s4/read-eval-print "(if (list) 7 8)")))
    (is (= "7" (s4/read-eval-print "(if (list 1 2 3) 7 8)"))))

  (testing "1-way if form"
    (is (= "nil" (s4/read-eval-print "(if false (+ 1 7))")))
    (is (= "nil" (s4/read-eval-print "(if nil 8)")))
    (is (= "7" (s4/read-eval-print "(if nil 8 7)")))
    (is (= "8" (s4/read-eval-print "(if true (+ 1 7))"))))

  (testing "basic conditionals"
    (is (= "false" (s4/read-eval-print "(= 2 1)")))
    (is (= "true" (s4/read-eval-print "(= 1 1)")))
    (is (= "false" (s4/read-eval-print "(= 1 2)")))
    (is (= "false" (s4/read-eval-print "(= 1 (+ 1 1))")))
    (is (= "true" (s4/read-eval-print "(= 2 (+ 1 1))")))
    (is (= "false" (s4/read-eval-print "(= nil 1)")))
    (is (= "true" (s4/read-eval-print "(= nil nil)")))
    (is (= "false" (s4/read-eval-print "(= (list) nil)"))))

  (testing "greater than"
    (is (= "true" (s4/read-eval-print "(> 2 1)")))
    (is (= "false" (s4/read-eval-print "(> 1 1)")))
    (is (= "false" (s4/read-eval-print "(> 1 2)"))))

  (testing "greater than or equal to"
    (is (= "true" (s4/read-eval-print "(>= 2 1)")))
    (is (= "true" (s4/read-eval-print "(>= 1 1)")))
    (is (= "false" (s4/read-eval-print "(>= 1 2)"))))

  (testing "less than"
    (is (= "false" (s4/read-eval-print "(< 2 1)")))
    (is (= "false" (s4/read-eval-print "(< 1 1)")))
    (is (= "true" (s4/read-eval-print "(< 1 2)"))))

  (testing "less than or equal to"
    (is (= "false" (s4/read-eval-print "(<= 2 1)")))
    (is (= "true" (s4/read-eval-print "(<= 1 1)")))
    (is (= "true" (s4/read-eval-print "(<= 1 2)"))))

  (testing "equals"
    (is (= "true" (s4/read-eval-print "(= 1 1)")))
    (is (= "true" (s4/read-eval-print "(= 0 0)")))
    (is (= "false" (s4/read-eval-print "(= 1 0)")))
    (is (= "true" (s4/read-eval-print "(= true true)")))
    (is (= "true" (s4/read-eval-print "(= false false)")))
    (is (= "true" (s4/read-eval-print "(= nil nil)")))
    (is (= "true" (s4/read-eval-print "(= (list) (list))")))
    (is (= "true" (s4/read-eval-print "(= (list 1 2) (list 1 2))")))
    (is (= "false" (s4/read-eval-print "(= (list 1) (list))")))
    (is (= "false" (s4/read-eval-print "(= (list) (list 1))")))
    (is (= "false" (s4/read-eval-print "(= 0 (list))")))
    (is (= "false" (s4/read-eval-print "(= (list) 0)")))
    (is (= "false" (s4/read-eval-print "(= (list nil) (list))"))))

  (testing "combinations"
    (is (= "78" (s4/read-eval-print "(if (> (count (list 1 2 3)) 3) 89 78)")))
    (is (= "89" (s4/read-eval-print "(if (>= (count (list 1 2 3)) 3) 89 78)"))))

  (testing "builtin functions"
    (is (= "3" (s4/read-eval-print "(+ 1 2)"))))

  (testing "user-defined functions"
    (is (= "7" (s4/read-eval-print "((fn (a b) (+ b a)) 3 4)")))))

;; ;; Testing builtin and user defined functions
;; ( (fn* (a b) (+ b a)) 3 4)
;; ;=>7
;; ( (fn* () 4) )
;; ;=>4
;;
;; ( (fn* (f x) (f x)) (fn* (a) (+ 1 a)) 7)
;; ;=>8
;;
;;
;; ;; Testing closures
;; ( ( (fn* (a) (fn* (b) (+ a b))) 5) 7)
;; ;=>12
;;
;; (def! gen-plus5 (fn* () (fn* (b) (+ 5 b))))
;; (def! plus5 (gen-plus5))
;; (plus5 7)
;; ;=>12
;;
;; (def! gen-plusX (fn* (x) (fn* (b) (+ x b))))
;; (def! plus7 (gen-plusX 7))
;; (plus7 8)
;; ;=>15
;;
;; ;; Testing do form
;; (do (prn 101))
;; ;/101
;; ;=>nil
;; (do (prn 102) 7)
;; ;/102
;; ;=>7
;; (do (prn 101) (prn 102) (+ 1 2))
;; ;/101
;; ;/102
;; ;=>3
;;
;; (do (def! a 6) 7 (+ a 8))
;; ;=>14
;; a
;; ;=>6
;;
;; ;; Testing special form case-sensitivity
;; (def! DO (fn* (a) 7))
;; (DO 3)
;; ;=>7
;;
;; ;; Testing recursive sumdown function
;; (def! sumdown (fn* (N) (if (> N 0) (+ N (sumdown  (- N 1))) 0)))
;; (sumdown 1)
;; ;=>1
;; (sumdown 2)
;; ;=>3
;; (sumdown 6)
;; ;=>21
;;
;;
;; ;; Testing recursive fibonacci function
;; (def! fib (fn* (N) (if (= N 0) 1 (if (= N 1) 1 (+ (fib (- N 1)) (fib (- N 2)))))))
;; (fib 1)
;; ;=>1
;; (fib 2)
;; ;=>2
;; (fib 4)
;; ;=>5
;;
;;
;; ;; Testing recursive function in environment.
;; (let* (cst (fn* (n) (if (= n 0) nil (cst (- n 1))))) (cst 1))
;; ;=>nil
;; (let* (f (fn* (n) (if (= n 0) 0 (g (- n 1)))) g (fn* (n) (f n))) (f 2))
;; ;=>0
;;
;;
;; ;>>> deferrable=True
;; ;;
;; ;; -------- Deferrable Functionality --------
;;
;; ;; Testing if on strings
;;
;; (if "" 7 8)
;; ;=>7
;;
;; ;; Testing string equality
;;
;; (= "" "")
;; ;=>true
;; (= "abc" "abc")
;; ;=>true
;; (= "abc" "")
;; ;=>false
;; (= "" "abc")
;; ;=>false
;; (= "abc" "def")
;; ;=>false
;; (= "abc" "ABC")
;; ;=>false
;; (= (list) "")
;; ;=>false
;; (= "" (list))
;; ;=>false
;;
;; ;; Testing variable length arguments
;;
;; ( (fn* (& more) (count more)) 1 2 3)
;; ;=>3
;; ( (fn* (& more) (list? more)) 1 2 3)
;; ;=>true
;; ( (fn* (& more) (count more)) 1)
;; ;=>1
;; ( (fn* (& more) (count more)) )
;; ;=>0
;; ( (fn* (& more) (list? more)) )
;; ;=>true
;; ( (fn* (a & more) (count more)) 1 2 3)
;; ;=>2
;; ( (fn* (a & more) (count more)) 1)
;; ;=>0
;; ( (fn* (a & more) (list? more)) 1)
;; ;=>true
;;
;;
;; ;; Testing language defined not function
;; (not false)
;; ;=>true
;; (not nil)
;; ;=>true
;; (not true)
;; ;=>false
;; (not "a")
;; ;=>false
;; (not 0)
;; ;=>false
;;
;;
;; ;; -----------------------------------------------------
;;
;; ;; Testing string quoting
;;
;; ""
;; ;=>""
;;
;; "abc"
;; ;=>"abc"
;;
;; "abc  def"
;; ;=>"abc  def"
;;
;; "\""
;; ;=>"\""
;;
;; "abc\ndef\nghi"
;; ;=>"abc\ndef\nghi"
;;
;; "abc\\def\\ghi"
;; ;=>"abc\\def\\ghi"
;;
;; "\\n"
;; ;=>"\\n"
;;
;; ;; Testing pr-str
;;
;; (pr-str)
;; ;=>""
;;
;; (pr-str "")
;; ;=>"\"\""
;;
;; (pr-str "abc")
;; ;=>"\"abc\""
;;
;; (pr-str "abc  def" "ghi jkl")
;; ;=>"\"abc  def\" \"ghi jkl\""
;;
;; (pr-str "\"")
;; ;=>"\"\\\"\""
;;
;; (pr-str (list 1 2 "abc" "\"") "def")
;; ;=>"(1 2 \"abc\" \"\\\"\") \"def\""
;;
;; (pr-str "abc\ndef\nghi")
;; ;=>"\"abc\\ndef\\nghi\""
;;
;; (pr-str "abc\\def\\ghi")
;; ;=>"\"abc\\\\def\\\\ghi\""
;;
;; (pr-str (list))
;; ;=>"()"
;;
;; ;; Testing str
;;
;; (str)
;; ;=>""
;;
;; (str "")
;; ;=>""
;;
;; (str "abc")
;; ;=>"abc"
;;
;; (str "\"")
;; ;=>"\""
;;
;; (str 1 "abc" 3)
;; ;=>"1abc3"
;;
;; (str "abc  def" "ghi jkl")
;; ;=>"abc  defghi jkl"
;;
;; (str "abc\ndef\nghi")
;; ;=>"abc\ndef\nghi"
;;
;; (str "abc\\def\\ghi")
;; ;=>"abc\\def\\ghi"
;;
;; (str (list 1 2 "abc" "\"") "def")
;; ;=>"(1 2 abc \")def"
;;
;; (str (list))
;; ;=>"()"
;;
;; ;; Testing prn
;; (prn)
;; ;/
;; ;=>nil
;;
;; (prn "")
;; ;/""
;; ;=>nil
;;
;; (prn "abc")
;; ;/"abc"
;; ;=>nil
;;
;; (prn "abc  def" "ghi jkl")
;; ;/"abc  def" "ghi jkl"
;;
;; (prn "\"")
;; ;/"\\"")
;; ;=>nil
;;
;; (prn "abc\ndef\nghi")
;; ;/"abc\\ndef\\nghi"
;; ;=>nil
;;
;; (prn "abc\\def\\ghi")
;; ;/"abc\\\\def\\\\ghi"
;; nil
;;
;; (prn (list 1 2 "abc" "\"") "def")
;; ;/\(1 2 "abc" "\\""\) "def"
;; ;=>nil
;;
;;
;; ;; Testing println
;; (println)
;; ;/
;; ;=>nil
;;
;; (println "")
;; ;/
;; ;=>nil
;;
;; (println "abc")
;; ;/abc
;; ;=>nil
;;
;; (println "abc  def" "ghi jkl")
;; ;/abc  def ghi jkl
;;
;; (println "\"")
;; ;/"
;; ;=>nil
;;
;; (println "abc\ndef\nghi")
;; ;/abc
;; ;/def
;; ;/ghi
;; ;=>nil
;;
;; (println "abc\\def\\ghi")
;; ;/abc\\def\\ghi
;; ;=>nil
;;
;; (println (list 1 2 "abc" "\"") "def")
;; ;/\(1 2 abc "\) def
;; ;=>nil
;;
;;
;; ;; Testing keywords
;; (= :abc :abc)
;; ;=>true
;; (= :abc :def)
;; ;=>false
;; (= :abc ":abc")
;; ;=>false
;; (= (list :abc) (list :abc))
;; ;=>true
;;
;; ;; Testing vector truthiness
;; (if [] 7 8)
;; ;=>7
;;
;; ;; Testing vector printing
;; (pr-str [1 2 "abc" "\""] "def")
;; ;=>"[1 2 \"abc\" \"\\\"\"] \"def\""
;;
;; (pr-str [])
;; ;=>"[]"
;;
;; (str [1 2 "abc" "\""] "def")
;; ;=>"[1 2 abc \"]def"
;;
;; (str [])
;; ;=>"[]"
;;
;;
;; ;; Testing vector functions
;; (count [1 2 3])
;; ;=>3
;; (empty? [1 2 3])
;; ;=>false
;; (empty? [])
;; ;=>true
;; (list? [4 5 6])
;; ;=>false
;;
;; ;; Testing vector equality
;; (= [] (list))
;; ;=>true
;; (= [7 8] [7 8])
;; ;=>true
;; (= [:abc] [:abc])
;; ;=>true
;; (= (list 1 2) [1 2])
;; ;=>true
;; (= (list 1) [])
;; ;=>false
;; (= [] [1])
;; ;=>false
;; (= 0 [])
;; ;=>false
;; (= [] 0)
;; ;=>false
;; (= [] "")
;; ;=>false
;; (= "" [])
;; ;=>false
;;
;; ;; Testing vector parameter lists
;; ( (fn* [] 4) )
;; ;=>4
;; ( (fn* [f x] (f x)) (fn* [a] (+ 1 a)) 7)
;; ;=>8
;;
;; ;; Nested vector/list equality
;; (= [(list)] (list []))
;; ;=>true
;; (= [1 2 (list 3 4 [5 6])] (list 1 2 [3 4 (list 5 6)]))
;; ;=>true
