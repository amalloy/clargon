(ns clargon.core
  (:use [clojure.contrib.str-utils :only (re-sub)]))

(defn name-for [k]
  (re-sub #"--no-|--|-" "" k))

(defn flag-for [v]
  (not (.startsWith v "--no-")))

(defn opt? [x]
  (.startsWith x "-"))

(defn parse-args [args]
  (into {}
        (map (fn [[k v]]
               (if (and (opt? k) (or (nil? v) (opt? v)))
                 [(name-for k) (flag-for k)]
                 [(name-for k) v]))
             (filter (fn [[k v]] (and (opt? k)))
                     (partition-all 2 1 args)))))

(defn option* [args params & [parse-fn]]
  (let [parse-fn (or parse-fn (fn [v] v))
        options (apply hash-map (drop-while string? params))
        aliases (map name-for (take-while string? params))
        name (or (options :name) (last aliases))
        value (->> (map (fn [a] [a (args a)]) aliases)
                   (remove (fn [[_ v]] (nil? v)))
                   (first))
        v (if (nil? value) (:default options) (last value))]
    (if (and (options :required)
             (nil? v))
      (throw (Exception. (str name " is a required parameter"))))
    [(keyword name) (parse-fn v)]))

(defn required* [args params & [parse-fn]]
  (option* args (into params [:required true]) parse-fn))

(defmacro clargon [args & specs]
  `(let [args# (parse-args ~args)
         ~'option (partial option* args#)
         ~'required (partial required* args#)]
     (reduce merge {} (do [~@specs]))))

