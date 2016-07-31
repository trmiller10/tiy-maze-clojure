(defproject tiy-clojure-maze-nightcode "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [tiy-clojure-maze-nightcode.core]
  :main tiy-clojure-maze-nightcode.core)
