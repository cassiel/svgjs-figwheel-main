(ns hello-world.components.resizer
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]))

(defrecord RESIZER [installed?]
  Object
  (toString [this] (str "RESIZER " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(do
                         (-> (js/$ js/window)
                             (.on "resize" (fn [] (println "*RESIZE*"))))
                         (assoc this
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (-> (js/$ js/window)
                             (.off "resize"))
                         (assoc this
                                :installed? false)))))
