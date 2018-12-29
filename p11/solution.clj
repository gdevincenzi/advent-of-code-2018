(ns p11.solution)

(def input (read-string (slurp "./input.txt")))

(def gridsize 300)

(defn powerlevel [x y serial]
  (let [id (+ x 10)]
    (-> id
        (* ,, y)
        (+ ,, serial)
        (* ,, id)
        (quot ,, 100)
        (mod ,, 10)
        (- ,, 5))))

(def grid (for [x (range 1 (inc gridsize)) y (range 1 (inc gridsize))] (powerlevel x y input)))

(def vectorgrid
  (->> grid
       (partition gridsize)
       (map vec)
       (vec)))

(defn squarepower [x y]
  (reduce + (for [x (range (dec x) (+ 2 x)) y (range (dec y) (+ 2 y))] (get-in vectorgrid [x y]))))

(def answer1
  (let [powers (for [x (range 1 (- gridsize 2)) y (range 1 (- gridsize 2))] {[x y] (squarepower x y)})]
    (->> powers
         (reduce conj {})
         (sort-by val >)
         (first)
         (key))))
