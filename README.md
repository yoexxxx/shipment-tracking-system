## TRACKING SHIPMENT-EVENT SIMULATION SYSTEM

---

### Technology Stack:

### Core:

#### Java
* **Version:** `21`
* **Role in the project:** Main programming language used for backend development and system logic implementation

#### Spring Boot
* **Version:** `4.0.0`
* **Role in the project:** Primary backend framework used for building RESTapi, scheduling tasks, and managing application lifecycle

---

### Messaging:

#### Apache Kafka
* **Version:** latest (Confluent platform / Apache Kafka)
* **Role in the project:** Event-driven communication between services, handling asynchronous shipment processing and state transitions

#### Spring Kafka
* **Version:** compatible with Spring Boot version
* **Role in the project:** Integration layer for producing and consuming Kafka messages, retry mechanisms, and DLT handling

---

### Persistence:

#### PostgreSQL
* **Version:** `16`
* **Role in the project:** Main relational database for storing shipment, cargo, route, and related entities

#### Spring Data JPA (Hibernate)
* **Version:** compatible with Spring Boot version
* **Role in the project:** ORM layer for database interaction and entity management

---

### Mapping:

#### MapStruct
* **Version:** `1.6.3`
* **Role in the project:** Compile-time generation of DTO ↔ Entity mappings to reduce boilerplate code

---

### Infrastructure:

#### Docker / Docker Compose
* **Role in the project:** Containerization of services and local environment orchestration (PostgreSQL, Apache Kafka & KafkaUI, microservices)

---

### Logging:

#### Logback
* **Version:** `1.5.21`
* **Role in the project:** Logging framework for tracking application flow, debugging, and monitoring system behavior
---

---

## Общая архитектура и идея проекта

Данный проект представляет собой демонстрационную систему, реализующую подход **event-driven architecture (EDA)** на основе Apache Kafka. Основная цель проекта — смоделировать поток обработки событий в распределённой системе, где данные проходят через несколько стадий жизненного цикла и обрабатываются асинхронно.
В качестве доменной области выбрано **отслеживание отправлений (Shipment Tracking Simulation)**. Система генерирует события, описывающие изменение состояния отправлений, и обрабатывает их через событийный поток, имитируя поведение реальной логистической системы.

Проект построен как набор взаимодействующих компонентов:

---

### Common module
Общий модуль, содержащий переиспользуемые компоненты системы:
- `GeneratorUtils` — утилитный класс, использующий библиотеку `Java Faker (version 1.0.2)` для генерации тестовых данных
- DTO-модели — общие модели данных, используемые для передачи событий между сервисами

Назначение модуля — исключить дублирование кода и обеспечить единый контракт данных между сервисами.

---

### Event Generator Service
Сервис-генератор событий, отвечающий за:
- создание тестовых `Shipment` событий
- моделирование жизненного цикла отправлений (изменение статусов перемещения)
- публикацию событий в Apache Kafka

Данный сервис имитирует источник данных в распределённой системе.

---

### Event Handler Service
Сервис обработки событий, выполняющий:
- потребление событий из Kafka-топиков
- обработку бизнес-логики изменения состояния отправлений
- сохранение и обновление данных в PostgreSQL
- предоставление REST API для получения актуального состояния данных

Также включает обработку ошибок, retry-механизмы и Dead Letter Topic (DLT) для некорректных сообщений.

---

### PostgreSQL
Реляционная база данных, используемая для:
- хранения текущего состояния отправлений
- хранения связанных сущностей (cargo, route, driver, vehicle)
- обеспечения консистентности данных после обработки событий

---

### REST API
REST интерфейс, предоставляемый event-handler сервисом:
- получение актуального состояния отправлений
- доступ к агрегированным данным через entity graph
- используется для проверки результата обработки событий

---

### Apache Kafka
Центральный компонент системы, обеспечивающий:
- асинхронную передачу событий между сервисами
- реализацию retry-механизмов
- обработку ошибок через DLT (Dead Letter Topic)
- разделение генерации и обработки данных

---

## Особенности технической реализации сервисов:
### Event-Generator-Service:

Сервис генерации событий построен как отдельный producer компонент EDA системы и отвечает за моделирование потока событий при перемещении грузовых отправлений. Основной задачей сервиса является генерация составных `ShipmentData` событий, управление их жизненным циклом и публикация изменений в Apache Kafka.

