(ns p08.solution)

(def input (slurp "./input.txt"))

(def data (map read-string (re-seq #"\d+" input)))

(defn parsenodes
  ([data]
   (let [[c m & rst] data]
     (if (zero? c)
       (let [[nodemeta remainder] (split-at m rst)]
         [nodemeta remainder])
       (let [[children remain] (parsenodes c rst)
             [meta remainder] (split-at m remain)]
         [[meta children] remainder]))))
  ([n data] (parsenodes n data []))
  ([n data acc]
   (if (zero? n)
     [acc data]
     (let [[brother remainder] (parsenodes data)]
       (recur (dec n) remainder (conj acc brother ))))))

(defn solve1 [data]
  (reduce + (flatten (parsenodes data))))

(def answer1 (solve1 data))

(defn parsenodesdetail
  ([data]
   (let [[c m & rst] data]
     (if (zero? c)
       (let [[nodemeta remainder] (split-at m rst)]
         [{:meta nodemeta :value (reduce + nodemeta)} remainder])
       (let [[children remain] (parsenodesdetail c rst)
             [meta remainder] (split-at m remain)
             nodevalue (reduce + (map #(:value (nth children (dec %) {:value 0})) meta ))]
         [{:meta meta :children children :value nodevalue} remainder]))))
  ([n data] (parsenodesdetail n data []))
  ([n data acc]
   (if (zero? n)
     [acc data]
     (let [[brother remainder] (parsenodesdetail data)]
       (recur (dec n) remainder (conj acc brother ))))))

(defn solve2 [data]
  (:value (first (parsenodesdetail data))))

(def answer2 (solve2 data))
