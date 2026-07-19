-- Supported cryptocurrency assets

INSERT INTO assets (symbol, name, created_at) VALUES
    ('BTC',  'Bitcoin',       NOW()),
    ('ETH',  'Ethereum',      NOW()),
    ('BNB',  'BNB',           NOW()),
    ('SOL',  'Solana',        NOW()),
    ('XRP',  'XRP',           NOW()),
    ('ADA',  'Cardano',       NOW()),
    ('DOGE', 'Dogecoin',      NOW()),
    ('TRX',  'TRON',          NOW()),
    ('AVAX', 'Avalanche',     NOW()),
    ('LINK', 'Chainlink',     NOW()),
    ('DOT',  'Polkadot',      NOW()),
    ('LTC',  'Litecoin',      NOW()),
    ('ATOM', 'Cosmos',        NOW()),
    ('UNI',  'Uniswap',       NOW()),
    ('AAVE', 'Aave',          NOW()),
    ('SUI',  'Sui',           NOW()),
    ('APT',  'Aptos',         NOW()),
    ('ARB',  'Arbitrum',      NOW()),
    ('OP',   'Optimism',      NOW()),
    ('NEAR', 'NEAR Protocol', NOW());
