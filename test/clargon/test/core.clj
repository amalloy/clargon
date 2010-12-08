(ns clargon.test.core
  (:use [clargon.core] :reload)
  (:use [clojure.test]))

(deftest boolean-flags
  (is (= [:verbose true] (option* (parse-args '("-v")) ["-v" "--verbose" :default true])))
  (is (= [:verbose false] (option* (parse-args '("--no-verbose")) ["--verbose"])))
  (is (= [:verbose true] (option* (parse-args '()) ["--verbose" :default true]))))

(deftest with-parse-fn
  (is (= [:port 8080] (option* (parse-args '("-p" "8080")) ["-p" "--port"] #(Integer. %)))))

(deftest with-required
  (is (thrown-with-msg? Exception #"host is a required parameter"
        (required* (parse-args '()) ["-h" "--host"]))))

(deftest syntax
  (is (= {:port 8080
          :host "localhost"
          :verbose false
          :log-directory "/tmp"}
         (clargon '("-p" "8080" "--no-verbose" "--log-directory" "/tmp")
                  (required ["-p" "--port"] #(Integer. %))
                  (option ["-h" "--host" :default "localhost"])
                  (option ["--verbose" :default true])
                  (option ["--log-directory" :default "/some/path"])))))
