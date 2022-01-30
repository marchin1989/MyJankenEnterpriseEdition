# MyJankenEnterpriseEdition

![badge.svg](https://github.com/marchin1989/MyJankenEnterpriseEdition/workflows/workflow/badge.svg)

[じゃんけん Advent Calendar 2020](https://qiita.com/advent-calendar/2020/janken)の追走。

[JankenEnterpriseEdition](https://github.com/os1ma/JankenEnterpriseEdition)

## 開発環境のセットアップ

プロジェクトホームで以下のコマンドを実行することで、Git の hook が設定され、`git commit` 実行時にビルドが通るかローカルで検証されるようになります。

```shell
ln -s ../../bin/pre-commit .git/hooks/pre-commit
```

## 開発環境での実行手順

```shell
docker-compose -f docker-compose.yml up -d
./bin/run.sh develop
```

その後、以下の URL などにアクセス

- http://localhost:8080/api/health
