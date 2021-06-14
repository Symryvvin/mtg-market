# dev

Запуск axon-server в docker
```shell
docker run -d --name order-event-store -p 8024:8024 -p 8124:8124 axoniq/axonserver
```


# prod

##### kubernetes namespace
```shell
kubectl create namespace mtg
kubectl config set-context --current --namespace mtg
```

#### Служба идентификации
build
```shell
mvn clean install
docker-compose -f identity/target/docker-compose.yml build
docker push symryvvin/identity:1.0
```
deploy app
```shell
kubectl apply -f deploy/identity/application.yml -n mtg
```


install db
```shell
helm install store-db bitnami/mongodb -f deploy/database/store/values.yml -n mtg
```



##### Установка базы данных для службы поиска (Search Service)
```shell
helm install mtg-db bitnami/postgresql -f deploy/database/search/values.yml -n mtg
```
###### Путь к скриптам:
```
search/src/main/resources/sql/create_card_table.sql
```

##### Установка базы данных для службы идентификации (Identity Service)
```shell
helm install identity-db bitnami/postgresql -f deploy/database/identity/values.yml -n mtg
```
###### Путь к скриптам:
```
identity/src/main/resources/sql/create_identity_tables.sql
```

##### Установка базы данных для службы управления коллекциями и магазинами (Store Service)
```shell
helm install store-db bitnami/mongodb -f deploy/database/store/values.yml -n mtg
```














```shell
docker-compose -f search/target/docker-compose.yml build
docker-compose -f identity/target/docker-compose.yml build
```