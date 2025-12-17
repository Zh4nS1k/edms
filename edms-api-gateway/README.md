# API Gateway (EDMS)

Фронтовой слой EDMS на Spring Cloud Gateway. Проверяет JWT из Keycloak realm `edms` и проксирует запросы в сервисы.

## Основное
- Порт по умолчанию: `8080`.
- Стек: Spring Cloud Gateway, Spring Security OAuth2 Resource Server.
- Маршруты: `/identity/**` → Identity, `/workflow/**` → Workflow, `/document/**` → Document. `StripPrefix=1` убирает префикс при проксировании.

## Конфигурация
- `SERVER_PORT` — порт.
- `IDENTITY_SERVICE_URL`, `WORKFLOW_SERVICE_URL`, `DOCUMENT_SERVICE_URL` — адреса бэкендов.
- `KEYCLOAK_URL`, `KEYCLOAK_REALM` — issuer для проверки JWT (по умолчанию realm `edms`).

## Запуск
Локально:
```bash
./gradlew bootRun
```

Через docker compose:
```bash
docker compose up -d --build api-gateway
```

Здоровье: `GET http://localhost:8080/actuator/health` (без токена). Все остальные маршруты требуют валидный JWT realm `edms`.
