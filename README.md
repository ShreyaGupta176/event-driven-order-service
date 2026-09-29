# 📦 Event-Driven Order Processing Microservice

A production-grade distributed microservice built with **Java 17**, **Spring Boot 3**, **Apache Kafka**, and **PostgreSQL**. Demonstrates asynchronous messaging, transaction persistence, and containerized local infrastructure.

---

## ⚙️ Architecture & Data Flow

```text
HTTP Client (Postman / curl)
       │
       ▼ [POST /api/v1/orders]
[OrderController]  ── (DTO Validation) ──► [OrderService]
                                                 │
                   ┌─────────────────────────────┴─────────────────────────────┐
                   ▼ (Transactional Write)                                     ▼ (Async Event Broadcast)
          [OrderRepository]                                              [KafkaTemplate]
                   │                                                             │
                   ▼ (JPA / Hibernate)                                           ▼ (Partitioned Topic)
             PostgreSQL / H2                                                Apache Kafka
            (`t_orders` table)                                            (`order-events` topic)
```

1. **Client** dispatches an HTTP POST request to `/api/v1/orders`.
2. **Order Service** validates the payload and persists the order entity in the relational store.
3. Upon commit, an `OrderPlacedEvent` is published to the `order-events` Kafka topic with 3-way partitioning.
4. Downstream microservices (Inventory, Notification) subscribe to the broker for asynchronous processing.

---

## 🚀 Tech Stack

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Hibernate
* **Event Streaming:** Apache Kafka, Zookeeper
* **Database:** PostgreSQL (with H2 in-memory local profile)
* **Orchestration:** Docker Compose

---

## 🛠️ Quickstart Guide

### 1. Launch Infrastructure (Docker)

```bash
docker compose up -d

```

*(Skip if using the local in-memory profile)*

### 2. Run the Application

```bash
# Windows
.\gradlew.bat bootRun

# Linux / macOS
./gradlew bootRun

```

---

## 🧪 Quick Test

### 1. Place Order

```bash
curl -X POST http://localhost:8081/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"skuCode": "IPHONE-15-PRO", "price": 1199.99, "quantity": 1}'

```

*Returns `201 Created` with a generated `orderNumber`.*

### 2. Get Order

```bash
curl http://localhost:8081/api/v1/orders/<ORDER_NUMBER>

```

### 3. Verify

* **Database:** Visit `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:order_db`, User: `sa`, Password: blank) $\rightarrow$ `SELECT * FROM t_orders;`.
* **Kafka Event:** Check the console log for:
```text
OrderPlacedEvent emitted to Kafka for order: <ORDER_NUMBER>

```