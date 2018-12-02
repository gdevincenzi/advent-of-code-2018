(ns 01.solution)

(def input (slurp "./input.txt"))

(->> input
     (clojure.string/split-lines)
     (map read-string)
     (reduce +))
