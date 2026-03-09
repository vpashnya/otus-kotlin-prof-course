Создать сообщения
```shell
echo '{"requestType":"create","debug":{"mode":"stub","stub":"success"},"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС"}}' | 
sudo docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"update","requestType":null,"debug":null,"stream":{"classShortName":"CLIENT","methodShortName":"EXPORT2FNS","transportParams":"some transport","description":"Отправка информации в ФНС","id":"12345"}}' |
sudo docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```

```shell
echo '{"requestType":"delete","requestType":null,"debug":null,"streamId":"12345"}' |
sudo docker exec -i kafka1 /usr/bin/kafka-console-producer --topic ip.stream.v1.in --bootstrap-server kafka1:9092
```
