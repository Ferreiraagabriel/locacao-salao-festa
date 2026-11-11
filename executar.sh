#!/usr/bin/env bash
set -euo pipefail
JDBC_JAR="${1:-sqlite-jdbc-3.54.1.0.jar}"
if [[ ! -f "$JDBC_JAR" ]]; then
  echo "Driver SQLite não encontrado: $JDBC_JAR"
  echo "Passe o caminho do sqlite-jdbc-3.54.1.0.jar como primeiro argumento."
  exit 1
fi
rm -rf out
mkdir -p out
javac --release 17 -encoding UTF-8 -cp "$JDBC_JAR" -d out $(find src -name '*.java')
java -cp "out:$JDBC_JAR" br.ufms.locacao.Main
