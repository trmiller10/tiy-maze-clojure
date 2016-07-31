(ns tiy-clojure-maze-nightcode.core
  (:gen-class))

(def size 10)

(defn create-rooms []
  (for [row (range size)]
    (for [col (range size)]
      {:row row, :col col, :visited? false, :bottom? true, :right? true})))
