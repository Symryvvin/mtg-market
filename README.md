```shell
kubectl create namespace mtg
kubectl config set-context --current --namespace mtg
```

```shell
docker-compose -f search/target/docker-compose.yml build
docker-compose -f identity/target/docker-compose.yml build
```