(ns hello-world.components.svg
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]))

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
                             (.attr "id" "svgmain")
                             #_ (.size 500 500)
                             (.size "100%" "100%")
                             )
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

(defn empty-svg! []
  (.empty (js/$ "svg#svgmain")))
