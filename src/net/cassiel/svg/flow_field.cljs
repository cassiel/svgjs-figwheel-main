(ns net.cassiel.svg.flow-field
  "Demo flow field form."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format])
  )

(defn draw-lines [container size]
  (let [step-size (/ size 10)

        scale (fn [x] (+ (- (/ js/Math.PI 4))
                         (* x (/ js/Math.PI 2))))

        rads (map #(* step-size (js/perlinNoise (/ % 9) 0)) (range 10))
        thetas (map #(scale (js/perlinNoise (/ % 9) 100)) (range 10))

        x-seq
        (map (fn [r, th] (* r (js/Math.cos th)))
             rads thetas)

        y-seq
        (map (fn [r, th] (* r (js/Math.sin th)))
             rads thetas)

        x-pos-seq
        (map (partial reduce +)
             (reductions conj [] x-seq))

        y-pos-seq
        (map (partial reduce +)
             (reductions conj [] y-seq))

        positions (interleave x-pos-seq y-pos-seq)

        _ (js/console.log positions)
        ]

    (-> (.polyline container (clj->js positions))
        (.fill "none")
        (.stroke "#000000"))
    )
  )

(deftype FlowFieldForm []
  px/FORM
  (render [this container size form-state]
    (draw-lines container size))

  (tick [this container ts form-state]
    nil))
