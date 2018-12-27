(ns p10.solution)

(def input (slurp "./input.txt"))

(defn point [input]
  (let [[x y dX dY] (map read-string input)]
  {:x x :y y :dX dX :dY dY}))

(def data
  (->> input
       (clojure.string/split-lines)
       (map #(re-seq #"-?\d+" %))
       (map point)))

(defn boundaries [points]
  (let [xs (map :x points)
        ys (map :y points)]
    {:min-x (apply min xs)
     :max-x (apply max xs)
     :min-y (apply min ys)
     :max-y (apply max ys)}))

(defn height [points]
  (let [bounds (boundaries points)
        {y0 :min-y
         y1 :max-y} bounds]
    (- y1 y0)))

(defn move-point [{:keys [x y dX dY]}]
  {:x (+ x dX) :y (+ y dY) :dX dX :dY dY})

(defn move-all-points [points]
  (map move-point points))

(defn plot [points]
  (let [bounds (boundaries points)
        x0 (:min-x bounds)
        x1 (inc (:max-x bounds))
        y0 (:min-y bounds)
        y1 (inc (:max-y bounds))
        pset (into #{} (map #(list (:x %) (:y %)) points))]
    (apply str (for [y (range y0 y1) x (range x0 x1)]
      (let [point (if (pset [x y]) "#" ".")]
        (if (= x (dec x1)) (str point "\n") point))))))

(defn converge [points]
  (if (> (height (second points)) (height (first points)))
    (first points)
    (recur (rest points))))

(defn answer1 []
  (print (plot (converge (iterate move-all-points data)))))

(defn time-to-converge
  ([points] (time-to-converge points 0))
  ([points time]
   (if (> (height (second points)) (height (first points)))
     time
     (recur (rest points) (inc time)))))

(def answer2 
  (time-to-converge (iterate move-all-points data)))
