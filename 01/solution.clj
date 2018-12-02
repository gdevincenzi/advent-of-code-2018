(ns 01.solution)

(def input (slurp "./input.txt"))

(def data
  (->> input
       (clojure.string/split-lines)
       (map read-string)))

(def answer1 (reduce + data))
