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
###### Путь к скриптам:
```
store/src/main/resources/
```
