<p align="center">
  <picture>
    <img alt="Java Game Box icon" src="./src/main/resources/icon.png" width=25% height=25%>
  </picture>
</p>
<h1 align="center">Java Game Box</h1>

[![Releases](https://img.shields.io/github/v/release/IvanNovR/JavaGameBox.svg)](https://github.com/IvanNovR/JavaGameBox/releases)
[![Status](https://img.shields.io/github/actions/workflow/status/IvanNovR/JavaGameBox/maven.yml.svg)](#)
[![License](https://img.shields.io/badge/license-GPL%20V3-blue.svg?longCache=true)](https://www.gnu.org/licenses/gpl-3.0.en.html)
[![English Readme](https://img.shields.io/badge/english-readme-blue)](/README.md)

JavaGameBox — это простая игровая платформа, которая позволяет пользователям играть в классические игры. Платформа поддерживает аутентификацию пользователей, отслеживание очков и систему лидеров.

## Структура проекта

Проект организован следующим образом:
- `src/main/java/ru/ivannovr`: Содержит основной код приложения.
    - `games`: Логика для каждой игры.
    - `gui`: Компоненты графического интерфейса.
    - `utils`: Утилитарные классы, включая управление базой данных.
- `src/main/resources`: Содержит ресурсы, такие как иконки и файлы конфигурации.
- `pom.xml`: Файл конфигурации Maven для управления зависимостями и сборки проекта.

## Сборка проекта

Для сборки проекта необходимо установить Maven. Скачать Maven можно с [официального сайта](https://maven.apache.org/download.cgi).

После установки Maven перейдите в корневую директорию проекта и выполните:

```bash
mvn clean package
```

Эта команда скомпилирует код, запустит тесты и упакует приложение в JAR-файл, который будет находиться в директории `target`.

## Запуск проекта

Для запуска проекта требуется установленная Java. Скачать Java можно с [официального сайта](https://www.java.com/en/download/).

Также необходимо установить и запустить PostgreSQL. Скачать PostgreSQL можно с [официального сайта](https://www.postgresql.org/download/).

Перед запуском приложения настройте базу данных:
1. Создайте новую базу данных в PostgreSQL.
2. Укажите URL базы данных, имя пользователя и пароль в аргументах командной строки при запуске JAR-файла.

Запустите приложение с помощью команды:

```bash
java -jar target/JavaGameBox-1.1.1.jar --db-url=<database_url> --db-user=<database_user> --db-password=<database_password>
```

Замените `<database_url>`, `<database_user>` и `<database_password>` на ваши реальные данные для подключения к PostgreSQL.

## Дополнительная информация

- Проект использует Maven для управления зависимостями (подробности в `pom.xml`).
- Логирование реализовано через Log4j, конфигурация находится в `src/main/resources/log4j2.xml`.
- Графический интерфейс построен с использованием Swing.
- Игры реализованы с применением объектно-ориентированных принципов, каждая игра наследуется от класса `AbstractGame`.

Для дополнительной информации обратитесь к исходному коду и комментариям.