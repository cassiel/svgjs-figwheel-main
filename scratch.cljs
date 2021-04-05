(ns user)

(.-addTo (identity js/SVG))

(identity js/SVG)

(js/SVG "#main")


(def draw (-> (js/SVG)
              (.addTo "#main")
              (.size 300 300)))

(-> draw
    (.rect 100 100)
    (.attr #js {:fill "#F06"}))
