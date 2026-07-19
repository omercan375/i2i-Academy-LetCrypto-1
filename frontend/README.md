# LetCrypto Web App

React + Vite Single-Page Application for the LetCrypto platform. Talks to the LetCrypto Core (Spring Boot) backend on `http://localhost:8080` through a dev proxy.

## Features

- **Authentication** — sign in / create account; session token is stored in `localStorage` and sent as a `Bearer` token on every authenticated request.
- **Live market list** — prices auto-refresh every 15 seconds (matching the backend scheduler) and can be refreshed manually. Updates re-render in place with no layout shift.
- **Trading** — clicking an asset opens a modal. *Buy* is shown only when the cash balance is eligible, *Sell* only when the user holds that asset. Orders are placed with the "Execute Order" button; backend business errors (e.g. insufficient balance) are shown as human-readable messages.
- **Portfolio & trade history** — cash balance, holdings with current worth, and the full transaction ledger.
- **AI chat** — Gemini-powered chat about the account, market trends and recent transactions, with a loading indicator while waiting and a lightweight Markdown renderer for responses.
- **Price sparkline** — the trade modal shows the asset's recent price history from the `price-history` endpoint.

## Running locally

```bash
npm install
npm run dev        # http://localhost:5173, proxies /api -> http://localhost:8080
```

The backend (LetCrypto Core) must be running on port 8080 together with PostgreSQL and Redis.

## Production build

```bash
npm run build      # outputs static files to dist/
```
