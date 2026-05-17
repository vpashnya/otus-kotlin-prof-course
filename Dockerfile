ARG BUILDER_IMG="bellsoft/liberica-openjdk-alpine:21"
ARG RUNTIME_IMG="bellsoft/liberica-openjre-alpine:21"

FROM ${BUILDER_IMG} AS builder
RUN mkdir -p /app
WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew

COPY gradle.properties ./gradle.properties
COPY settings.gradle.kts ./settings.gradle.kts
COPY specs/specs-integration-platform.yaml ./specs/specs-integration-platform.yaml

COPY build-plugin/build.gradle.kts ./build-plugin/build.gradle.kts
COPY build-plugin/gradle.properties ./build-plugin/gradle.properties
COPY build-plugin/settings.gradle.kts ./build-plugin/settings.gradle.kts
COPY build-plugin/src ./build-plugin/src

COPY integration-platform/build.gradle.kts ./integration-platform/build.gradle.kts
COPY integration-platform/gradle.properties ./integration-platform/gradle.properties
COPY integration-platform/settings.gradle.kts ./integration-platform/settings.gradle.kts

COPY ancient-monolith/build.gradle.kts ./ancient-monolith/build.gradle.kts
COPY ancient-monolith/gradle.properties ./ancient-monolith/gradle.properties
COPY ancient-monolith/settings.gradle.kts ./ancient-monolith/settings.gradle.kts
COPY ancient-monolith/src ./ancient-monolith/src

COPY integration-platform/integration-platform-api-v1/build.gradle.kts ./integration-platform/integration-platform-api-v1/build.gradle.kts
COPY integration-platform/integration-platform-api-v1/src ./integration-platform/integration-platform-api-v1/src

COPY integration-platform/integration-platform-common/build.gradle.kts ./integration-platform/integration-platform-common/build.gradle.kts
COPY integration-platform/integration-platform-common/src ./integration-platform/integration-platform-common/src

COPY integration-platform/integration-platform-api-v1-mappers/build.gradle.kts ./integration-platform/integration-platform-api-v1-mappers/build.gradle.kts
COPY integration-platform/integration-platform-api-v1-mappers/src ./integration-platform/integration-platform-api-v1-mappers/src

COPY integration-platform/integration-platform-lib-cor/build.gradle.kts ./integration-platform/integration-platform-lib-cor/build.gradle.kts
COPY integration-platform/integration-platform-lib-cor/src ./integration-platform/integration-platform-lib-cor/src

COPY integration-platform/integration-platform-business-logic/build.gradle.kts ./integration-platform/integration-platform-business-logic/build.gradle.kts
COPY integration-platform/integration-platform-business-logic/src ./integration-platform/integration-platform-business-logic/src

COPY integration-platform/metadata-actualizer/build.gradle.kts ./integration-platform/metadata-actualizer/build.gradle.kts
COPY integration-platform/metadata-actualizer/src ./integration-platform/metadata-actualizer/src

COPY integration-platform/integration-platform-repo-tests/build.gradle.kts ./integration-platform/integration-platform-repo-tests/build.gradle.kts
COPY integration-platform/integration-platform-repo-tests/src ./integration-platform/integration-platform-repo-tests/src

COPY integration-platform/integration-platform-repo-inmemory/build.gradle.kts ./integration-platform/integration-platform-repo-inmemory/build.gradle.kts
COPY integration-platform/integration-platform-repo-inmemory/src ./integration-platform/integration-platform-repo-inmemory/src

COPY integration-platform/integration-platform-repo-pg/build.gradle.kts ./integration-platform/integration-platform-repo-pg/build.gradle.kts
COPY integration-platform/integration-platform-repo-pg/src ./integration-platform/integration-platform-repo-pg/src

COPY integration-platform/integration-platform-app-kafka/build.gradle.kts ./integration-platform/integration-platform-app-kafka/build.gradle.kts
COPY integration-platform/integration-platform-app-kafka/src ./integration-platform/integration-platform-app-kafka/src

COPY integration-platform/integration-platform-app-ktor/build.gradle.kts ./integration-platform/integration-platform-app-ktor/build.gradle.kts
COPY integration-platform/integration-platform-app-ktor/src ./integration-platform/integration-platform-app-ktor/src

COPY integration-platform/integration-processor-kafka/build.gradle.kts ./integration-platform/integration-processor-kafka/build.gradle.kts
COPY integration-platform/integration-processor-kafka/src ./integration-platform/integration-processor-kafka/src




COPY integration-platform/testing-machine/build.gradle.kts ./integration-platform/testing-machine/build.gradle.kts
COPY integration-platform/testing-machine/src ./integration-platform/testing-machine/src

RUN ./gradlew --no-daemon ancient-monolith:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-api-v1:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-common:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-api-v1-mappers:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-lib-cor:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-business-logic:build
RUN ./gradlew --no-daemon integration-platform:metadata-actualizer:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-tests:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-inmemory:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-pg:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-app-kafka:build
RUN ./gradlew --no-daemon integration-platform:integration-platform-app-ktor:build
RUN ./gradlew --no-daemon integration-platform:integration-processor-kafka:build
RUN ./gradlew --no-daemon integration-platform:testing-machine:build

FROM ${RUNTIME_IMG} AS ancient-monolith
WORKDIR /opt/app
COPY --from=builder /app/ancient-monolith/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM ${RUNTIME_IMG} AS integration-platform-app-kafka
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-platform-app-kafka/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM ${RUNTIME_IMG} AS integration-platform-app-ktor
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-platform-app-ktor/build/libs/*.jar /app.jar
RUN apk add --no-cache curl
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM ${RUNTIME_IMG} AS integration-processor-kafka
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-processor-kafka/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]


FROM ${RUNTIME_IMG} AS testing-machine
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/testing-machine/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

