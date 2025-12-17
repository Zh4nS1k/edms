# Enterprise Document Management System (EDMS)

Общий контур для существующих микросервисов, приведённых к единому стилю и готовых к совместному запуску. Домен и бизнес-логика не менялись — адаптированы только конфигурации (имена, порты, переменные окружения, Docker).

## Сервисы и роли
- `edms-identity-service` (Identity Service): управление пользователями/ролями, интеграция с Keycloak. Порт `8082`.
- `edms-workflow-service` (Workflow Service): задачи/оркестрация, обращается к Identity через OpenFeign. Порт `8083`.
- `edms-document-service` (Document Service): файлы и метаданные через MinIO + PostgreSQL. Порт `8084`.
- Инфраструктура: `postgres` (отдельные БД под каждый сервис), `minio`, `rabbitmq`, `redis`, `keycloak` (dev-стенд). `rabbit/` — примеры обмена сообщениями; `redis/` — примеры кэш/сессии.

## Конфигурации (единый формат)
Все сервисы переведены на переменные окружения с дефолтами в `application.properties`:
- `SERVER_PORT` — порт сервиса.
- `<SERVICE>_DB_URL/USERNAME/PASSWORD` — строка подключения и креды Postgres.
- `USER_SERVICE_URL` — адрес user-service для feign-клиента (используется task-service).
- `KEYCLOAK_URL` / `KEYCLOAK_REALM` / `KEYCLOAK_CLIENT_ID` / `KEYCLOAK_CLIENT_SECRET` — настройки Keycloak.
- `MINIO_URL` / `MINIO_USER` / `MINIO_PASSWORD` / `MINIO_BUCKET` — настройки MinIO (file-service).

## Docker Compose
Создан единый `docker-compose.yml`, который поднимает БД, MinIO, Redis, RabbitMQ, Keycloak и сами сервисы с согласованными портами.

Запуск:
```bash
docker compose up -d --build
```

Порты хоста (по умолчанию):
- identity-service: `8082`
- workflow-service: `8083`
- document-service: `8084`
- Keycloak: `8081` (URL для сервисов `http://keycloak:8080/auth`)
- MinIO API/Console: `9000` / `9001`
- RabbitMQ UI: `15672`
- Redis: `6379`
- Postgres: `5433` (user), `5434` (task), `5435` (file)

Примечания:
- Реалм/клиент Keycloak нужно заполнить под EDMS (плейсхолдеры оставлены в переменных).
- Значения по умолчанию в `application.properties` сохранили старые локальные настройки — для compose используются переменные окружения.

## Что сделано для унификации
- Имена `spring.application.name` приведены к формату `edms-<service>-service`.
- Порты разнесены, чтобы исключить конфликты: 8082 (user), 8083 (task), 8084 (file).
- Конфигурации вынесены в переменные окружения (DB, MinIO, Keycloak, feign), добавлен единый docker-compose для совместного запуска.

Дальнейшие шаги: настроить realm/клиенты в Keycloak, при необходимости добавить API Gateway и централизованный лог/метрики, затем подключить оставшиеся инфраструктурные решения из примеров (`rabbit/`, `redis/`) в основной compose или Helm-чарты.
