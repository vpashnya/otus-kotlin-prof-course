## Сборка проекта 
`docker build . -t app-kafka`
## Запуск проекта 
`docker compose -f docker-compose.yaml up -d app-kafka`

## Параметры среды (пример)
MODE=PROD
KAFKA_HOSTS="127.0.0.1:9092"
KAFKA_GROUP_ID="integration-platform"
KAFKA_IP_STREAM_TOPIC_V1_IN="ip.stream.v1.in"
KAFKA_IP_STREAM_TOPIC_V1_OUT="ip.stream.v1.out"
PG_URL=jdbc:postgresql://localhost:5432/mydatabase
PG_USER=myuser
PG_PASSWORD=mypassword
PG_MAXIMUM_POOL_SIZE=10
PG_MINIMUM_IDLE=5
PG_IDLE_TIMEOUT=60000
PG_CONNECTION_TIMEOUT=30000

## Запросы (примеры)

```shell
echo '{"requestType":"create","debug":{"mode":"stub","stub":"success"},"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС"}}' | 
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"update","requestType":null,"debug":null,"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС","id":"12345","version":"1"}}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"delete","requestType":null,"debug":null,"streamId":"12345"}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"read","requestType":null,"debug":null,"streamId":"12345"}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"enable","requestType":null,"debug":null,"streamId":"12345","version":"1"}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
``` 

```shell
echo '{"requestType":"disable","requestType":null,"debug":null,"streamId":"12345","version":"1"}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"search","requestType":null,"debug":null,"streamFilter":{"searchString":"classShorName = ''KREDS%'' and methodShortName = ''%''  ","classShortName":"DEPOSIT","methodShortName":"OEPN","active":false}}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"accessible","requestType":null,"debug":null,"externalSystemId":null}' |
docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
``` 
