(ns p02.solution)

(def data (clojure.string/split-lines (slurp "./input.txt")))

;; part 1
(defn someX [x data] (some #(= % x) (vals (frequencies data))))

(defn checkX [x data] (map #(someX x %) data))

(defn countX [x data] (->> data
                         (checkX x)
                         (filter true?)
                         (count)))

(defn count2 [data] (countX 2 data))

(defn count3 [data] (countX 3 data))

(defn checksum [data] (* (count2 data) (count3 data)))

(def answer1 (checksum data))

;; part 2
(defn strcomparemap [string1 string2]
  (map #(= %1 %2) string1 string2))

(defn strfilter [string1 string2]
  (->> string2
       (strcomparemap string1)
       (filter false?)
       (count)
       (= 1)))

(defn find-values [data]
  (for [x data y data :when (strfilter x y)] x))

(defn extract-answer [data]
  (let [[v1 v2] (find-values data)]
    (apply str (remove nil? (map #(if (= %1 %2) %1) v1 v2)))))

(def answer2 (extract-answer data))
