# LetCrypto

A full-stack cryptocurrency trading platform. Users register with a randomized starting balance, monitor live market prices, buy and sell crypto assets, and chat with a Gemini-powered AI assistant about their portfolio and market trends.

## Architecture

| Component | Technology | Role |
|---|---|---|
| Web App | React + Vite (SPA) | Market list, trading modals, portfolio, AI chat |
| Core | Spring Boot (modular monolith) | Auth, trading, market data, AI orchestration |
| Cache | Redis | Session tokens and latest prices (sub-ms reads) |
| Database | PostgreSQL | Users, wallets, trades, price history (source of truth) |
| Data Provider | Binance public API | Live price feed, refreshed every 15 seconds |
| LLM | Google Gemini | Context-aware portfolio & market insights |

The backend scheduler pulls live prices every 15 seconds, overwrites the Redis cache, and periodically snapshots prices into PostgreSQL for trend history. Price reads always come from Redis; financial state always lives in PostgreSQL with transactional integrity.

## Prerequisites

- Java 17+
- Node.js 18+
- Docker (for PostgreSQL + Redis)
- A Google Gemini API key ([get one here](https://aistudio.google.com/apikey))

## Setup

### 1. Environment variables

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

| Variable | Default | Description |
|---|---|---|
| `POSTGRES_HOST` | `localhost` | PostgreSQL host |
| `POSTGRES_PORT` | `5432` | PostgreSQL port |
| `POSTGRES_DB` | `letcrypto` | Database name |
| `POSTGRES_USER` | `letcrypto` | Database user |
| `POSTGRES_PASSWORD` | `letcrypto` | Database password |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6379` | Redis port |
| `GEMINI_API_KEY` | — | **Required.** Google Gemini API key |
| `GEMINI_MODEL` | `gemini-3.5-flash` | Gemini model name |

No credentials are hardcoded; the application reads everything from environment variables.

### 2. Infrastructure (PostgreSQL + Redis)

```bash
docker compose up -d
```

On first startup the SQL scripts in [`init-scripts/`](init-scripts/) run automatically: `01-schema.sql` creates all tables with foreign key constraints, `02-seed-assets.sql` inserts the 20 supported cryptocurrencies.

> To reset the database from scratch: `docker compose down -v && docker compose up -d`

### 3. Backend (LetCrypto Core)

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Make sure `GEMINI_API_KEY` is set in the environment the app starts from (IDE run configuration or shell). The API runs at `http://localhost:8080`.

Swagger UI: **http://localhost:8080/swagger-ui.html**

### 4. Frontend (LetCrypto Web App)

```bash
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173**. The dev server proxies all `/api` requests to the backend on port 8080. See [frontend/README.md](frontend/README.md) for details.

## API Overview

| Endpoint | Method | Description |
|---|---|---|
| `/api/auth/create-account` | POST | Register (assigns a randomized starting balance) |
| `/api/auth/login` | POST | Login, returns a session token (cached in Redis) |
| `/api/asset-service/findAll` | GET | All assets with latest prices from Redis |
| `/api/transaction/trade` | POST | Execute a BUY/SELL order (transactional) |
| `/api/transaction/history` | GET | The user's trade ledger |
| `/api/user-assets/portfolio` | GET | Cash balance and holdings with current worth |
| `/api/price-history/history` | GET | Historical price snapshots for an asset |
| `/api/ai/query` | POST | Ask the Gemini-powered assistant |

Authenticated endpoints expect the session token in the `Authorization` header.
