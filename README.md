# Система управления банковскими картами

Это Spring Boot REST API для работы с банковскими картами. Проект собран на Maven и запускается в Docker-контейнерах (приложение + PostgreSQL + pgAdmin).

---
## Содержание

1. [Предварительные требования](#предварительные-требования)
2. [Структура проекта](#структура-проекта)
3. [Генерация OpenAPI спецификации](#генерация-openapi-спецификации)
4. [Быстрый старт с Docker Compose](#быстрый-старт-с-docker-compose)
5. [Локальный запуск без Docker (профиль `dev`)](#локальный-запуск-без-docker-профиль-dev)
6. [Документация API](#документация-api)

---

## Предварительные требования

- Установленный [Docker](https://docs.docker.com/get-docker/)  
- Установленный [Docker Compose](https://docs.docker.com/compose/install/)  
- (При локальном запуске) Java 17+ и Maven 3.6+ 

---

## Структура проекта

```
Bank_REST
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
├── docs/
│   └── openapi.yaml
└── src/
    ├── main/
    │   ├── java/com/example/bankcards/… 
    │   └── resources/
    │       ├── application.yml
    │       └── application-dev.yml
    └── test/
        └── java/…
```

---

## Быстрый старт с Docker Compose

1. **Клонировать репозиторий и перейти в директорию проекта**  
   ```bash
   git clone <URL_репозитория>
   cd Bank_REST
   ```

2. **Собрать образы и запустить контейнеры**  
   ```bash
   docker-compose up --build
   ```

3. **Проверить сервисы**  
   - API: `http://localhost:8060`  
   - pgAdmin: `http://localhost:5433`  
     — логин: `BankRest`, пароль: `BankRest`

### Состав контейнеров

| Сервис       | Образ               | Порт (хост → контейнер) | Описание                               |
|--------------|---------------------|-------------------------|----------------------------------------|
| **postgres** | `postgres:16`       | `5432 → 5432`           | PostgreSQL с БД `BankRest`             |
| **pgadmin**  | `dpage/pgadmin4` | `5433 → 80`             | Веб-интерфейс для управления PostgreSQL |
| **app**      | `bank-rest:latest`  | `8060 → 8060`           | Spring Boot приложение                 |

---

## Локальный запуск без Docker

1. Убедитесь, что установлены Java 17+ и Maven 3.6+ (или используйте Maven Wrapper).  
2. В каталоге проекта выполните:

   ```bash
   # на Unix/macOS
   ./mvnw clean package
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

   # на Windows PowerShell
   .\mvnw.cmd clean package
   .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. Перейдите в браузере по адресу:  
   `http://localhost:8060`
---

## Генерация OpenAPI спецификации

Чтобы сгенерировать файл `docs/openapi.yaml`, выполните в корне проекта:

```bash
# при наличии глобального Maven
mvn verify

# или с Maven Wrapper
./mvnw verify
```


## Документация API

- **OpenAPI (YAML):** `docs/openapi.yaml`  
- **Swagger UI:** `http://localhost:8060/swagger-ui.html`

---

