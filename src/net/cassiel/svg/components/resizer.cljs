(ns net.cassiel.svg.components.resizer
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]
            [net.cassiel.svg.components.svg :as svg]
            [cljs.core.async :as a :refer [>! <!]]))

(defn throttle
  "Speed limit messages coming into in-ch, echoing to out-chan after a timeout."
  [in-ch out-ch]
  (go-loop [held-value nil]
    (if held-value
      (alt!
        in-ch ([v] (when v (recur v)))
        (a/timeout 250) (when (>! out-ch held-value)
                          (recur nil)))
      (when-let [v (<! in-ch)] (recur v)))))

(defrecord RESIZER [svg fast-chan slow-chan installed?]
  Object
  (toString [this] (str "RESIZER " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [fast-chan (a/chan)
                             slow-chan (a/chan)]
                         (throttle fast-chan slow-chan)
                         (-> (js/$ js/window)
                             (.on "resize" (fn [] (go (>! fast-chan :RESIZE)))))
                         (go-loop []
                           (when-let [_ (<! slow-chan)]
                             (svg/empty-svg!)
                             (svg/render (:svg svg) (:form svg) (:form-state svg))
                             (recur)))
                         (assoc this
                                :fast-chan fast-chan
                                :slow-chan slow-chan
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (-> (js/$ js/window)
                             (.off "resize"))
                         (a/close! fast-chan)
                         (a/close! slow-chan)
                         (assoc this
                                :fast-chan nil
                                :slow-chan nil
                                :installed? false)))))
