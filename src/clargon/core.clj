(ns clargon.core
  (:use [clojure.contrib.str-utils :only (re-sub)]
        [clojure.contrib.pprint :only (cl-format)]))

;; help message stuff

(defn build-doc [{:keys [switches docs options]}]
  [(apply str (interpose ", " switches))
   (or docs "")
   (or (str (options :default)) "")
   (if (options :required) "Yes" "No")])

(defn show-help [specs]
  (println "Usage:")
  (println)
  (let [docs (into (map build-doc specs)
                   [["--------" "----" "-------" "--------"]
                    ["Switches" "Desc" "Default" "Required"]])
        sizes (for [d docs] (map count d))
        max-cols (map #(apply max %)
                      (apply map (fn [& c] (apply vector c)) sizes))
        vs (for [d docs]
             (mapcat (fn [& x] (apply vector x)) max-cols d))]
    (doseq [v vs]
      (cl-format true "~{ ~vA  ~vA  ~vA  ~vA ~}" v)
      (prn))))

;; option parsing

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
             (filter (fn [[k v]] (opt? k))
                     (partition-all 2 1 args)))))

(defn optional* [args params & [parse-fn]]
  (let [parse-fn (or parse-fn (fn [v] v))
        options (apply hash-map (drop-while string? params))
        switches (take-while #(and (string? %) (opt? %)) params)
        docs (first (filter #(and (string? %) (not (opt? %))) params))
        aliases (map name-for switches)
        name (or (options :name) (last aliases))]
    {:parse-fn parse-fn
     :options options
     :aliases aliases
     :switches switches
     :docs docs
     :name name}))

(defn required* [args params & [parse-fn]]
  (optional* args (into params [:required true]) parse-fn))

(defn print-and-fail [msg]
  (println msg)
  (System/exit 1))

(defn help-and-quit [specs]
  (show-help specs)
  (System/exit 0))

(defn parse-specs [{:keys [parse-fn aliases options name]} args]
  (let [raw (->> (map #(args %) aliases)
                 (remove nil?)
                 (first))
        raw (if (nil? raw)
              (:default options)
              raw)]
    (if (and (nil? raw)
             (:required options))
      (print-and-fail (str name " is a required parameter"))
      (try
        [(keyword name) (parse-fn raw)]
        (catch Exception _
          (print-and-fail (str "could not parse " name " with value of " raw)))))))

(defmacro clargon [args & specs]
  `(let [args# (parse-args ~args)
         ~'optional (partial optional* args#)
         ~'required (partial required* args#)
         specs# (do [~@specs])]
     (if (some #(or (= "-h" %) (= "--help" %)) ~args)
       (help-and-quit specs#)
       (into {} (map #(parse-specs % args#) specs#)))))
