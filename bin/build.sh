#!/bin/bash

set -o errexit
set -o nounset
set -o pipefail
set -o xtrace

readonly SCRIPT_DIR="$(
  cd "$(dirname "$0")"
  pwd
)"
readonly PROJECT_HOME="${SCRIPT_DIR}/.."
readonly DB_SERVICE="mysql"

readonly JAR=${PROJECT_HOME}"/app/build/libs/app.jar"

#
# 指定したサービスのログの、指定した文字列の出現回数を取得する
#
get_log_count_in_service() {
  local target_service="$1"
  local grep_condition="$2"

  docker-compose logs "$target_service" |
    grep -c "${grep_condition}"
}

#
# MySQL のコンテナが起動完了したかを返す
#
is_mysql_container_ready() {
  local target_service="$1"

  local ready_log_count="$(get_log_count_in_service "${target_service}" 'mysqld: ready for connections.')"
  [[ ready_log_count -eq 2 ]]
}

#
# MySQL のコンテナでエラーが発生したかを返す
#
error_occurred_in_service() {
  local target_service="$1"

  local error_log_count="$(get_log_count_in_service "${target_service}" 'ERROR')"
  [[ error_log_count -gt 0 ]]
}

#
# MySQL コンテナの起動を待つ
#
wait_for_mysql_container_starting() {
  while true; do
    if is_mysql_container_ready "${DB_SERVICE}"; then
      set +o xtrace
      echo '=================================='
      echo "${DB_SERVICE} container is ready !"
      echo '=================================='
      set -o xtrace
      break

    elif error_occurred_in_service "${DB_SERVICE}"; then
      set +o xtrace
      echo '==========================================' >&2
      echo "Error occurred in ${DB_SERVICE} container." >&2
      echo '==========================================' >&2
      set -o xtrace
      break

    else
      # 起動中の場合
      echo "Waiting for container starting..."
      sleep 1
    fi

  done
}

main() {
  cd "${PROJECT_HOME}"

  # クリーンアップ
  docker-compose down

  # MySQL 起動
  docker-compose up -d
  wait_for_mysql_container_starting

  # ビルド
  "${PROJECT_HOME}/gradlew" \
    clean \
    dependencyCheckAnalyze \
    build

  # JAR の状態での実行をテスト
  export DATA_DIR="${PROJECT_HOME}/data"
  echo -e "0\n0" | java -jar "${JAR}"

  # クリーンアップ
  docker-compose down
}

main "$@"
