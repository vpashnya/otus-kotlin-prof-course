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

# COPY lessons/build.gradle.kts ./lessons/build.gradle.kts
# COPY lessons/gradle.properties ./lessons/gradle.properties
# COPY lessons/settings.gradle.kts ./lessons/settings.gradle.kts

COPY build-plugin/build.gradle.kts ./build-plugin/build.gradle.kts
COPY build-plugin/gradle.properties ./build-plugin/gradle.properties
COPY build-plugin/settings.gradle.kts ./build-plugin/settings.gradle.kts
COPY build-plugin/src ./build-plugin/src

COPY integration-platform/build.gradle.kts ./integration-platform/build.gradle.kts
COPY integration-platform/gradle.properties ./integration-platform/gradle.properties
COPY integration-platform/settings.gradle.kts ./integration-platform/settings.gradle.kts

COPY integration-platform/integration-platform-api-v1/build.gradle.kts ./integration-platform/integration-platform-api-v1/build.gradle.kts
COPY integration-platform/integration-platform-api-v1/src ./integration-platform/integration-platform-api-v1/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-api-v1:build

COPY integration-platform/integration-platform-common/build.gradle.kts ./integration-platform/integration-platform-common/build.gradle.kts
COPY integration-platform/integration-platform-common/src ./integration-platform/integration-platform-common/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-common:build

COPY integration-platform/integration-platform-api-v1-mappers/build.gradle.kts ./integration-platform/integration-platform-api-v1-mappers/build.gradle.kts
COPY integration-platform/integration-platform-api-v1-mappers/src ./integration-platform/integration-platform-api-v1-mappers/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-api-v1-mappers:build

COPY integration-platform/integration-platform-lib-cor/build.gradle.kts ./integration-platform/integration-platform-lib-cor/build.gradle.kts
COPY integration-platform/integration-platform-lib-cor/src ./integration-platform/integration-platform-lib-cor/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-lib-cor:build

COPY integration-platform/integration-platform-business-logic/build.gradle.kts ./integration-platform/integration-platform-business-logic/build.gradle.kts
COPY integration-platform/integration-platform-business-logic/src ./integration-platform/integration-platform-business-logic/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-business-logic:build

COPY integration-platform/integration-platform-repo-tests/build.gradle.kts ./integration-platform/integration-platform-repo-tests/build.gradle.kts
COPY integration-platform/integration-platform-repo-tests/src ./integration-platform/integration-platform-repo-tests/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-tests:build

COPY integration-platform/integration-platform-repo-inmemory/build.gradle.kts ./integration-platform/integration-platform-repo-inmemory/build.gradle.kts
COPY integration-platform/integration-platform-repo-inmemory/src ./integration-platform/integration-platform-repo-inmemory/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-inmemory:build

COPY integration-platform/integration-platform-repo-pg/build.gradle.kts ./integration-platform/integration-platform-repo-pg/build.gradle.kts
COPY integration-platform/integration-platform-repo-pg/src ./integration-platform/integration-platform-repo-pg/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-repo-pg:build

COPY integration-platform/integration-platform-app-kafka/build.gradle.kts ./integration-platform/integration-platform-app-kafka/build.gradle.kts
COPY integration-platform/integration-platform-app-kafka/src ./integration-platform/integration-platform-app-kafka/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-app-kafka:build

COPY integration-platform/integration-platform-app-ktor/build.gradle.kts ./integration-platform/integration-platform-app-ktor/build.gradle.kts
COPY integration-platform/integration-platform-app-ktor/src ./integration-platform/integration-platform-app-ktor/src
RUN ./gradlew --no-daemon integration-platform:integration-platform-app-ktor:build

FROM ${RUNTIME_IMG} AS integration-platform-app-kafka
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-platform-app-kafka/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM ${RUNTIME_IMG} AS integration-platform-app-ktor
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-platform-app-ktor/build/libs/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
