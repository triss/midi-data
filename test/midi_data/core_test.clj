(ns midi-data.core-test
  "Tests for midi-data.core."
  (:use midje.sweet)
  (:use [midi-data.core]))

(def test-map 
  "Represents a note-on on channel 1 with velocity 100 and note of 60. "
  {:channel 1 
   :midi-msg :note-on 
   :note 60 
   :velocity 100})

(facts "about `decode`"
       (fact "provides representation of MIDI data tuple as a map."
             (decode [0x91 60 100])     => test-map)
       (fact "`decode` is practically inverse of `encode`"
             (decode (encode test-map)) => test-map))

(facts "about `encode`"
       (fact "Converts data in map back to MIDI data tuple."
             (encode  test-map)              => [0x91 60 100])
       (fact "`encode` is practically inverse of `decode`"
             (encode (decode [0x91 60 100])) => [0x91 60 100]))
