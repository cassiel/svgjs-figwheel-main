(ns net.cassiel.svg.protocols)

(defprotocol FORM
  "SVG-generating form to render in this framework."
  (render [this container size form-state] "Render from scratch.")
  (tick [this container ts form-state] "Take action according to on-the-second tick."))
