(ns p11.solution)

(def input (read-string (slurp "./input.txt")))

(def gridsize 300)

;; PART 1
(defn powerlevel [x y serial]
  (let [id (+ x 10)]
    (-> id
        (* ,, y)
        (+ ,, serial)
        (* ,, id)
        (quot ,, 100)
        (mod ,, 10)
        (- ,, 5))))

(def grid (for [y (range 1 (inc gridsize)) x (range 1 (inc gridsize))] (powerlevel x y input)))

(def vectorgrid
  (->> grid
       (partition gridsize)
       (map vec)
       (vec)))

(defn squarepower [x y]
  (reduce + (for [y (range (dec y) (+ 2 y)) x (range (dec x) (+ 2 x))] (get-in vectorgrid [y x]))))

(def answer1
  (let [powers (for [y (range 1 (- gridsize 2)) x (range 1 (- gridsize 2))] {[x y] (squarepower x y)})]
    (->> powers
         (reduce conj {})
         (sort-by val >)
         (first)
         (key))))

;; PART 2
(def sum-node
  (memoize (fn [x y]
             (cond
               (= x y 0) (get-in vectorgrid [y x])
               (< x 0) 0
               (< y 0) 0
               :else (-> (get-in vectorgrid [y x])
                         (+ ,, (sum-node x (dec y)))
                         (+ ,, (sum-node (dec x) y))
                         (- ,, (sum-node (dec x) (dec y))))))))

(def summed-area-table
  (let [areas (for [y (range 0 gridsize) x (range 0 gridsize)] (sum-node x y))]
    (->> areas
         (partition gridsize)
         (map vec)
         (vec))))

(defn squarepower [x y size]
  (let [s (dec size)
        x0 (- x 2)
        y0 (- y 2)
        x1 (+ x0 size)
        y1 (+ y0 size)]
    (-> (get-in summed-area-table [y1 x1] 0)
        (+ ,, (get-in summed-area-table [y0 x0] 0))
        (- ,, (get-in summed-area-table [y1 x0] 0))
        (- ,, (get-in summed-area-table [y0 x1] 0)))))

(defn power-reduce [[x y size power] [nx ny nsize]]
  (let [npower (squarepower nx ny nsize)]
    (if (> npower power)
      [nx ny nsize npower]
      [x y size power])))

(def answer2 
  (let [gs (inc gridsize)
        squares (for [x (range 1 gs)
                      y (range 1 gs)
                      s (range 1 (- gs (max x y)))]
                  [x y s])]
    (reduce #(power-reduce %1 %2) [0 0 0 0] squares)))
