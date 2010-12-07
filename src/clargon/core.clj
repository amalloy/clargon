(ns clargon.core
  (:use [clojure.contrib.str-utils :only (re-sub)])
  (:gen-class))

(defn -main [& args]
  (println args))

(defn name-for [k]
  (re-sub #"--no-|--|-" "" k))

(defn flag-for [v]
  (not (.startsWith v "--no-")))

(defn opt? [x] (.startsWith x "-"))

(defn parse-args [args]
  (into {}
        (map (fn [[k v]]
               (if (and (opt? k) (or (nil? v) (opt? v)))
                 [k (flag-for k)]
                 [k v]))
             (filter (fn [[k v]] (and (opt? k)))
                     (partition-all 2 1 args)))))

(parse-args '("-v" "--port" "8080" "--foo" "bar" "--no-quiet"))

(defn parse-args [args]
  (loop [args args
         result []]
    (if-not (seq args)
      (into {} result)
      (let [[k v] args]
        (if (or (and v (.startsWith v "-"))
                (not v))
          (recur (next args) (conj result [(name-for k) (flag-for k)]))
          (recur (nnext args) (conj result [(name-for k) v])))))))

(defn option* [args params & [parse-fn]]
  (let [aliases (map name-for (take-while string? params))
        options (apply hash-map (remove string? params))
        parse-fn (or parse-fn (fn [v] v))
        value (map (fn [a] [a (args a)]) aliases)]
    value))

(defn parameter* [args params & [parse-fn]]
  (option* args (into params [:required true]) parse-fn))


(option* (parse-args '("--no-v")) ["-v" "--verbose" :default true])
;; (option* [] ["-p" "--port PORT" :default 8080] #(Integer. %))
;; (parameter* [] )








