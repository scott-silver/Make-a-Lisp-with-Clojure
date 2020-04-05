(defproject make-a-lisp-with-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot make-a-lisp-with-clojure.core
  :target-path "target/%s"
  :profiles {:step0 {:main make-a-lisp-with-clojure.step0-repl
                     :uberjar-name "step0_repl.jar"
                     :aot [make-a-lisp-with-clojure.step0-repl]}
             :step1 {:main make-a-lisp-with-clojure.step1-read-print
                     :uberjar-name "step1_read_print.jar"
                     :aot [make-a-lisp-with-clojure.step1-read-print]}})
