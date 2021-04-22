(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Functional style. Return an isolated form."
  [container size]
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
                                                               (.stop 0.5 "#FF4040")
                                                               (.stop 1 "#202080")))
                      ;; Build a gradiant TR to BL, to allow for later form rotation.
                      (.from 0.7 0.3)       ; Y grows downwards.
                      (.to 0.3 0.7))
        ]
    (-> container
        (.circle (* size 0.9))
        (.fill disc-grad)
        (.stroke #js {:color "#000000"
                      :opacity 0.3
                      :width 2})
        (.center (/ size 2) (/ size 2)))
    (-> container
        (.rect (/ size 2) (/ size 2))
        (.radius 10)                    ; Rounded corners.
        (.fill rect-grad)
        (.stroke #js {:color "#000000"
                      :opacity 0.5
                      :width 2})
        (.center (/ size 2) (/ size 2))
        ;; Rotates can be accumulated; it appears that translates can't.
        (.rotate 35)
        (.rotate 10)
        (.rotate -45))
    (-> container
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
        (.rotate -45))))
