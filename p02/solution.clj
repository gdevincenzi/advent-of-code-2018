(ns p02.solution)

(def data (clojure.string/split-lines (slurp "./input.txt")))

(defn someX [x data] (some #(= % x) (vals (frequencies data))))

(defn checkX [x data] (map #(someX x %) data))

(defn convert [data] (map #(if (true? %) 1 0) data))

(defn countX [x data] (->> data
                         (checkX x)
                         (convert)
                         (reduce +)))

(defn count2 [data] (countX 2 data))

(defn count3 [data] (countX 3 data))

(defn checksum [data] (* (count2 data) (count3 data)))

(def answer (checksum data))
