(ns net.cassiel.svg.forms.grid
  "Demo grid form."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format])
  )

(def GRID_SIZE 10)

(defn make-tiles [container size state]
  (let [pitch (/ size GRID_SIZE)
        side (-> size (/ GRID_SIZE) (* 0.8))
        margin (-> size (/ GRID_SIZE) (* 0.1))
        defs (.defs (.root container))]
    (doseq [i (range 10)]
      (let [g (.group defs)]
        (-> g
            (.rect side side)
            (.fill "#C0C0C0")
            (.stroke #js {:color "#000000"
                          :width 1
                          :opacity 0.2})
            (.radius (/ size 10 GRID_SIZE))
            (.move margin margin))
        (-> g
            (.text (str i))
            (.font #js {:family "Microgramma Bold"
                        :size   (/ size 18)
                        :anchor "middle"
                        :fill "#808080"})
            (.center (/ pitch 2) (/ pitch 2)))
        (swap! state assoc-in [:deps i] g))))
  )

(defn tile-at [g size x-pos y-pos state]
  (let [level (* 100 (js/perlinNoise x-pos y-pos))
        item (get-in @state [:deps (rand-int 10)])]
    (-> g
        (.use item)
        ;; Can't use center() on arbitrary saved items?
        (.move x-pos y-pos)
        )
    #_ (-> g
        (.rect (/ size 1.4 GRID_SIZE) (/ size 1.4 GRID_SIZE))
        (.fill (gstring/format "hsl(90, 0%, %f%%)" level))
        (.stroke #js {:color "#000000"
                      :width 1})
        (.radius (/ size 10 GRID_SIZE))
        (.center x-pos y-pos))))

(defn stack-at [g size x-pos state]
  (let [pitch (/ size GRID_SIZE)]
    (doseq [y (range GRID_SIZE)
            :let [y-pos (* y pitch)]]
      (tile-at g size x-pos y-pos state))))

(deftype GridForm []
  px/FORM
  (render [this container size state]
    (let [g (.group container)
          pitch (/ size GRID_SIZE)]
      (make-tiles g size state)
      (doseq [x (range GRID_SIZE)
              :let [x-pos (* x pitch)]]
        (stack-at g size x-pos state))
      (.addTo g container)))

  (tick [this container ts state]))
