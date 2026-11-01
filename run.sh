#!/usr/bin/env bash
# usage: ./run.sh build | demo | test | bench | plots | all
set -euo pipefail
cd "$(dirname "$0")"

build() {
  rm -rf out out-test
  javac --release 17 -d out $(find src -name "*.java")
  javac --release 17 -cp out -d out-test $(find test -name "*.java")
}

case "${1:-all}" in
  build) build ;;
  demo)  build && java -cp out app.SmartCityDemo ;;
  test)  build && java -cp out:out-test tests.AllTests ;;
  bench) build && java -Xms1g -Xmx2g -cp out experiments.BenchmarkRunner results/tables ;;
  plots) python3 scripts/plot_results.py ;;
  all)   build && java -cp out:out-test tests.AllTests && java -cp out app.SmartCityDemo \
           && java -Xms1g -Xmx2g -cp out experiments.BenchmarkRunner results/tables \
           && python3 scripts/plot_results.py ;;
  *) echo "usage: $0 build|demo|test|bench|plots|all"; exit 1 ;;
esac
