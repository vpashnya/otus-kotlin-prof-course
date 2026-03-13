## Сборка проекта 
`docker build . -t service`
## Запуск проекта 
`docker run service`

# Параметры среды (пример)
MODE=STUB
#KAFKA_HOSTS="127.0.0.1:9092,127.0.0.1:9192"
KAFKA_HOSTS="127.0.0.1:9092"
KAFKA_GROUP_ID="integration-platform"
KAFKA_IP_STREAM_TOPIC_V1_IN="ip.stream.v1.in"
KAFKA_IP_STREAM_TOPIC_V1_OUT="ip.stream.v1.out"
