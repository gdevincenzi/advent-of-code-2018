(ns p07.solution
  (:require
   [clojure.string :as str]))

(def input
  (str/split-lines (slurp "./input.txt")))

(def re #".*([A-Z]).*([A-Z])")

(defn parse-entry [entry]
  (let [[_ instruction requirement] (re-find re entry)]
  (list instruction requirement)))

(defn available-keys [steps]
  (let [instructions (map #(first %) steps)
        requirements (map #(second %) steps)]
    (reduce
     (fn [acc cur]
       (if (some #{cur} requirements)
         acc
         (conj acc cur))) '() instructions)))

(defn next-key [steps]
  (first (sort (available-keys steps))))

(defn extract-steps
  ([steps] (solve1 steps '()))
  ([steps output]
   (let [nxt (first steps)
         rst (rest steps)
         next-key (next-key steps)]
     (if (empty? rst)
       (concat output (first nxt) (second nxt))
       (recur (remove #(= (first %) next-key) steps) (concat output next-key))))))

(def answer1
  (->> input
       (map parse-entry)
       (extract-steps)
       (apply str)))

