(ns user
  (:require [hello-world.core :as core]
            [com.stuartsierra.component :as component]))

(.-addTo (identity js/SVG))

(identity js/SVG)

(js/SVG "#main")


(def draw (-> (js/SVG)
              (.addTo "#main")
              (.size 300 300)))

(-> draw
    (.rect 100 100)
    (.attr #js {:fill "#F06"}))

(swap! core/S component/stop)

(swap! core/S component/start)

(.remove (js/$ "svg"))

(-> (deref core/S)
    :svg
    :svg)
