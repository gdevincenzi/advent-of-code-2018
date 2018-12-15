(ns p08.solution)

(def input (slurp "./input.txt"))

(def testinput [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])

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
