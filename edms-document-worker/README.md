# EDMS Document Worker

RabbitMQ worker, который принимает события о новых документах, тянет файл из MinIO, считает метаданные (тип, размер, SHA-256) и сохраняет результат в PostgreSQL.

## Поток
1) `document-service` публикует событие `document.uploaded` в exchange `edms.document.events`.
2) Worker слушает очередь `edms.document.process.queue`, получает payload `{id, fileName, originalName, mimeType, size, addedTime}`.
3) Worker скачивает файл из MinIO, вычисляет SHA-256 и пишет результат в таблицу `document_processing_results`.

## Конфигурация
- RabbitMQ: `DOCUMENT_EXCHANGE`, `DOCUMENT_QUEUE`, `DOCUMENT_ROUTING_KEY`, `RABBITMQ_*`.
- MinIO: `MINIO_URL`, `MINIO_USER`, `MINIO_PASSWORD`, `MINIO_BUCKET`.
- Postgres: `WORKER_DB_URL`, `WORKER_DB_USERNAME`, `WORKER_DB_PASSWORD`.
- Порт по умолчанию: `8091`.

## Запуск (docker compose)
```bash
docker compose up -d --build document-worker
```
Worker зависит от RabbitMQ, MinIO и своей БД (`document-worker-db` в compose).
