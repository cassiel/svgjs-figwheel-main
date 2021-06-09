(ns net.cassiel.svg.forms.flow-field
  "Demo flow field form."
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format])
  )

(defn draw-line [container size steps y-pos]

  (let [step-size (* 1 (/ size steps))

        scale (fn [x] (+ (- (/ js/Math.PI 4))
                         (* x (/ js/Math.PI 2))))

        rads (map #(* step-size (js/perlinNoise (/ % 9) (js/Math.random))) (range steps))
        thetas (map #(scale (js/perlinNoise (js/Math.random) (/ % 9))) (range steps))

        x-seq
        (map (fn [r, th] (* r (js/Math.cos th)))
             rads thetas)

        y-seq
        (map (fn [r, th] (* r (js/Math.sin th)))
             rads thetas)

        x-seq1
        (map (fn [r, th] (* (* r 1.25) (js/Math.cos (* -3 th))))
             rads thetas)

        y-seq1
        (map (fn [r, th] (* (* r 1.5) (js/Math.sin (* -3 th))))
             rads thetas)

        x-pos-seq
        (map (partial reduce +)
             (reductions conj [] x-seq))

        y-pos-seq
        (map (partial reduce +)
             (reductions conj [] y-seq))

        x-pos-seq1
        (map (partial reduce +)
             (reductions conj [] x-seq1))

        y-pos-seq1
        (map (partial reduce +)
             (reductions conj [] y-seq1))

        positions (interleave x-pos-seq y-pos-seq)
        positions1 (interleave x-pos-seq1 y-pos-seq1)

        _ (js/console.log positions)]

    (-> (.polyline container (clj->js positions))
        (.fill "none")
        (.stroke "#000000")
        (.translate 0 y-pos)
        (.animate 2000)
        (.ease "<>")
        (.plot  (clj->js positions1))
        (.loop true true)
        )
    )
  )

(deftype FlowFieldForm []
  px/FORM
  (render [this container size form-state]
          (map (fn [i] (draw-line container size 10 (* i (/ size 10))))
               (range 10))
          (draw-line container size 10 (/ size 10))
          (draw-line container size 10 (* 2 (/ size 10)))
          (draw-line container size 10 (* 1.3 (/ size 10)))

          #_(map (fn [i] (draw-line container size 10 (* i (/ size 10))))
                 (range 10)))

  (tick [this container ts form-state]
    nil))
