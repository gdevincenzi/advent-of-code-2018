(ns p13.solution)

(def input (slurp "./input.txt"))

(defn build-keys [y]
  (map #(vec [%1 %2]) (iterate inc 0) (repeat y)))

(defn build-line [y-range line]
  (zipmap (build-keys y-range) line))

(defn parse-tracks [input]
  (->> input
       (clojure.string/split-lines)
       (map build-line (iterate inc 0))
       (reduce conj {})))

(defn carts-to-tracks [carts]
  (mapcat #(if (contains? #{\v \^} (second %)) [(first %) \|] [(first %) \-]) carts))

(defn is-cart? [char]
  (contains? #{\v \^ \> \<} char))

(defn find-carts [tracks]
  (filter #(is-cart? (second %)) tracks))

(defn parse-cart [cart-data]
  (let [[c d] cart-data
        dir (get {\^ 0 \> 1 \v 2 \< 3} d)]
    {:pos c :dir dir :turn-count 0}))

(defn parse-carts [tracks]
  (->> tracks
       (find-carts)
       (map parse-cart)))

(defn remove-spaces [parsed-tracks]
  (->> parsed-tracks
       (remove #(= \space (second %)))
       (reduce conj {})))

(defn remove-carts [tracks]
  (->> tracks
       (find-carts)
       (carts-to-tracks)
       (apply assoc tracks)))

(def parsed-tracks (parse-tracks input))

(def carts (parse-carts parsed-tracks))

(def tracks
  (->> parsed-tracks
       (remove-spaces)
       (remove-carts)))

(def cart-moves
  {0 (fn [[x y]] [x (dec y)])      ;; ^ - up
   1 (fn [[x y]] [(inc x) y])      ;; > - right
   2 (fn [[x y]] [x (inc y)])      ;; v - down
   3 (fn [[x y]] [(dec x) y])})    ;; < - left

(def turn-order [-1 0 1]) ;; left, straight, right

(defn next-turn [turn-counter]
  (get turn-order (mod turn-counter 3)))

(defn next-direction [cart]
  (let [{dir :dir
         tc :turn-count} cart
        nt (next-turn tc)]
    (mod (+ dir nt) 4)))

(defn move-cart [cart]
  (let [{pos :pos
         dir :dir} cart]
    (assoc cart :pos ((get cart-moves dir) pos))))

(defn turn-left-and-move [cart]
  (let [next-dir (mod (dec (:dir cart)) 4)]
    (-> cart
        (assoc ,, :dir next-dir)
        (move-cart))))

(defn turn-right-and-move [cart]
  (let [next-dir (mod (inc (:dir cart)) 4)]
    (-> cart
        (assoc ,, :dir next-dir)
        (move-cart))))

(defn turn-on-intersection-and-move [cart]
  (let [next-tc (inc (:turn-count cart))
        next-dir (next-direction cart)]
    (-> cart
        (assoc ,, :dir next-dir :turn-count next-tc)
        (move-cart))))

(defn process-cart [cart]
  (let [{pos :pos
         dir :dir
         tc :turn-count} cart
        track (get tracks pos)]
    (cond
      (= track \+) (turn-on-intersection-and-move cart)
      (= track \\) (if (even? dir)
                     (turn-left-and-move cart)
                     (turn-right-and-move cart))
      (= track \/) (if (even? dir)
                     (turn-right-and-move cart)
                     (turn-left-and-move cart))
      :else (move-cart cart))))

(defn advance-tick
  ([carts] (advance-tick '() (sort-by second (sort-by first carts))))
  ([acc remn]
   (let [current-state (concat acc remn)
         positions (map :pos current-state)
         highest-freq-pos (first (sort-by val > (frequencies positions)))]
     (cond
       (> (second highest-freq-pos) 1) (first highest-freq-pos)
       (empty? remn) acc
       :else (recur (conj acc (process-cart (first remn))) (rest remn))))))

(defn advance-to-crash [carts]
  (let [next-tick (advance-tick carts)]
    (if (seq? next-tick)
      (recur next-tick)
      next-tick)))

(def answer1 (advance-to-crash carts))

(defn advance-tick-and-remove-crashes
  ([carts] (advance-tick-and-remove-crashes '() (sort-by second (sort-by first carts))))
  ([acc remn]
   (let [current-state (concat acc remn)
         positions (map :pos current-state)
         crashes (into #{} (map first (filter #(> (second %) 1) (frequencies positions))))]
     (cond
       (not (empty? crashes)) (let [new-acc (remove #(contains? crashes (:pos %)) acc)
                                    new-remn (remove #(contains? crashes (:pos %)) remn)]
                                (if (empty? remn)
                                  new-acc
                                  (recur (conj new-acc (process-cart (first new-remn))) (rest new-remn))))
       (empty? remn) acc
       :else (recur (conj acc (process-cart (first remn))) (rest remn))))))

(defn advance-to-last [carts]
  (let [next-tick (advance-tick-and-remove-crashes carts)]
    (if (= 1 (count next-tick))
      (:pos (first next-tick))
      (recur next-tick))))

(def answer2 (advance-to-last carts))
