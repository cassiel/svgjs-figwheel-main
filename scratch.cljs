(ns user
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [net.cassiel.svg.core :as core]
            [com.stuartsierra.component :as component]
            [net.cassiel.svg.components.svg :as svg]
            [net.cassiel.svg.form :as form]
            [cljs.core.async :as a :refer [>! <!]]))

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

;; =====
(do
  (swap! core/S component/stop)
  (reset! core/S (core/system))
  (swap! core/S component/start))
;; =====

(.remove (js/$ "svg"))

(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  (svg/empty-svg!)
  (let [n (.nested svg)]
    (.attr n #js {:x "25%"})
    (-> svg
        (.rect "50%" "50%")
        (.fill "#888")
        (.radius 50)
        #_ (.attr #js {:fill "#000"}))
    (-> n
        (.rect "50%" "50%")
        (.move "25%" "25%")
        (.move 0 0)
        (.radius 50)
        (.attr #js {:fill "#F06"}))))



(when-let [svg (-> (deref core/S)
                   :svg
                   :svg)]
  #_ (.children svg)
  (svg/empty-svg!)
  (form/render svg 300))


(svg/empty-svg!)

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

(letfn [(foo [] {:A 1})]
  #js [(foo)])

(svg/calculate-square-parameters)

(js/SVG.G.)

(let [c (js/SVG.Circle. #js {:fill "#000000"})]
  (.size c 100)
  (.center c 50 50)
  (.attr c))

;; ---- TIME

(.getTime (js/Date.))
(.getSeconds (js/Date.))

(.getMinutes (js/Date. (+ (.now js/Date) 10000)))


;; Testing channels

(def a (a/chan))
(go (println "RD" (<! a)))
(go (println "WR" (>! a 999)))
(a/close! a)
