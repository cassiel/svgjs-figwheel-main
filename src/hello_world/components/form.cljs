(ns hello-world.components.form
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]))

;; I don't think the SVG form is stateful or intricate enough to require a
;; component layer, so for now this is purely vestigial.

(defrecord FORM [installed?]
  Object
  (toString [this] (str "FORM " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let []
                         (assoc this
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (assoc this
                                :installed? false)))))
