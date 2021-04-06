(ns ^:figwheel-hooks hello-world.core
  (:require [com.stuartsierra.component :as component]
            [hello-world.components.dummy :as dummy]
            [hello-world.components.svg :as svg]
            #_ [cljsjs.svgjs]))

(enable-console-print!)

(defn system []
  (component/system-map :dummy (dummy/map->DUMMY {})
                        :svg (svg/map->SVG {})))

(defonce S (atom (system)))

(defn ^:before-load teardown []
  (swap! S component/stop))

(defn ^:after-load startup []
  (swap! S component/start))
