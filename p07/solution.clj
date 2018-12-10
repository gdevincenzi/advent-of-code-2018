(ns p07.solution
  (:require
   [clojure.string :as str]))

(def input
  (str/split-lines (slurp "./input.txt")))

(def re #".*([A-Z]).*([A-Z])")

(defn parse-entry [entry]
  (let [[_ instruction requirement] (re-find re entry)]
  (list instruction requirement)))

(defn available-keys [steps]
  (let [instructions (map #(first %) steps)
        requirements (map #(second %) steps)]
    (reduce
     (fn [acc cur]
       (if (some #{cur} requirements)
         acc
         (conj acc cur))) '() instructions)))

(defn next-key [steps]
  (first (sort (available-keys steps))))

(defn solve1
  ([steps] (solve1 steps '()))
  ([steps output]
   (let [nxt (first steps)
         rst (rest steps)
         next-key (next-key steps)]
     (if (empty? rst)
       (concat output (first nxt) (second nxt))
       (recur (remove #(= (first %) next-key) steps) (concat output next-key))))))

(def answer1
  (->> input
       (map parse-entry)
       (solve1)
       (apply str)))

(def instruction-values
  (zipmap (map #(str (char %)) (range 65 91)) (range 61 87)))

(defn available-work [steps]
  (let [instructions (into #{} (map #(first %) steps))
        requirements (into #{} (map #(second %) steps))]
    (sort (clojure.set/difference instructions requirements))))

(defn reassign-work [data]
  (let [available-instructions (remove #(some #{%} (keys (:workers data))) (available-work (:steps data)))
        instructions (take (- 5 (count (:workers data))) available-instructions)
        work (into {} (map #(hash-map % (get instruction-values %)) instructions))]
    (update data :workers conj work)))

(defn compute-work [data time]
  (reduce #(update-in % [:workers %2] - time) data (keys (:workers data))))

(defn execute-work [data]
  (let [[k t] (first (sort-by val (:workers data)))]
    (-> data
        (update ,, :time + t) ;; add time
        (compute-work ,, t) ;; dissoc worker
        (update ,, :workers dissoc k) ;; remove step
        (assoc ,, :steps (remove #(= (first %) k) (:steps data))))))

(defn solve2 [data]
  (cond
    (empty? (:steps data)) (:time data)
    (= 1 (count (:steps data)))
    (let [[s1 s2] (first (:steps data))]
      (+ (:time data) (get instruction-values s1) (get instruction-values s2)))
    :else
    (->> data
         (reassign-work)
         (execute-work)
         (recur))))

(def answer2 (solve2 {:time 0 :steps (map parse-entry input) :workers {}}))