Центральным компонентом генерации выступает [ShipmentDataGenerator](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/generator/ShipmentDataGenerator.java), который формирует агрегированную DTO-модель отправления, объединяя данные из специализированных sub-generator компонентов:
[CargoDataGenerator](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/generator/subgenerator/CargoDataGenerator.java),
[DriverDataGenerator](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/generator/subgenerator/DriverDataGenerator.java),
[RouteDataGenerator](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/generator/subgenerator/RouteDataGenerator.java) и
[VehicleDataGenerator](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/generator/subgenerator/VehicleDataGenerator.java).

Подобный подход позволяет разделить ответственность между генераторами по доменным областям и избежать перегруженности единой точки генерации данных. Для создания тестовых значений используется общий utility-класс [GeneratorUtils](./common/src/main/java/utils/GeneratorUtils.java), инкапсулирующий работу с `Java Faker`, генерацию идентификаторов, маршрутов, регистрационных номеров и прочих вспомогательных данных.

Для упрощения и централизации сквозной логики логирования в проекте осознанно используется AOP. В рамках данного подхода была реализована кастомная аннотация [@GenerateData](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/annotation/GenerateData.java), выступающая условным контрактом для методов генерации данных.

На основе данной аннотации реализован pointcut в [GeneratorPointcuts](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/aspect/pointcut/GeneratorPointcuts.java) и аспект [GeneratorLoggingAspect](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/aspect/GeneratorLoggingAspect.java), который автоматически перехватывает вызовы методов генерации, логирует факт создания данных, а при debug-уровне сериализует результат в JSON через `ObjectMapper`.
Подобный подход позволил вынести logging logic из основной бизнес-логики генераторов, уменьшить дублирование кода и упростить дальнейшее масштабирование системы.

За публикацию событий в Apache Kafka отвечает [ShipmentPublisherService](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/service/ShipmentPublisherService.java), использующий `KafkaTemplate` и producer-конфигурацию из [KafkaProducerConfig](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/config/KafkaProducerConfig.java).
Для сериализации DTO-моделей используется `JacksonJsonSerializer`, а в качестве message key используется `shipmentId`, что позволяет сохранять консистентность обработки событий.

Для повышения устойчивости producer-side логики реализован retry-механизм на отправку сообщений. Метод публикации помечен аннотацией `@Retryable` и при возникновении `KafkaException` выполняет повторные попытки отправки с использованием exponential backoff (`1s → 2s → 4s`).
После исчерпания количества попыток вызывается `@Recover` метод, выполняющий финальное логирование ошибки. Подобный подход позволяет корректно обрабатывать временные инфраструктурные проблемы Kafka без прерывания работы всей системы.

Обновление состояния event-отправлений выполняет [ShipmentLifecycleService](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/service/ShipmentLifecycleService.java), реализующий упрощённую модель жизненного цикла отправления через карту переходов состояний (`shipmentLifecycleMap`).
Сервис переводит отправление между статусами:

CREATED → ENTRY_TO_COUNTRY → IN_TRANSIT → LEFT_THE_COUNTRY → DELIVERED

При каждом обновлении изменяется статус отправления, время обновления и текущая точка маршрута (`currentPoint`). После достижения статуса `DELIVERED` отправление считается завершённым.

Оркестрацией всего процесса генерации и обновления событий занимается [ShipmentOrchestratorService](./data-generator-service/src/main/java/ru/filimonov/datageneratorservice/service/orchestrator/ShipmentOrchestratorService.java).
После старта приложения сервис инициализирует набор тестовых отправлений, сохраняет их в `ConcurrentHashMap` для локального хранения актуальных событий, и публикует события в Kafka. Далее через механизм шедурела `@Scheduled` запускается периодическое обновление активных отправлений с последующей повторной отправкой обновлённых событий в Kafka-топик.

По мере достижения всеми отправлениями финального статуса `DELIVERED`, `Map` актуальных событий для обработки очищается и сервис автоматически завершает свою работу через `SpringApplication.exit(...)`, после проверки факта отсутствия актуальных событий.
Подобный подход позволяет моделировать завершённый event-pipeline без постоянной фоновой нагрузки на систему после окончания генерации данных.

### Event-Handler-Service:

Обработка событий в системе реализована через [ShipmentConsumerService](src/main/java/ru/filimonov/eventhandlerservice/service/ShipmentConsumerService.java), который получает сообщения из Kafka-топика при помощи `@KafkaListener`.

