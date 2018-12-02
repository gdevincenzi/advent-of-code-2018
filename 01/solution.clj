(ns 01.solution)

(def input (slurp "./input.txt"))

(def data
  (->> input
       (clojure.string/split-lines)
       (map read-string)))

(def answer1 (reduce + data))

(defn find-duplicate [acc data]
  (let [cur (first data)]
    (if (contains? acc cur)
      cur
      (recur (conj acc cur) (rest data)))))

(def answer2 (find-duplicate #{} (reductions + (cycle data))))
