(ns p14.solution)

(def input 681901)

(def recipes [3 7])

(defn make-new-recipes [r1 r2]
  (map #(read-string (str %)) (str (+ r1 r2))))

(defn create-recipes [data]
  (if (map? data)
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
             :elf2 (mod (+ elf2 (inc recipe2)) total)))
    {:recipes [3 7 1 0] :elf1 0 :elf2 1}))
