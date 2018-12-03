(ns p03.solution)

(def input (slurp "./input.txt"))

;; part 1
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

;;part 2
(def parsed-input
  (->> input
       (clojure.string/split-lines)
       (map parse-entry)))

(def unspent-fabric
  (->> parsed-input
       (mapcat entry-to-points)
       (frequencies)
       (filter #(let [[k v] %] (= 1 v)))
       (keys)
       (into #{})))

(defn entry-to-schema [entry]
  (let [[id x0 y0 deltaX deltaY] entry
        x1 (+ x0 deltaX)
        y1 (+ y0 deltaY)
        coords (for [x (range x0 x1) y (range y0 y1)] [x y])]
    {:id id :coords coords}))

(def fabric (map entry-to-schema parsed-input))

(defn intersect? [piece unspent-fabric]
  (let [coords (into #{} (:coords piece))]
    (= coords (clojure.set/intersection coords unspent-fabric))))

(def answer2
  (:id (first (filter #(intersect? % unspent-fabric) fabric))))

