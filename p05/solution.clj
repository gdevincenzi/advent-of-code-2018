(ns p05.solution)

(def input (clojure.string/trim-newline (slurp "./input.txt")))

(defn reacts? [a b]
  (= (Math/abs (compare a b)) 32))

(defn reduce-polimer [polimer]
  (reduce
   (fn [acc cur]
     (if (reacts? (first acc) cur)
       (rest acc)
       (conj acc cur))) '() polimer))

(def answer1
  (->> input
       (reduce-polimer)
       (count)))
