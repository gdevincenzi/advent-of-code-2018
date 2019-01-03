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

(def answer1
  (let [final-state (nth (iterate iterate-growth initial-state) 20)]
    (->> final-state
         (zipmap (range -20 (count final-state)))
         (filter #(= (second %) \#))
         (keys)
         (reduce +))))

