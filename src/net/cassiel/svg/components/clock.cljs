(ns net.cassiel.svg.components.clock
  "Clock times on the second, wall-clock time."
  (:require-macros [cljs.core.async.macros :refer [go go-loop alt!]])
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [starting stopping]]
            [cljs.core.async :as a :refer [>! <!]]))

(defn ms-to-next-second []
  (let [now (.now js/Date)
        now+sec (+ now 1000)
        next-sec-boundary (- now+sec (mod now+sec 1000))]
    (- next-sec-boundary now)))

(defn start-clock! [chan]
  (go-loop []
    (<! (a/timeout (ms-to-next-second)))
    (when (>! chan (.now js/Date))            ; Might be slightly ahead of the second boundary.
      (recur)))
  )

(defrecord CLOCK [tick-chan installed?]
  Object
  (toString [this] (str "CLOCK " (seq this)))

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [tick-chan (a/chan)]
                         (start-clock! tick-chan)
                         ;; Debug:
                         (assoc this
                                :tick-chan tick-chan
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (a/close! tick-chan)
                         (assoc this
                                :tick-chan nil
                                :installed? false)))))
