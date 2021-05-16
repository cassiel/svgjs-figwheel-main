(ns net.cassiel.svg.checker
  (:require [clojure.spec.alpha :as s]))

(defn debug [tag x]
  ;;(js/console.log tag (str x))
  x)

(defn conformer
  "This is a bit of a hack."
  [key x]
  (let [attempt (s/conform key x)]
    (if (= attempt ::s/invalid)
      (throw (js/Error. (s/explain key x)))
      (debug "conformed" attempt))))
