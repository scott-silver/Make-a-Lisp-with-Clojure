(ns make-a-lisp-with-clojure.printer
  (:require [clojure.string :as str]))

(defmulti print-data-structure first)

(defmethod print-data-structure :integer
  [integer-structure]
  (second integer-structure))

(defmethod print-data-structure :symbol
  [string-structure]
  (second string-structure))

(defmethod print-data-structure :list
  [list-structure]
  (let [list-contents (second list-structure)
        list-contents-strings (map print-data-structure list-contents)]
    (str "(" (str/join " " list-contents-strings) ")")))

(defn print-data-structures
  [data-structure-list]
  (loop [list data-structure-list
         accumulated-string ""]
    (let [first-data-structure (first list)]
      (cond
        (empty? list) (clojure.string/trim accumulated-string)
        :else
        (let [next-string (print-data-structure first-data-structure)]
          (recur (rest list) (str accumulated-string " " next-string)))))))
