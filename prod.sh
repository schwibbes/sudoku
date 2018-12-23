#!/bin/bash

rm -rf build/
cp -r resources/public/* docs/
clojure -m figwheel.main --optimizations advanced -o docs/cljs-out/dev-main.js --output-dir "build/" --build-once dev