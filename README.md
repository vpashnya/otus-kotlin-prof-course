# otus-kotlin-prof-course

Integration Platform - интеграционная платформа для банковского приложения "Древний Монолит" версия 2.

Задача платформы - интеграция корпоративного приложения с внешними системами.

## Визуальная схема фронтенда
TODO
[//]: # (![Макет фронта]&#40;imgs/design-layout.png&#41;)

## Документация

1. Маркетинг и аналитика
   1. [Целевая аудитория](docs/01-biz/01-target-audience.md)
   2. [Заинтересанты](docs/01-biz/02-stakeholders.md)
   3. [Пользовательские истории](docs/01-biz/03-bizreq.md)
2. Аналитика:

   1. [Функциональные требования](./docs/02-analysis/01-functional-requiremens.md)
   2. [Нефункциональные требования](./docs/02-analysis/02-nonfunctional-requirements.md)

3. Архитектура

[//]: # (   1. [ADR]&#40;docs/03-architecture/01-adrs.md&#41;)
[//]: # (   2. [Описание API]&#40;docs/03-architecture/02-api.md&#41;)
[//]: # (   3. [Архитектурные схемы]&#40;docs/03-architecture/03-arch.md&#41;)


# Структура проекта

## Проект интеграционная платформа
1. [integration-module](integration-platform/integration-module) - Модуль для приложения "Древний Монолит"
2. [integration-service](integration-platform/integration-service) - Интеграционный сервис

## Gradle плагины
1. Модуль 2: Расширенные возможности Kotlin<br>
   1. [BuildPluginJvm.kt](build-plugin/src/main/kotlin/BuildPluginJvm.kt) - Плагин для сборки проектов JVM
   2. [BuildPluginMultiplatform.kt](build-plugin/src/main/kotlin/BuildPluginMultiplatform.kt) - Плагин для сборки мультиплатформенных проектов 

## Подпроекты для занятий по языку Kotlin

1. Модуль 1: Введение в Kotlin
    1. [m1l1-first](lessons/m1l1-first) - Вводное занятие, создание первой программы на Kotlin
    2. [m1l2-basic](lessons/m1l2-basic) - Основные конструкции Kotlin
    3. [m1l3-func](lessons/m1l3-func) - Функциональные элементы Kotlin
    4. [m1l4-oop](lessons/m1l4-oop) - Объектно-ориентированное программирование
2. Модуль 2: Расширенные возможности Kotlin
    1. [m2l1-dsl](lessons/m2l1-dsl) - Предметно ориентированные языки (DSL)
    2. [m2l2-coroutines](lessons/m2l2-coroutines) - Асинхронное и многопоточное программирование с корутинами
    3. [m2l3-flows](lessons/m2l3-flows) - Асинхронное и многопоточное программирование с Sequence и Flow
    4. [m2l4-kmp](lessons/m2l4-kmp) - Мультиплатформенная разработка
