# Курсовой проект "Сервис перевода денег"

## Описание проекта 

* Spring Boot Application "Money Transfer Service" - REST-service - предоставляет интерфейс для перевода денег с одной карты на другую по заранее описанной спецификации.
* Подготовленное веб-приложение (FRONT) подключается к разработанному сервису (BACK) и использует его функционал для перевода денег.

Спецификация: 
https://github.com/netology-code/jd-homeworks/blob/master/diploma/MoneyTransferServiceSpecification.yaml

FRONT доступен по адресу:
* Код: https://github.com/serp-ya/card-transfer
* Демо: https://serp-ya.github.io/card-transfer/

## Сделано:
* разработана архитектура
* использован сборщик пакетов Maven
* реализован весь функционал в соответствии со спецификацией
* протестировано с помощью Postman

## В планах:
* покрытие кода  unit тестами с использованием Mockito
* написание dockerfile, docker-compose файлов
* добавление интеграционных тестов с использованием TestContainers


