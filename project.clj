(defproject org.clojars.triss/midi-data "0.2.0-SNAPSHOT"
  :description "Handles parsing of MIDI data tuples to maps containing friendly
               representations of the events they represent."
  :url "https://github.com/triss/midi-data/"
  :license {:name "Do What The Fuck You Want To Public License, Version 2"
            :url  "http://www.wtfpl.net/txt/copying"}
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :profiles {:dev {:dependencies [[midje "1.7.0"]]}})
