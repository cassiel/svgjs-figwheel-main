(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Functional style. Return an isolated form."
  [container size]
  (println "render" container)
  (let [;; Gradients seem to get hoisted to the root anyway, but for clarity:
        disc-grad (-> (.gradient (.root container) "linear" #(doto %
                                                               (.stop 0 "#303030")
                                                               (.stop 0.5 "#FFBB80")
                                                               (.stop 1 "#CC0000")))
                      (.from 0.5 0.2)     ; GUESS: normalised coordinates, 0..1,
                      (.to 0.5 0.8))

        rect-grad (-> (.gradient (.root container) "linear" #(doto %
                                                               (.stop 0 "#F0C040")
                                                               (.stop 1 "#C09010")))
                      (.from 0 0.2)
                      (.to 0 0.8))

        text-grad (-> (.gradient (.root container) "linear" #(doto %
                                                               (.stop 0 "#000000")
                                                               (.stop 0.5 "#FFFFFF")
                                                               (.stop 1 "#202080")))
                      ;; Build a gradiant TR to BL, to allow for later form rotation.
                      (.from 0.9 0.1)       ; Y grows downwards.
                      (.to 0.1 0.9))
        g         (.group container)    ; I don't think the creating element matters
        ]
    (-> g
        (.attr #js {:fill-opacity 0})
        (.stroke #js {:color   "#000000"
                      :opacity 0
                      :width   2})
        (.animate 2000)
        (.attr #js {:fill-opacity 1})
        (.after #(-> g
                     (.animate 500)
                     (.stroke #js {:opacity 0.5}))))

    (-> container
        (.circle (* size 0.9))
        (.fill disc-grad)
        (.stroke #js {:color   "#000000"
                      :opacity 0.3
                      :width   2})
        (.center (/ size 2) (/ size 2)))
    (-> g
        (.rect (/ size 2) (/ size 2))
        (.radius 10)                    ; Rounded corners.
        (.fill rect-grad)
        #_ (.stroke #js {:color   "#000000"
                      :opacity 0.5
                      :width   2})
        (.center (/ size 2) (/ size 2))
        ;; Rotates can be accumulated; it appears that translates can't.
        (.rotate 35)
        (.rotate 10)
        (.rotate -45))
    (-> g
        (.text "HELLO\nSVG\nWORLD")
        (.font #js {"family" "Microgramma Bold"
                    "size"   (/ size 13)
                    "anchor" "middle"})
        (.fill text-grad)
        ;; .size() doesn't seem to work, at least for fonts,
        ;; although including it in a map (above)
        ;; works fine, as does setting font-size via .attr().
        #_ (.size (/ size 12))
        #_ (.attr "font-size" (/ size 12))
        (.center (/ size 2) (/ size 2))
        #_ (.animate #js {:duration 2000})
        (.animate 500 "circInOut")
        #_ (.ease "<>")
        (.rotate -45)
        (.after #(println "DONE")))
    (-> g
        (.text "00:00:00")
        (.font #js {"family" "Microgramma Bold"
                    "size"   (/ size 20)
                    "anchor" "middle"})
        (.fill "#000000")
        (.stroke #js {:width 0})        ; !!!!!
        (.center (/ size 2) (* size 0.85))
        (.clear)
        (.text "11:11:11")
        )

    (.addTo g container)))

(defn tick
  "Incoming on-or-slightly-after-the-second tick (in ms since epoch)."
  [container ts]
  (println "TS" container ts))
