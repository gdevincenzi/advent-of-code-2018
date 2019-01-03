(ns p12.solution
  (:require [clojure.zip :as zip
             clojuse.string :as s]))

(def input (slurp "./input.txt"))

(def initial-state
  (->> input
       (s/split-lines)
       (first)
       (drop 15)
       (apply str)))

(defn parse-rule [rule]
  (let [[k v] (drop 1 (re-find #"([.|#]{5}) => ([.|#]{1})" rule))]
    {k v}))

(def rules
  (->> input
       (s/split-lines)
       (drop 2)
       (map parse-rule)
       (reduce conj {})))

(defn apply-rule [[_ _ c _ _ :as window]]
  (get rules (apply str window) (str c)))

(defn iterate-growth [state]
  (let [padded (str "..." state "...")]
    (->> padded
         (partition 5 1)
         (map apply-rule)
         (apply str))))

(def total-for-nth
  (memoize
   (fn [n]
     (let [final-state (nth (iterate iterate-growth initial-state) n)]
       (->> final-state
            (zipmap (range (- 0 n) (count final-state)))
            (filter #(= (second %) \#))
            (keys)
            (reduce +))))))

(def answer1 (total-for-nth 20))

(defn extrapolate [n]
  (let [diff (- (total-for-nth (inc n)) (total-for-nth n))
        next-diff (- (total-for-nth (+ 2 n)) (total-for-nth (inc n)))]
    (if (= diff next-diff)
      [n diff (total-for-nth n)]
      (recur (inc n)))))

(def answer2
  (let [[n diff count] (extrapolate 1)
        remn (- 50000000000 n)]
    (+ count (* diff remn))))


