ARG BUILDER_IMG="bellsoft/liberica-openjdk-alpine:21"
ARG RUNTIME_IMG="bellsoft/liberica-openjre-alpine:21"

FROM ${BUILDER_IMG} AS builder
RUN mkdir -p /app
WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew

COPY gradle.properties ./gradle.properties
COPY settings.gradle.kts ./settings.gradle.kts

COPY integration-platform/build.gradle.kts ./integration-platform/build.gradle.kts
COPY integration-platform/gradle.properties ./integration-platform/gradle.properties
COPY integration-platform/settings.gradle.kts ./integration-platform/settings.gradle.kts


COPY lessons/build.gradle.kts ./lessons/build.gradle.kts
COPY lessons/gradle.properties ./lessons/gradle.properties
COPY lessons/settings.gradle.kts ./lessons/settings.gradle.kts

COPY build-plugin/build.gradle.kts ./build-plugin/build.gradle.kts
COPY build-plugin/gradle.properties ./build-plugin/gradle.properties
COPY build-plugin/settings.gradle.kts ./build-plugin/settings.gradle.kts
COPY build-plugin/src ./build-plugin/src

COPY integration-platform/integration-service/build.gradle.kts ./integration-platform/integration-service/build.gradle.kts
COPY integration-platform/integration-service/src ./integration-platform/integration-service/src

RUN ./gradlew --no-daemon integration-platform:integration-service:build

FROM ${RUNTIME_IMG}
WORKDIR /opt/app
COPY --from=builder /app/integration-platform/integration-service/build/libs/*.jar /opt/app/app.jar
ENTRYPOINT ["java", "-cp", "/opt/app/app.jar", "ru.pvn.learning.MainKt"]
