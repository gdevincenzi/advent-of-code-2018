(ns p09.solution)

(defn rotate-ccw [circle]
  (let [{left :left
         current :current
         right :right} circle]
    (if (empty? left)
      (let [[cur & l] (reverse (cons current right))]
        (assoc circle
               :left l
               :current cur
               :right '()))
      (assoc circle
             :left (rest left)
             :current (first left)
             :right (cons current right)))))

(defn add-marble [circle marble]
  (let [{left :left
         current :current
         right :right} circle]
    (if (empty? right)
      (let [[l & r] (reverse (cons current left))]
        (assoc circle
               :left (list l)
               :current marble
               :right r))
      (assoc circle
             :left (cons (first right) (cons current left))
             :current marble
             :right (rest right)))))

(defn run-game [players limit]
  (let [score (atom (zipmap (range 1 (inc players)) (repeat 0)))]
    (do
      (reduce (fn [circle marble]
                (if (= 0 (mod marble 23))
                  (let [c (nth (iterate rotate-ccw circle) 7)
                        removed (:current c)
                        player (inc (rem marble players))]
                    (do
                      (swap! score update-in [player] + removed marble)
                      (assoc c
                             :current (first (:right c))
                             :right (rest (:right c)))))
                  (add-marble circle marble)))
              {:left '() :current 0 :right '()} (range 1 limit))
      (apply max (vals @score)))))
