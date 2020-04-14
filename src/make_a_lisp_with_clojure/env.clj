(ns make-a-lisp-with-clojure.env)

(comment "

env shape:
{
  :outer nil
  :data {}
}

set:
  [symbol-key value] (should also take an env)
  adds symbol-key to data structure

find-env-with-key
  [key env]
  searches for key in current env
    if key exists in the env, returns the env
    if it does not exist, and `outer` is not nil, recurs with [key outer]
    if it does not exist, and `outer` is nil, returns nil

retrieve-symbol
  [symbol env]
  calls (find-env-with-key symbol env)
    if nil, throws an error
")

(defn set-value-in-env
  [env key value]
  (assoc-in env [:data key] value))

(defn find-env-with-key
  "starts with an env, searching recursively through outer envs until it runs out"
  [env key]
  (let [{:keys [data]} env]
    (cond
      (nil? env) nil
      (contains? data key) env
      (not (nil? (:outer env))) (recur (:outer env) key)
      :else nil)))

(defn retrieve-symbol
  [env symbol]
  (let [env-with-key (find-env-with-key env symbol)]
    (cond
      (nil? env-with-key) (throw (Exception. (str "unable to find symbol " symbol)))
      :else (get-in env-with-key [:data symbol]))))
