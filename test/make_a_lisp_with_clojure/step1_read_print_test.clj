(ns make-a-lisp-with-clojure.step1-read-print-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.step1-read-print :as s1]))

(deftest step1-read-print
  (testing "basic string"
    (is (= (s1/read-eval-print "abcABC123") "abcABC123")))

  (testing "read of numbers"
    (is (= (s1/read-eval-print "1") "1"))
    (is (= (s1/read-eval-print "7") "7"))
    (is (= (s1/read-eval-print "-123") "-123")))

  (testing "read of symbols"
    (is (= (s1/read-eval-print "+") "+"))
    (is (= (s1/read-eval-print "abc") "abc"))
    (is (= (s1/read-eval-print "   abc   ") "abc"))
    (is (= (s1/read-eval-print "abc5") "abc5"))
    (is (= (s1/read-eval-print "abc-def") "abc-def")))

  (testing "non-numbers starting with a dash."
    (is (= (s1/read-eval-print "-") "-"))
    (is (= (s1/read-eval-print "-abc") "-abc"))
    (is (= (s1/read-eval-print "->>") "->>")))

  (testing "read of lists"
    (is (= (s1/read-eval-print "(+ 1 2)") "(+ 1 2)"))
    (is (= (s1/read-eval-print "()") "()"))
    (is (= (s1/read-eval-print "( )") "()"))
    (is (= (s1/read-eval-print "(nil)") "(nil)"))
    (is (= (s1/read-eval-print "((3 4))") "((3 4))"))
    (is (= (s1/read-eval-print "(+ 1 (+ 2 3))") "(+ 1 (+ 2 3))"))
    (is (= (s1/read-eval-print "  ( +   1   (+   2 3   )   )") "(+ 1 (+ 2 3))"))
    ;; (is (= (s1/read-eval-print "(* 1 2)") "(* 1 2 3)"))
    (is (= (s1/read-eval-print "(** 1 2)") "(** 1 2)"))
    (is (= (s1/read-eval-print "(* -3 6)") "(* -3 6)")))
    ;; (is (= (s1/read-eval-print "(()())") "(()())")))

  (testing "commas as whitespace"
    (is (= (s1/read-eval-print "(1 2, 3,,,,),,") "(1 2 3)"))))

