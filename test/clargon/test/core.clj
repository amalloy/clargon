(ns clargon.test.core
  (:use [clargon.core] :reload)
  (:use [clojure.test]))

(deftest syntax
  (is (= {:port 8080
          :host "localhost"
          :verbose false
          :log-directory "/tmp"}
         (clargon '("-p" "8080" "--no-verbose" "--log-directory" "/tmp")
                  (required ["-p" "--port"] #(Integer. %))
                  (optional ["--host" :default "localhost"])
                  (optional ["--verbose" :default true])
                  (optional ["--log-directory" :default "/some/path"])))))
