(ns clargon.test.core
  (:use [clargon.core] :reload)
  (:use [clojure.test]))

(deftest boolean-flags
  (let [args (option* ["-v"] ["-v" "--verbose" :default true])]
    (is (= [:verbose true]  args))))

;; (let [args (clargonz 
;;             (option ["-n" "--name NAME" "the name"])
;;             (option ["-v" "--verbose"])
;;             (parameter ["-p" "--port" "the port of server"] #(Integer. %)))])
