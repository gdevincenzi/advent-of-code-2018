(ns p11.solution)

(def input (read-string (slurp "./input.txt")))

(defn powerlevel [x y serial]
  (let [id (+ x 10)
        hundreth (quot (* id (+ serial (* y id))) 100)]
   (- (read-string (str (first (reverse (str hundreth))))) 5)))

(def grid (for [x (range 1 301) y (range 1 301)] (powerlevel x y input)))

