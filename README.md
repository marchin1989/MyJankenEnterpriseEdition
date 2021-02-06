# MyJankenEnterpriseEdition

![badge.svg](https://github.com/marchin1989/MyJankenEnterpriseEdition/workflows/workflow/badge.svg)

[じゃんけん Advent Calendar 2020](https://qiita.com/advent-calendar/2020/janken)の追走。

[JankenEnterpriseEdition](https://github.com/os1ma/JankenEnterpriseEdition)

## 開発環境のセットアップ

以下のコマンドにより、`git commit` 実行時にビルドが通るかローカルで検証されるようになります。

```shell
printf '#!/bin/bash\n'"$(pwd)"'/bin/build.sh\n' > .git/hooks/pre-commit
chmod +x .git/hooks/pre-commit
```

## 実行手順

### CLI アプリケーション

```shell
./gradlew clean jar
DATA_DIR="$(pwd)/data" java -jar app/build/libs/app.jar
```

### Web アプリケーション

```shell
./gradlew clean war
docker-compose -f docker-compose.yml -f docker-compose-tomcat.yml up -d
```

その後、以下の URL などにアクセス

- http://localhost:8080/api/health
