(ns net.cassiel.svg.grid
  "Demo grid form."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format])
  )

(def GRID_SIZE 10)

(defn make-tiles [container size]
  (let [defs (.defs (.root container))]
    (-> defs
        (.rect (/ size 1.4 GRID_SIZE) (/ size 1.4 GRID_SIZE))
        (.fill "#E0E0E0")
        (.stroke #js {:color "#000000"
                      :width 1})
        (.radius (/ size 10 GRID_SIZE))))
  )

(defn tile-at [g size x-pos y-pos]
  (let [level (* 100 (js/perlinNoise x-pos y-pos))]
    (-> g
        (.rect (/ size 1.4 GRID_SIZE) (/ size 1.4 GRID_SIZE))
        (.fill (gstring/format "hsl(0, 0%, %f%%)" level))
        (.stroke #js {:color "#000000"
                      :width 1})
        (.radius (/ size 10 GRID_SIZE))
        (.center x-pos y-pos))))

(defn stack-at [g size x-pos]
  (let [pitch (/ size GRID_SIZE)]
    (doseq [y (range GRID_SIZE)
            :let [y-shifted (- y (/ (dec GRID_SIZE) 2))
                  y-pos (+ (/ size 2) (* y-shifted pitch))]]
      (tile-at g size x-pos y-pos))))

(deftype GridForm []
  px/FORM
  (render [this container size form-state]
    (let [g (.group container)
          pitch (/ size GRID_SIZE)]
      (make-tiles g size)
      (doseq [x (range GRID_SIZE)
              :let [x-shifted (- x (/ (dec GRID_SIZE) 2))
                    x-pos (+ (/ size 2) (* x-shifted pitch))]]
        (stack-at g size x-pos))
      (.addTo g container)))

  (tick [this container ts form-state]))
