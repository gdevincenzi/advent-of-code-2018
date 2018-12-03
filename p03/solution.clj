(ns p03.solution)

(def input (slurp "./input.txt"))

(def input (slurp "./testinput.txt"))

(defn parse-entry [entry]
  (map read-string (filter #(not (empty? %)) (clojure.string/split entry #"\D"))))

(defn entry-to-points [entry]
  (let [[id x0 y0 deltaX deltaY] entry
        x1 (+ x0 deltaX)
        y1 (+ y0 deltaY)]
    (for [x (range x0 x1) y (range y0 y1)] [x y])))

(def answer1
  (->> input
       (clojure.string/split-lines)
       (map parse-entry)
       (mapcat entry-to-points)
       (frequencies)
       (vals)
       (filter #(> % 1))
       (count)))
