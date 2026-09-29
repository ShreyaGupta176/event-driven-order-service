# 📦 Event-Driven Order Processing Microservice

A production-grade distributed microservice built with **Java 17**, **Spring Boot 3**, **Apache Kafka**, and **PostgreSQL**. Demonstrates asynchronous messaging, transaction persistence, and containerized local infrastructure.

## ⚙️ Architecture & Data Flow
1. **Client** dispatches an HTTP POST request to `/api/v1/orders`.
2. **Order Service** validates the payload and persists the order entity in the relational store.
3. Upon commit, an `OrderPlacedEvent` is published to the `order-events` Kafka topic with 3-way partitioning.
4. Downstream microservices (Inventory, Notification) subscribe to the broker for asynchronous processing.

## 🚀 Tech Stack
- **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Hibernate
- **Event Streaming:** Apache Kafka, Zookeeper
- **Database:** PostgreSQL (with H2 in-memory local profile)
- **Orchestration:** Docker Compose

## 🛠️ Quickstart Guide

### 1. Launch Infrastructure (Docker)
```bash
docker compose up -d