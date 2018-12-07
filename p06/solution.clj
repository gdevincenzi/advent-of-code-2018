(ns p06.solution
  (:require [clojure.string :as str]))

(def testinput
  (str/split-lines (slurp "./testinput.txt")))

(def input (str/split-lines (slurp "./input.txt")))

(defn to-point [entry]
  (map read-string (str/split entry #",")))

(defn manhattan [coordA coordB]
  (let [xA (first coordA)
        xB (first coordB)
        yA (second coordA)
        yB (second coordB)]
    (+ (Math/abs (- xA xB)) (Math/abs (- yA yB)))))

(defn closest-to [point points]
  (let [distmap (reduce into {} (map #(hash-map %1 (manhattan %1 point)) points))
        distances (filter #(= (apply min (vals distmap)) (second %)) distmap)]
    (if (> (count distances) 1) nil (first (first distances)))))

(defn outer-bounds [points]
  (let [xs (map first points)
        ys (map second points)]
    {:x0 (apply min xs) :x1 (apply max xs) :y0 (apply min ys) :y1 (apply max ys)}))

(defn is-outer-bound? [point points]
  (let [{x0 :x0
         x1 :x1
         y0 :y0
         y1 :y1} (outer-bounds points)]
    (or
      (= (first point) x0)
      (= (first point) x1)
      (= (second point) y0)
      (= (second point) y1))))

(defn makegrid [points]
  (let [{x0 :x0
         x1 :x1
         y0 :y0
         y1 :y1} (outer-bounds points)]
    (for [x (range x0 x1) y (range y0 y1)] [x y])))

(defn map-grid-to-points [points]
  (map #(closest-to % points) (makegrid points)))

(def answer1
  (let [points (->> input (map to-point))]
    (->> points
         (map-grid-to-points)
         (remove nil?)
         (remove #(is-outer-bound? % points))
         (frequencies)
         (sort-by val >)
         (first)
         (second))))
