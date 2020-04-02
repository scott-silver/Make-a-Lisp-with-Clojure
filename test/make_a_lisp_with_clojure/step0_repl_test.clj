(ns make-a-lisp-with-clojure.step0-repl-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.step0-repl :as step0-repl]))

(deftest step0-test
  (testing "basic string"
    (is (= (step0-repl/read-eval-print "abcABC123") "abcABC123")))

  (testing "string containing spaces"
    (is (= (step0-repl/read-eval-print "hello mal world") "hello mal world")))

  (testing "string containing symbols"
    (is (= (step0-repl/read-eval-print "[]{}\"'* ;:()") "[]{}\"'* ;:()")))

  (testing "long string"
    (let [long-string "hello world abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789 (;:() []{}\"'*]) ;:() []{}\"'* ;:() []{}\"'*)"]
      (is (= (step0-repl/read-eval-print long-string) long-string))))

  (testing "non-alphanumeric characters"
    (is (= (step0-repl/read-eval-print "!") "!"))
    (is (= (step0-repl/read-eval-print "&") "&"))
    (is (= (step0-repl/read-eval-print "+") "+"))
    (is (= (step0-repl/read-eval-print ",") ","))
    (is (= (step0-repl/read-eval-print "-") "-"))
    (is (= (step0-repl/read-eval-print "/") "/"))
    (is (= (step0-repl/read-eval-print "<") "<"))
    (is (= (step0-repl/read-eval-print "=") "="))
    (is (= (step0-repl/read-eval-print ">>") ">>"))
    (is (= (step0-repl/read-eval-print "?") "?"))
    (is (= (step0-repl/read-eval-print "@") "@"))
    (is (= (step0-repl/read-eval-print "^") "^"))
    (is (= (step0-repl/read-eval-print "_") "_"))
    (is (= (step0-repl/read-eval-print "`") "`"))
    (is (= (step0-repl/read-eval-print "~") "~"))
    (is (= (step0-repl/read-eval-print "#") "#"))
    (is (= (step0-repl/read-eval-print "$") "$"))
    (is (= (step0-repl/read-eval-print "%") "%"))
    (is (= (step0-repl/read-eval-print ".") "."))
    (is (= (step0-repl/read-eval-print "|") "|"))))
