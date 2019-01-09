(ns p14.solution)

(def input 681901)

(defn make-new-recipes [r1 r2]
  (map #(read-string (str %)) (str (+ r1 r2))))

(defn create-recipes [data]
  (let [{recipes :recipes
         elf1 :elf1
         elf2 :elf2} data
        recipe1 (get recipes elf1)
        recipe2 (get recipes elf2)
        new-recipes (make-new-recipes recipe1 recipe2)
        all-recipes (apply conj recipes new-recipes)
        total (count all-recipes)]
    (assoc data
           :recipes all-recipes
           :elf1 (mod (+ elf1 (inc recipe1)) total)
           :elf2 (mod (+ elf2 (inc recipe2)) total))))

(def answer1
  (->> input
       (+ 10)
       (nth (iterate create-recipes {:recipes [3 7 1 0] :elf1 0 :elf2 1}))
       (:recipes)
       (drop input)
       (take 10)
       (apply str)
       (read-string)))

(def vector-input
  (vec (map #(read-string (str %)) (str input))))

(def input-size (count vector-input))

(def strinput (str input))

(defn find-recipe [data]
  (let [new-data (create-recipes data)
        last-recipes (apply str (take-last input-size (:recipes new-data)))]
    (if (= last-recipes strinput)
      (:recipes new-data)
      (recur new-data))))

(defn find-recipe [r elf1 elf2]
  (let [^long r1 (r elf1)
        ^long r2 (r elf2)
        new-recipes (make-new-recipes r1 r2)
        recipes (apply conj r new-recipes)
        total (count recipes)
        last-recipes (subvec recipes (- total input-size))]
    (if (= last-recipes vector-input)
      (- total input-size)
      (recur recipes (mod (+ elf1 (inc r1)) total) (mod (+ elf2 (inc r2)) total)))))


(defn find-recipe [data]
  (let [new-data (create-recipes data)
        recipes (:recipes new-data)
        last-recipes (subvec recipes (- (count recipes) input-size))]
    (if (= last-recipes vector-input)
      (- (count recipes) input-size)
      (recur new-data))))