Для повышения отказоустойчивости системы используется механизм `@RetryableTopic`.  
При временных инфраструктурных сбоях (например, недоступности PostgreSQL или проблемах сети) сообщение автоматически переотправляется на повторную обработку с использованием exponential backoff.

После исчерпания всех попыток обработки сообщение отправляется в Dead Letter Topic (DLT), а факт отправки фиксируется через `@DltHandler`.  
Подобный подход позволяет не блокировать основной поток обработки проблемными сообщениями и моделирует поведение продуктивной event-driven системы.

Основная бизнес-логика данного сервиса сосредоточена в [ShipmentService](src/main/java/ru/filimonov/eventhandlerservice/service/ShipmentService.java).  
Сервис реализует подход "create-or-update": если отправление уже существует в БД — выполняется обновление сущности, иначе создаётся новая запись.

Для хранения данных используется PostgreSQL и набор связанных JPA-сущностей:

- [Shipment](src/main/java/ru/filimonov/eventhandlerservice/entity/Shipment.java)
- [Cargo](src/main/java/ru/filimonov/eventhandlerservice/entity/Cargo.java)
- [Driver](src/main/java/ru/filimonov/eventhandlerservice/entity/Driver.java)
- [Route](src/main/java/ru/filimonov/eventhandlerservice/entity/Route.java)
- [Vehicle](src/main/java/ru/filimonov/eventhandlerservice/entity/Vehicle.java)

Сущность `Shipment` агрегирует остальные сущности через связи `@OneToOne` с `cascade = CascadeType.ALL`, что позволяет обновлять связанные данные в рамках одной транзакции.

Для загрузки связанных сущностей используется [ShipmentRepository](src/main/java/ru/filimonov/eventhandlerservice/repository/ShipmentRepository.java) и механизм `@EntityGraph`, позволяющий избежать проблемы `N+1 select` при получении полного объекта отправления вместе со всеми зависимостями.

