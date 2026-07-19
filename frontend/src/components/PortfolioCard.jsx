export default function PortfolioCard({ portfolio, loading, error, onSelect }) {
  if (loading) {
    return (
      <section className="card">
        <div className="card-header">
          <h2>Portfolio</h2>
        </div>
        <div className="skeleton-list">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="skeleton-row" />
          ))}
        </div>
      </section>
    )
  }

  const cashBalance = portfolio.length > 0 ? Number(portfolio[0].cashBalance) : null
  const holdings = portfolio.filter((p) => Number(p.assetQuantity) > 0)
  const totalWorth = holdings.reduce((sum, p) => sum + Number(p.totalWorth || 0), 0)

  return (
    <section className="card">
      <div className="card-header">
        <h2>Portfolio</h2>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {cashBalance !== null && (
        <div className="balance-row">
          <div className="balance-box">
            <span className="muted small">Cash Balance</span>
            <span className="balance-value mono">
              ${cashBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </span>
          </div>
          <div className="balance-box">
            <span className="muted small">Assets Worth</span>
            <span className="balance-value mono">
              ${totalWorth.toLocaleString('en-US', { minimumFractionDigits: 2 })}
            </span>
          </div>
        </div>
      )}

      {holdings.length === 0 ? (
        <p className="muted small empty-note">No crypto holdings yet. Buy your first asset from the market list!</p>
      ) : (
        <table className="market-table">
          <thead>
            <tr>
              <th>Asset</th>
              <th className="right">Quantity</th>
              <th className="right">Worth (USD)</th>
            </tr>
          </thead>
          <tbody>
            {holdings.map((p) => (
              <tr key={p.assetSymbol} className="clickable" onClick={() => onSelect?.(p.assetSymbol)}>
                <td className="asset-symbol">{p.assetSymbol}</td>
                <td className="right mono">{Number(p.assetQuantity)}</td>
                <td className="right mono">
                  ${Number(p.totalWorth).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  )
}
