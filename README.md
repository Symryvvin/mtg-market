```shell
kubectl create namespace mtg
kubectl config set-context --current --namespace mtg
```

```shell
helm install mtg-db bitnami/postgresql -f search/deploy/database/values.yml
```

