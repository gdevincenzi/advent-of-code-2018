(ns p09.zipper
  (:require [clojure.zip :as zip]))

(defn rem-left [circle]
  (count (zip/lefts circle)))

(defn rem-right [circle]
  (count (zip/rights circle)))

(defn next-rot [zipper]
  (if (= 0 (rem-right zipper))
    (zip/leftmost zipper)
    (zip/next zipper)))

(defn prev-rot [zipper]
  (if (= 0 (rem-left zipper))
    (zip/rightmost zipper)
    (zip/prev zipper)))

(defn add-marble [circle marble]
  (-> circle
      (next-rot)
      (zip/insert-right marble)
      (next-rot)))

(defn rem-marble [circle marble]
  (let [c (nth (iterate prev-rot circle) 7)]
    (-> c
        (zip/remove)
        next-rot)))

(defn run-game [players limit]
  (loop [circle (zip/next (zip/vector-zip [0]))
         marble 1
         limit limit
         players players
         score {}]
    (cond
      (= marble limit) (apply max (vals score))
      (= 0 (mod marble 23))
         (let [c (nth (iterate prev-rot circle) 7)
               removed (zip/node c)
               player (rem marble players)
               newscore (+ removed marble (get score player 0))]
           (-> c
               (zip/remove)
               (next-rot)
               (recur ,, (inc marble) limit players (assoc score player newscore))))
         :else
         (-> circle
             (add-marble ,, marble)
             (recur (inc marble) limit players score)))))

