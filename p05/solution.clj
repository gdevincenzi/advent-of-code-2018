(ns p05.solution)

(def input
  (slurp "./input.txt"))

(defn reacts? [a b]
  (= (Math/abs (compare a b)) 32))

(def testinput "dabAcCaCBAcCcaDA")

(defn reduce-polimer [polimer]
  (reduce
   (fn [acc cur]
     (if (reacts? (first acc) cur)
       (rest acc)
       (conj acc cur))) '() polimer))
