(ns clargon.example
  (:use clargon.core)
  (:gen-class))

(defn -main [& args]
  (let [cla
        (clargon args
                 (required ["-p" "--port" "the server port"] #(Integer. %))
                 (required ["--host" "the server hostname"])
                 (optional ["-v" "--verbose" "output lots of logging" :default false])
                 (optional ["--debug" "run in debug mode" :default false]))]
    (println cla)))


