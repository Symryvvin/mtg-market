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
build
```shell
mvn clean install
```
---
#### Служба идентификации
###### database
```shell
helm install identity-db bitnami/postgresql -f deploy/database/identity/values.yml -n mtg
```
###### scripts
```
identity/src/main/resources/sql/create_identity_tables.sql
```
###### docker
```shell
docker-compose -f identity/target/docker-compose.yml build
docker push symryvvin/identity:1.0
```
###### deploy
```shell
kubectl apply -f deploy/identity/application.yml -n mtg
```
---
#### Служба управления магазином
###### database
```shell
helm install store-db bitnami/mongodb -f deploy/database/store/values.yml -n mtg
```
###### docker
```shell
docker-compose -f store/target/docker-compose.yml build
docker push symryvvin/store:1.0
```
###### deploy
```shell
kubectl apply -f deploy/store/application.yml -n mtg
```
---
#### Служба заказов
###### docker
```shell
docker-compose -f order/target/docker-compose.yml build
docker push symryvvin/order:1.0
```
###### deploy
```shell
kubectl apply -f deploy/order/axon-server.yml -n mtg
kubectl apply -f deploy/order/application.yml -n mtg
```
---
#### Служба базы данных карт
###### database
```shell
helm install mtg-db bitnami/postgresql -f deploy/database/search/values.yml -n mtg
```
###### scripts
```
search/src/main/resources/sql/create_card_table.sql
```
###### docker
```shell
docker-compose -f card-db/target/docker-compose.yml build
docker push symryvvin/card-db:1.0
```
###### deploy
```shell
kubectl apply -f deploy/card-db/application.yml -n mtg
```
---


#### Api Gateway
build
```shell
docker-compose -f identity/target/docker-compose.yml build
docker push symryvvin/api-gateway:1.0
```
deploy
```shell
kubectl apply -f deploy/gateway/application.yml -n mtg









```shell
docker-compose -f search/target/docker-compose.yml build
docker-compose -f identity/target/docker-compose.yml build
```