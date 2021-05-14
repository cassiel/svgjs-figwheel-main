(ns net.cassiel.svg.grass
  (:require [net.cassiel.svg.protocols :as px]
            [goog.string :as gstring]
            [goog.string.format]))

(defn single-grass [container x h num-segs]
  (let [path (.path container)
        path-str (gstring/format "M%d,0 %d,%d z" x x h)
        text (.text container #(do
                                 (-> (.tspan % "We go")
                                     (.dy 50))
                                 (-> (.tspan % "down")
                                     (.fill "#F09")
                                     (.dy 100))))
        text2 (.text container #(do
                                  (.tspan % "try try again")))
        textPath (.path text2 path-str)]

    (-> textPath
        (.animate 2000)
        (.ease "<>")
        (.plot (gstring/format "M%d,0 %d,%d z" x (+ x 100) h))
        (.loop true true))))

(deftype GrassForm []
  px/FORM
  (render [this container size form-state]
    (single-grass container 0 100 4))

  (tick [this container ts form-state]
    (js/console.log ts)))
