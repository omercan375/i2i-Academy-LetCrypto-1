-- LetCrypto database schema
-- Runs automatically on first container startup via /docker-entrypoint-initdb.d

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email       VARCHAR(255) NOT NULL UNIQUE,
    username    VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP,
    version     INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE wallets (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID NOT NULL REFERENCES users (id),
    cash_balance NUMERIC(30, 8) NOT NULL DEFAULT 0,
    version      INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE assets (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    symbol     VARCHAR(20) NOT NULL UNIQUE,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP
);

CREATE TABLE user_assets (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID NOT NULL REFERENCES users (id),
    asset_id   UUID NOT NULL REFERENCES assets (id),
    quantity   NUMERIC(30, 8) NOT NULL DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version    INTEGER NOT NULL DEFAULT 0,
    UNIQUE (user_id, asset_id)
);

CREATE TABLE trade_transaction (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users (id),
    asset_id        UUID NOT NULL REFERENCES assets (id),
    trade_type      VARCHAR(10) NOT NULL CHECK (trade_type IN ('BUY', 'SELL', 'SELL_ALL')),
    crypto_amount   NUMERIC(30, 8) NOT NULL,
    execution_price NUMERIC(30, 8) NOT NULL,
    total_amount    NUMERIC(30, 8) NOT NULL,
    executed_at     TIMESTAMP
);

CREATE TABLE price_history (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    asset_id       UUID NOT NULL REFERENCES assets (id),
    price          NUMERIC(30, 8) NOT NULL,
    change_percent NUMERIC(12, 4),
    recorded_at    TIMESTAMP
);

CREATE TABLE ai_query_logs (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID NOT NULL REFERENCES users (id),
    query      TEXT NOT NULL,
    response   TEXT,
    created_at TIMESTAMP
);

-- Frequently queried foreign keys
CREATE INDEX idx_wallets_user_id ON wallets (user_id);
CREATE INDEX idx_user_assets_user_id ON user_assets (user_id);
CREATE INDEX idx_trade_transaction_user_id ON trade_transaction (user_id);
CREATE INDEX idx_price_history_asset_id ON price_history (asset_id);
CREATE INDEX idx_ai_query_logs_user_id ON ai_query_logs (user_id);
