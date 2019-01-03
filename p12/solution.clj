(ns p12.solution
  (:require [clojure.zip :as zip
             clojuse.string :as s]))

(def testinput
  (s)"#..#.#..##......###...###")

(def z (zip/vector-zip testinput))