;;   ;>>> deferrable=True
;;
;;   ;;
;;   ;; -------- Deferrable Functionality --------
;;
;;   ;; read of nil/true/false
;;   nil
;;   ;=>nil
;;   true
;;   ;=>true
;;   false
;;   ;=>false
;;
;;   ;; read of strings
;;   "abc"
;;   ;=>"abc"
;;      "abc"))
;;   ;=>"abc"
;;   "abc (with parens)"
;;   ;=>"abc (with parens)"
;;   "abc\"def"
;;   ;=>"abc\"def"
;;   ""
;;   ;=>""
;;   "\\"
;;   ;=>"\\"
;;   "\\\\\\\\\\\\\\\\\\"
;;   ;=>"\\\\\\\\\\\\\\\\\\"
;;   "&"
;;   ;=>"&"
;;   "'"
;;   ;=>"'"
;;   "("
;;   ;=>"("
;;   ")"
;;   ;=>")"
;;   "*"
;;   ;=>"*"
;;   "+"
;;   ;=>"+"
;;   ","
;;   ;=>","
;;   "-"
;;   ;=>"-"
;;   "/"
;;   ;=>"/"
;;   ":"
;;   ;=>":"
;;   ";"
;;   ;=>";"
;;   "<"
;;   ;=>"<"
;;   "="
;;   ;=>"="
;;   ">"
;;   ;=>">"
;;   "?"
;;   ;=>"?"
;;   "@"
;;   ;=>"@"
;;   "["
;;   ;=>"["
;;   "]"
;;   ;=>"]"
;;   "^"
;;   ;=>"^"
;;   "_"
;;   ;=>"_"
;;   "`"
;;   ;=>"`"
;;   "{"
;;   ;=>"{"
;;   "}"
;;   ;=>"}"
;;   "~"
;;   ;=>"~"
;;
;;   ;; reader errors
;;   (1 2
;;   ;/.*(EOF|end of input|unbalanced).*
;;   [1 2
;;   ;/.*(EOF|end of input|unbalanced).*
;;
;;   ;;; These should throw some error with no return value
;;   "abc
;;   ;/.*(EOF|end of input|unbalanced).*
;;   "
;;   ;/.*(EOF|end of input|unbalanced).*
;;   "\"
;;   ;/.*(EOF|end of input|unbalanced).*
;;   "\\\\\\\\\\\\\\\\\\\"
;;   ;/.*(EOF|end of input|unbalanced).*
;;   (1 "abc
;;   ;/.*(EOF|end of input|unbalanced).*
;;   (1 "abc"
;;   ;/.*(EOF|end of input|unbalanced).*
;;
;;   ;; read of quoting
;;   '1
;;   ;=>(quote 1)
;;   '(1 2 3)
;;   ;=>(quote (1 2 3))
;;   `1
;;   ;=>(quasiquote 1)
;;   `(1 2 3)
;;   ;=>(quasiquote (1 2 3))
;;   ~1
;;   ;=>(unquote 1)
;;   ~(1 2 3)
;;   ;=>(unquote (1 2 3))
;;   `(1 ~a 3)
;;   ;=>(quasiquote (1 (unquote a) 3))
;;   ~@(1 2 3)
;;   ;=>(splice-unquote (1 2 3))
;;
;;
;;   ;; keywords
;;   :kw
;;   ;=>:kw
;;   (:kw1 :kw2 :kw3)
;;   ;=>(:kw1 :kw2 :kw3)
;;
;;   ;; read of vectors
;;   [+ 1 2]
;;   ;=>[+ 1 2]
;;   []
;;   ;=>[]
;;   [ ]
;;   ;=>[]
;;   [[3 4]]
;;   ;=>[[3 4]]
;;   [+ 1 [+ 2 3]]
;;   ;=>[+ 1 [+ 2 3]]
;;     [ +   1   [+   2 3   ]   ]
;;   ;=>[+ 1 [+ 2 3]]
;;   ([])
;;   ;=>([])
;;
;;   ;; read of hash maps
;;   {}
;;   ;=>{}
;;   { }
;;   ;=>{}
;;   {"abc" 1}
;;   ;=>{"abc" 1}
;;   {"a" {"b" 2}}
;;   ;=>{"a" {"b" 2}}
;;   {"a" {"b" {"c" 3}}}
;;   ;=>{"a" {"b" {"c" 3}}}
;;   {  "a"  {"b"   {  "cde"     3   }  }}
;;   ;=>{"a" {"b" {"cde" 3}}}
;;   ;;; The regexp sorcery here ensures that each key goes with the correct
;;   ;;; value and that each key appears only once.
;;   {"a1" 1 "a2" 2 "a3" 3}
;;   ;/{"a([1-3])" \1 "a(?!\1)([1-3])" \2 "a(?!\1)(?!\2)([1-3])" \3}
;;   {  :a  {:b   {  :cde     3   }  }}
;;   ;=>{:a {:b {:cde 3}}}
;;   {"1" 1}
;;   ;=>{"1" 1}
;;   ({})
;;   ;=>({})
;;
;;   ;; read of comments
;;    ;; whole line comment (not an exception)
;;   1 ; comment after expression
;;   ;=>1
;;   1; comment after expression
;;   ;=>1
;;
;;   ;; read of @/deref
;;   @a
;;   ;=>(deref a)
;;
;;   ;>>> soft=True
;;   ;>>> optional=True
;;   ;;
;;   ;; -------- Optional Functionality --------
;;
;;   ;; read of ^/metadata
;;   ^{"a" 1} [1 2 3]
;;   ;=>(with-meta [1 2 3] {"a" 1})
;;
;;
;;   ;; Non alphanumerice characters in strings
;;   ;;; \t is not specified enough to be tested
;;   "\n"
;;   ;=>"\n"
;;   "#"
;;   ;=>"#"
;;   "$"
;;   ;=>"$"
;;   "%"
;;   ;=>"%"
;;   "."
;;   ;=>"."
;;   "\\"
;;   ;=>"\\"
;;   "|"
;;   ;=>"|"
;;
;;   ;; Non alphanumeric characters in comments
;;   1;!
;;   ;=>1
;;   1;"
;;   ;=>1
;;   1;#
;;   ;=>1
;;   1;$
;;   ;=>1
;;   1;%
;;   ;=>1
;;   1;'
;;   ;=>1
;;   1;\
;;   ;=>1
;;   1;\\
;;   ;=>1
;;   1;\\\
;;   ;=>1
;;   1;`
;;   ;=>1
;;   ;;; Hopefully less problematic characters
;;   1; &()*+,-./:;<=>?@[]^_{|}~
;;
;;   ;; FIXME: These tests have no reasons to be optional, but...
;;   ;; fantom fails this one
;;   "!"
;;   ;=>"!"
