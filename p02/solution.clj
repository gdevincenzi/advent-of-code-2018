(ns p02.solution)

(def data (clojure.string/split-lines (slurp "./input.txt")))

;; part 1
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

(def answer1 (checksum data))

;; part 2
(defn strcomparemap [string1 string2]
  (map #(not (= %1 %2)) string1 string2))

(defn strfilter [string1 string2]
  (= 1 (reduce + (convert (strcomparemap string1 string2)))))

(defn find-values [data]
  (for [x data y data :when (strfilter x y)] x))

(defn extract-answer [data]
  (let [[v1 v2] (find-values data)]
    (apply str (remove nil? (map #(if (= %1 %2) %1) v1 v2)))))

(def answer2 (extract-anwser data))