Преобразование DTO ↔ Entity реализовано через [MapStruct](https://mapstruct.org/) и набор mapper-компонентов:

- [ShipmentMapper](src/main/java/ru/filimonov/eventhandlerservice/mapper/ShipmentMapper.java)
- [CargoMapper](src/main/java/ru/filimonov/eventhandlerservice/mapper/submapper/CargoMapper.java)
- [DriverMapper](src/main/java/ru/filimonov/eventhandlerservice/mapper/submapper/DriverMapper.java)
- [RouteMapper](src/main/java/ru/filimonov/eventhandlerservice/mapper/submapper/RouteMapper.java)
- [VehicleMapper](src/main/java/ru/filimonov/eventhandlerservice/mapper/submapper/VehicleMapper.java)

MapStruct используется для compile-time генерации типобезопасного маппинга без написания большого количества бойлерплейта.

Для внешнего взаимодействия с системой реализован RESTapi посредством [ShipmentController](src/main/java/ru/filimonov/eventhandlerservice/controller/ShipmentController.java), предоставляющий возможность:

- получить информацию по конкретному отправлению;
- получить список всех доступных отправлений для отслеживания;
- удалить отправление.

API документирован через OpenAPI / Swagger-аннотации (`@Operation`, `@ApiResponses`, `@Schema`), что позволяет автоматически формировать интерактивную документацию и упрощает интеграцию с frontend или внешними сервисами.

Обработка ошибок REST-уровня вынесена в [GlobalExceptionHandler](src/main/java/ru/filimonov/eventhandlerservice/exceptionhandler/GlobalExceptionHandler.java), реализованный через `@RestControllerAdvice`.

Глобальный обработчик централизованно перехватывает:
- `EntityNotFoundException`
- `ConstraintViolationException`

и формирует единый формат серверных ошибок через DTO [ErrorResponse](src/main/java/ru/filimonov/eventhandlerservice/exceptionhandler/dto/response/ErrorResponse.java).

В результате сервис моделирует полноценный backend-компонент event-driven архитектуры: получает поток событий из Kafka, обрабатывает и сохраняет данные в PostgreSQL, обеспечивает отказоустойчивость через retry/DLT механизмы и предоставляет агрегированные данные посредством RESTapi.

---
## Способ запуска и тестирование системы:

Для упрощения запуска и развёртывания вся система контейнеризирована при помощи Docker.  
Каждый микросервис собирается в отдельный Docker-образ, что позволяет запускать систему независимо от локального окружения и установленных зависимостей на хостовой машине.

Оркестрация контейнеров выполняется через Docker Compose.  
В состав `docker-compose.yml` входят следующие компоненты:

- `event-generator-service` — сервис генерации и отправки событий;
- `event-handler-service` — сервис обработки событий и REST API;
- `postgres` — база данных PostgreSQL для хранения информации об отправлениях;
- `kafka` — брокер сообщений Apache Kafka;
- `kafka-ui` — сервис для отслеживания состояния брокера.

Docker Compose автоматически создаёт общую внутреннюю сеть между контейнерами, благодаря чему сервисы могут взаимодействовать друг с другом по именам контейнеров без дополнительной настройки сети.

Для запуска всей системы необходимо выполнить команду:

```bash
docker compose up --build
```

Параметр `--build` принудительно пересобирает Docker-образы перед запуском контейнеров, что особенно полезно после внесения изменений в код сервисов.

После успешного запуска:

- Kafka начинает принимать события от `event-generator-service`;
- `event-handler-service` начинает обработку и сохранение данных в PostgreSQL;
- REST API становится достижимым для тестирования;
- `kafka-ui` предоставляет графичесикй интерфейс для отслеживания состояния брокера;
- OpenAPI/Swagger документация автоматически поднимается вместе с сервисом.

Таким образом, вся система запускается одной командой и полностью готова к локальному тестированию.

### Тестрирование RESTapi:

В рамках тестирования REST API возможно выполнить проверку корректности получения, удаления и отображения данных об отправлениях, сохранённых в PostgreSQL после обработки Kafka-событий.

Тестирование возможно производить при помощи HTTP-клиента Insomnia (или любого удобного), посредством отправки запросов к REST endpoint-ам сервиса `event-handler-service`.

Список доступных эндпоинтов (на данный момент):

1. Получение информации по конкретному отправлению:
```http
GET /api/shipments/{shipmentId}
```

2. Получение общей информации по всем отправлениям:
```http
GET /api/shipments/all
```

3. Удаление неактивного отправления (после достижения статуса `DELIVERED`):
```http
DELETE /api/shipments/{shipmentId}
```

Пример полученных данных в ходе обращения к эндпоинту `GET /api/shipments/{shipmentId}`:
```json
{
  "id": 52,
  "shipmentId": "3Vn1w2E6",
  "createdAt": "2026-05-12T16:54:37.000022",
  "shipmentStatus": "DELIVERED",
  "cargo": {
    "id": 52,
    "cargoId": 6049142645,
    "cargoDescription": "Refrigerators LG",
    "ownerCompanyName": "OOO Nord",
    "weight": 79,
    "numberOfPackages": 104,
    "originCountry": "Руанда",
    "destinationCountry": "Кыргызстан",
    "cargoTrackingNumber": 441880063947
  },
  "driver": {
    "id": 52,
    "driverId": 8324727545,
    "driverName": "Petr",
    "driverLastName": "Borisov",
    "driverLicenseNumber": "MK064HA",
    "contactPhoneNumber": "+7(913)378-53-07",
    "transitCompany": "ООО ТюменьТрейд"
  },
  "route": {
    "id": 52,
    "startPoint": "Тольятти",
    "endPoint": "Хабаровск",
    "currentPoint": "Нижний Новгород",
    "plannedArrival": "2026-05-22T16:54:37.040945"
  },
  "vehicle": {
    "id": 52,
    "vehicleId": 2638860301,
    "vehicleRegistrationNumber": "L063HF 701",
    "vehicleType": "TRUCK"
  }
}
```
---
## Перспективы развития и доработка проекта:

Основная цель данного проекта состояла в том, чтобы на практике изучить и применить подходы к проектированию EDA-подобных систем с использованием Apache Kafka.
Дальнейшие шаги будут направлены на доработку и углубленное изучениие концепций микросервисного подхода к проектированию систем.

А именно:
- плановая доработка идемпотентности системы в рамках работы `event-handler-service`;
- приближение генерации данных к более реалистичным production сценариям;
- оптимизация работы RESTapi, упрощение структуры ответов и доработка бизнес-сценариев;
- унификация конфигурации проекта к maven multi-module структуре с родительским `pom.xml` для упрощения сборки и управления зависимостями;
- декомпозиция модели данных `ShipmentData`, переход от использования общей модели из `common` к отдельным event-моделям, с целью уменьшения связности между компонентами системы;
- декомпозиция сервиса `ShipmentOrchestratorService` из `event-generator-service`, для упрощения структуры и повышения чистоты.

---