<div align="center">

# ₿ LetCrypto

**A full-stack cryptocurrency trading platform with real-time market data and AI-powered insights**

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=black)](https://react.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-DC382D?logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![Gemini](https://img.shields.io/badge/Google-Gemini-8E75B2?logo=googlegemini&logoColor=white)](https://ai.google.dev/)

### 🌐 [Live Demo → http://178.105.204.39](http://178.105.204.39)

*Register with one click — every new account starts with a randomized cash balance, ready to trade.*

<img src="docs/screenshots/dashboard.png" alt="LetCrypto dashboard — live market, portfolio and AI assistant" width="100%">

</div>

---

## ✨ Features

- 📈 **Live market data** — prices for 20 cryptocurrencies streamed from the Binance API, refreshed every 15 seconds by a background scheduler and served to clients exclusively from Redis for sub-millisecond reads
- 💸 **Real trading engine** — buy and sell with strict transactional integrity: balance updates, asset ledger changes and transaction logs either all succeed or all roll back
- 👛 **Portfolio tracking** — cash balance, holdings and live market worth, auto-refreshing alongside price updates without any UI freezes or layout shifts
- 🤖 **AI market assistant** — a Gemini-powered chat that answers questions about your account, recent transactions and market trends, fed with a context assembled on demand from PostgreSQL and Redis
- 🔐 **Session-based auth** — BCrypt-hashed credentials in PostgreSQL, session tokens cached in Redis for instant validation on every request
- ⚙️ **Account management** — change username, email and password from an in-app settings modal
- 🧾 **Full trade history** — an immutable ledger of every executed order with execution price and totals
- 📉 **Price history** — periodic snapshots persisted to PostgreSQL, rendered as sparkline charts in the trade modal

## 📸 Screenshots

| Trading — conditional Buy / Sell / Sell All with live sparkline | AI assistant — Gemini analysis of your real portfolio |
|:---:|:---:|
| <img src="docs/screenshots/trade-modal.png" alt="Trade modal"> | <img src="docs/screenshots/ai-chat.png" alt="AI chat"> |

| Authentication | Swagger / OpenAPI |
|:---:|:---:|
| <img src="docs/screenshots/login.png" alt="Login page"> | <img src="docs/screenshots/swagger.png" alt="Swagger UI"> |

<img src="docs/screenshots/history.png" alt="Trade history ledger" width="100%">

## 🏗️ Architecture

```mermaid
flowchart LR
    subgraph Client
        UI["⚛️ React SPA<br/>(Vite)"]
    end
    subgraph Backend["Spring Boot — Modular Monolith"]
        CORE["REST API<br/>auth · trading · portfolio · AI"]
        SCHED["⏱️ Scheduler<br/>every 15s"]
    end
    subgraph Data
        REDIS[("Redis<br/>live prices · sessions")]
        PG[("PostgreSQL<br/>users · wallets · trades · history")]
    end
    BINANCE["🌐 Binance API"]
    GEMINI["✨ Google Gemini"]

    UI -->|"REST /api"| CORE
    CORE --> REDIS
    CORE --> PG
    CORE -->|"context-enriched prompts"| GEMINI
    SCHED -->|"fetch prices"| BINANCE
    SCHED -->|"overwrite cache"| REDIS
    SCHED -->|"persist snapshots"| PG
```

**Design principles**

| Concern | Decision |
|---|---|
| Hot data (prices, sessions) | Redis only — no database reads on the polling path |
| Financial state | PostgreSQL as the single source of truth, ACID transactions, FK constraints |
| Price ingestion | `PriceProvider` interface decouples the Binance client from core logic |
| AI resilience | try/catch fallbacks — an unreachable LLM returns a clean error payload, never blocks a server thread |
| Configuration | All credentials and API keys injected via environment variables |

## 🛠️ Tech Stack

**Backend:** Java 17 · Spring Boot 4 · Spring Data JPA · Spring Security · Spring Data Redis · springdoc-openapi
**Frontend:** React 18 · Vite · vanilla CSS (dark theme)
**Data:** PostgreSQL 16 · Redis 7
**AI:** Google Gemini (`google-genai` SDK)
**Infra:** Docker Compose · nginx · systemd (production deployment on Hetzner Cloud)

## 🚀 Getting Started

### Prerequisites

Java 17+, Node.js 18+, Docker, a [Google Gemini API key](https://aistudio.google.com/apikey)

### 1 · Infrastructure

```bash
cp .env.example .env        # fill in your values
docker compose up -d        # PostgreSQL + Redis; DDL scripts in init-scripts/ run automatically
```

### 2 · Backend

Create `src/main/resources/application.properties`:

```properties
spring.application.name=i2i-academy-LetCrypto-1
spring.datasource.url=jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:crypto_db}
spring.datasource.username=${POSTGRES_USER:postgres}
spring.datasource.password=${POSTGRES_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.data.redis.host=${REDIS_HOST:localhost}
spring.data.redis.port=${REDIS_PORT:6379}
gemini.api-key=${GEMINI_API_KEY}
gemini.model=${GEMINI_MODEL:gemini-3.5-flash}
```

Then run (with `POSTGRES_PASSWORD` and `GEMINI_API_KEY` set in your environment):

```bash
./mvnw spring-boot:run      # Windows: .\mvnw.cmd spring-boot:run
```

### 3 · Frontend

```bash
cd frontend
npm install
npm run dev                 # http://localhost:5173 — proxies /api to :8080
```

## 📖 API Overview

Interactive documentation: **`/swagger-ui/index.html`** ([live](http://178.105.204.39/swagger-ui/index.html))

| Endpoint | Method | Description |
|---|---|---|
| `/api/auth/create-account` | POST | Register — assigns a randomized starting balance |
| `/api/auth/login` | POST | Login — returns a Redis-cached session token |
| `/api/asset-service/findAll` | GET | All assets with latest prices (Redis-only read) |
| `/api/transaction/trade` | POST | Execute a BUY / SELL order (transactional) |
| `/api/transaction/history` | GET | The user's full trade ledger |
| `/api/user-assets/portfolio` | GET | Cash balance and holdings with current worth |
| `/api/price-history/history` | GET | Historical price snapshots for an asset |
| `/api/user-service/info` | GET | Account info (email, username, balance) |
| `/api/user-service/change-*` | PUT | Update username / email / password |
| `/api/ai/query` | POST | Ask the Gemini-powered assistant |

Authenticated endpoints expect the session token in the `Authorization` header.

## 📁 Project Structure

```
├── src/main/java/.../          # Spring Boot modular monolith
│   ├── auth/                   #   registration & login
│   ├── users/                  #   account management
│   ├── wallets/                #   cash balances
│   ├── assets/                 #   asset catalog & price refresh
│   ├── user_assets/            #   holdings ledger
│   ├── trade_transaction/      #   order execution & history
│   ├── price_history/          #   persisted price snapshots
│   ├── ai_query_logs/          #   Gemini integration & prompt building
│   ├── provider/               #   Binance price provider (PriceProvider interface)
│   ├── redis/                  #   session & price cache services
│   └── schedular/              #   15-second background price worker
├── frontend/                   # React SPA (Vite)
├── init-scripts/               # PostgreSQL DDL + seed data (auto-run by Docker)
└── docker-compose.yml          # One-command local infrastructure
```

---

<div align="center">

*Built as part of the **i2i Academy** Java Spring bootcamp.*

</div>
