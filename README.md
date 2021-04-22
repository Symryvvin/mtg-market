```shell
kubectl create namespace mtg
kubectl config set-context --current --namespace mtg
```

```shell
helm install mtg-db bitnami/postgresql -f deploy/database/search/values.yml
```

```shell
docker-compose -f search/target/docker-compose.yml build
```