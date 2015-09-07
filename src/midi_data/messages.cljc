(ns midi-data.messages
  "Provide's functions for quickly creating MIDI messages of various types.")

(defn note-on 
  "Construct's a note on message."
  [note velocity & [channel]] 
  {:midi-msg :note-on
   :note     note
   :velocity velocity
   :channel  channel})

(defn note-off 
  "Construct's a note on message."
  [note velocity & [channel]] 
  {:midi-msg :note-off
   :note     note
   :velocity velocity
   :channel  channel})

(defn control
  "Construct's a control (sometimes called cc) message."
  [number value & [channel]] 
  {:midi-msg :control
   :number   number
   :value    value
   :channel  channel})
