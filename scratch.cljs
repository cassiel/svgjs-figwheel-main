(ns user
  (:require [hello-world.core :as core]
            [com.stuartsierra.component :as component]
            [hello-world.components.svg :as svg]))

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

(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (-> svg
      (.rect "100%" "100%")
      (.attr #js {:fill "#F06"})))


(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (-> svg
      (.symbol)
      (.rect 100 100)
      (.attr #js {:fill "#F06"})))



(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (-> svg
      (.defs)
      (.rect 100 100)
      (.attr #js {:fill "#F06"})))

(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (svg/empty-svg!)
  (let [w (.-node.clientWidth svg)
        h (.-node.clientHeight svg)]
    (-> svg
        (.nested)
        (.attr #js {:x (/ w 2) :y (/ h 2)}))))

(swap! core/S component/stop)

(swap! core/S component/start)

(svg/empty-svg!)

(def s
  (-> (deref core/S)
      :svg
      :svg))

(-> (deref core/S)
    :svg
    :svg)

(.attr s)


(.innerWidth (js/$ "svg.svgmain"))
