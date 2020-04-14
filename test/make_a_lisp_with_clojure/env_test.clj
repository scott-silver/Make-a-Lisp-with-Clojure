(ns make-a-lisp-with-clojure.env-test
  (:require [clojure.test :refer :all]
            [make-a-lisp-with-clojure.env :as env]))

(deftest set-value-in-env-test
  (testing "returns a new env with new value defined in :data"
    (is (= {:outer nil :data {:a 1 :b 2}}
           (env/set-value-in-env {:outer nil :data {:a 1}} :b 2))))

  (testing "maintains reference to :outer"
    (let [outer-env {:a 1}]
      (is (= {:outer outer-env :data {:b 2 :c 3}}
             (env/set-value-in-env {:outer outer-env :data {:b 2}} :c 3))))))

(deftest find-env-with-key
  (testing "returns the first env with a matching key"
    (let [outer-env {:outer nil :data {:a 2}}
          inner-env {:outer outer-env :data {:a 1}}]
      (is (= inner-env (env/find-env-with-key inner-env :a)))))

  (testing "searches through each outer env until it finds a match"
    (let [outer-env {:outer nil :data {:c 3}}
          middle-env {:outer outer-env :data {:b 2}}
          inner-env {:outer middle-env :data {:a 1}}]
      (is (= outer-env (env/find-env-with-key inner-env :c)))))

  (testing "stops at the first match"
    (let [outer-env {:outer nil :data {:b 3}}
          middle-env {:outer outer-env :data {:b 2}}
          inner-env {:outer middle-env :data {:a 1}}]
      (is (= middle-env (env/find-env-with-key inner-env :b)))))

  (testing "returns nil if no match is found"
    (let [outer-env {:outer nil :data {:c 3}}
          middle-env {:outer outer-env :data {:b 2}}
          inner-env {:outer middle-env :data {:a 1}}]
      (is (= nil (env/find-env-with-key inner-env :d))))))

(deftest retrieve-symbol
  (testing "returns the first matching value"
    (let [outer-env {:outer nil :data {:a 2}}
          inner-env {:outer outer-env :data {:a 1}}]
      (is (= 1 (env/retrieve-symbol inner-env :a)))))

  (testing "searches until it finds a match"
    (let [outer-env {:outer nil :data {:c 3}}
          middle-env {:outer outer-env :data {:b 2}}
          inner-env {:outer middle-env :data {:a 1}}]
      (is (= 3 (env/retrieve-symbol inner-env :c)))))

  (testing "throws if no match is found"
    (let [outer-env {:outer nil :data {:c 3}}
          middle-env {:outer outer-env :data {:b 2}}
          inner-env {:outer middle-env :data {:a 1}}]
      (is (thrown-with-msg? Exception #"unable to find symbol :d"
                            (env/retrieve-symbol inner-env :d))))))
