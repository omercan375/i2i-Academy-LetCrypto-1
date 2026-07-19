import Spinner from './Spinner'

function formatPrice(value) {
  const num = Number(value)
  if (Number.isNaN(num)) return '-'
  return num.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: num < 1 ? 6 : 2,
  })
}

export default function MarketTable({ assets, loading, error, onRefresh, onSelect, lastUpdated }) {
  return (
    <section className="card">
      <div className="card-header">
        <div>
          <h2>Market</h2>
          {lastUpdated && (
            <span className="muted small">Last updated {lastUpdated.toLocaleTimeString()}</span>
          )}
        </div>
        <button className="btn btn-ghost" onClick={onRefresh} disabled={loading} type="button">
          {loading ? <Spinner /> : '⟳ Refresh'}
        </button>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {assets.length === 0 && !error ? (
        <div className="skeleton-list">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="skeleton-row" />
          ))}
        </div>
      ) : (
        <table className="market-table">
          <thead>
            <tr>
              <th>Asset</th>
              <th className="right">Price (USD)</th>
              <th className="right">24h Change</th>
            </tr>
          </thead>
          <tbody>
            {assets.map((asset) => {
              const change = Number(asset.percentChange)
              const changeClass = change > 0 ? 'up' : change < 0 ? 'down' : ''
              return (
                <tr key={asset.assetId} onClick={() => onSelect(asset)} className="clickable">
                  <td>
                    <span className="asset-symbol">{asset.assetSymbol}</span>
                    <span className="muted"> {asset.assetName}</span>
                  </td>
                  <td className="right mono">${formatPrice(asset.assetPrice)}</td>
                  <td className={`right mono ${changeClass}`}>
                    {change > 0 ? '▲' : change < 0 ? '▼' : ''} {formatPrice(Math.abs(change))}%
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}
    </section>
  )
}
