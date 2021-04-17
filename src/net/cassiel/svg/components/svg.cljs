(ns net.cassiel.svg.components.svg
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]
            [net.cassiel.svg.form :as form]))

(defn empty-svg! []
  (.empty (js/$ "svg.svgmain")))

(defn calculate-square-parameters
  "Calculate x, y and side length of maximum square in this SVG area."
  []
  (let [elem (js/$ "svg.svgmain")
        w (.innerWidth elem)
        h (.innerHeight elem)]
    (if (> w h)
      {:x (/ (- w h) 2)
       :y 0
       :size h}
      {:x 0
       :y (/ (- h w) 2)
       :size w})))

(defn render [svg]
  (let [{:keys [x y size]} (calculate-square-parameters)
        svg' (-> (.nested svg) (.move x y))]
    (form/render svg' size)))

(defrecord SVG [svg installed?]
  Object
  (toString [this] (str "SVG " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [svg (js/SVG)]
                         (-> svg
                             (.addTo "#main")
                             (.addClass "svgmain")
                             (.size "100%" "100%"))
                         (render svg)
                         (assoc this
                                :svg svg
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (.remove (js/$ "svg"))
                         (assoc this
                                :svg nil
                                :installed? false)))))
