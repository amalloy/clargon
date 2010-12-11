(ns clargon.example
  (:use clargon.core)
  (:gen-class))

(defn -main [& args]
  (let [cla
        (clargon args
                 (required ["-p" "--port" "the port for the server"] #(Integer. %))
                 (optional ["--host" "hostname of server" :default "localhost"])
                 (optional ["--verbose" "run in chatty mode" :default true])
                 (optional ["--log-directory" "where you put logs" :default "/some/path"]))]
    (println cla)))


