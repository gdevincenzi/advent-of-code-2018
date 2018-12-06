(ns p04.solution)

(def data (slurp "./input.txt"))

;(def data (slurp "./testinput.txt"))

;;part 1
(def re #"\[(\d+)-(\d+)-(\d+) (\d+):(\d+)\] (\w+) #?(\d+)?")

(defn parse-entry [entry]
  (let [[_ year month day hour minute event id] (re-find re entry)]
    {:year (Integer/parseInt year)
     :month (Integer/parseInt month)
     :day (Integer/parseInt day)
     :hour (Integer/parseInt hour)
     :minute (Integer/parseInt minute)
     :event event
     :id (when id (Integer/parseInt id))}))

(defn extract-data
  ([input] (extract-data input '() (:id (first input))))
  ([input output id]
   (let [cur (first input)
         nxt (fnext input)
         rem (rest input)]
     (cond
       (empty? cur) output
       (:id cur) (recur rem output (:id cur))
       (= "falls" (:event cur)) (recur rem (conj output {:id id :slept (range (:minute cur) (:minute nxt))}) id)
       :else (recur rem output id)))))

(defn guard-ids [data]
  (set (map :id data)))

(defn filter-times [guard-id data]
  (let [times (mapcat :slept (filter #(= (:id %) guard-id) data))
        minute (first (first (sort-by #(second %) > (frequencies times))))]
    {:id guard-id :total-slept (count times) :minute minute}))

(defn map-times [parsed-data]
  (map #(filter-times % parsed-data) (guard-ids parsed-data)))

(defn solve1 [entry]
  (* (:id entry) (:minute entry)))

(def answer1
  (->> data
       (clojure.string/split-lines)
       (map parse-entry)
       (sort-by (juxt :year :month :day :hour :minute))
       (extract-data)
       (map-times)
       (sort-by :total-slept >)
       (first)
       (solve1)))
