(ns p05.solution
  (:require
   [clojure.string :as str]))

(def input (str/trim-newline (slurp "./input.txt")))

(defn reacts? [a b]
  (= (Math/abs (compare a b)) 32))

(defn reduce-polymer [polymer]
  (reduce
   (fn [acc cur]
     (if (reacts? (first acc) cur)
       (rest acc)
       (conj acc cur))) '() polymer))

(def answer1
  (->> input
       (reduce-polymer)
       (count)))

(defn regbuilder [type]
  (re-pattern (str type "|" (str/upper-case type))))

(def polymer-types (map char (range 97 123)))

(defn remove-polymer-type [polymer type]
  (str/replace polymer (regbuilder type) ""))

(defn possible-polymers [polymer]
  (map #(remove-polymer-type polymer %) polymer-types))

(def answer2
  (->> input
       (possible-polymers)
       (map reduce-polymer)
       (map count)
       (apply min)))
