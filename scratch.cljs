(ns user
  (:require [hello-world.core :as core]
            [com.stuartsierra.component :as component]
            [hello-world.components.svg :as svg]))

(.-addTo (identity js/SVG))

(identity js/SVG)

(js/SVG "#main")


(svg/empty-svg!)


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
  (svg/empty-svg!)
  (let [n (.nested svg)]
    (.attr n #js {:x "25%"})
    (-> svg
        (.rect "50%" "50%")
        (.radius 50)
        (.attr #js {:fill "#000"}))
    (-> n
        (.rect "50%" "50%")
        (.move "25%" "25%")
        (.move 0 0)
        (.radius 50)
        (.attr #js {:fill "#F06"}))))



;; --- FILTER
(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (svg/empty-svg!)
  (let [n (.nested svg)]
    (.attr n #js {:x "25%"})
    (-> svg
        (.rect "50%" "50%")
        (.radius 50)
        (.attr #js {:fill "#000"}))))

;; ---


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

;; -------

(swap! core/S component/stop)

(swap! core/S component/start)

;; ------ Filtering

(svg/empty-svg!)

(def s
  (-> (deref core/S)
      :svg
      :svg))


(def r (.rect s "100%" "100%"))

(.filterWith r (fn [add]
                 (-> (.turbulence add 0.005 2)
                     (.animate 3000)
                     (.attr #js {;;:numOctaves 7
                                 :baseFrequency 0.01})
                     )))

(-> (deref core/S)
    :svg
    :svg)

(.attr s)


(.innerHeight (js/$ "svg.svgmain"))
