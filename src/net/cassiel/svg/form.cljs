(ns net.cassiel.svg.form
  "The actual SVG form, drawn into a provided SVG.js container
   assumed to be a square, with size provided.")

(defn render
  "Render an SVG form into `container`, a square of side `size`."
  [container size]
  (-> container
      (.circle size)
      (.fill "#000000")))
