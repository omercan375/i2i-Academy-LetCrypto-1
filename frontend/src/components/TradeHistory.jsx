export default function TradeHistory({ trades, loading, error }) {
  if (loading) {
    return (
      <section className="card">
        <div className="card-header">
          <h2>Trade History</h2>
        </div>
        <div className="skeleton-list">
          {[...Array(3)].map((_, i) => (
            <div key={i} className="skeleton-row" />
          ))}
        </div>
      </section>
    )
  }

  return (
    <section className="card">
      <div className="card-header">
        <h2>Trade History</h2>
      </div>

      {error && <div className="alert alert-error">{error}</div>}

      {trades.length === 0 ? (
        <p className="muted small empty-note">No trades yet.</p>
      ) : (
        <div className="scroll-table">
          <table className="market-table">
            <thead>
              <tr>
                <th>Date</th>
                <th>Asset</th>
                <th>Side</th>
                <th className="right">Amount</th>
                <th className="right">Price</th>
                <th className="right">Total</th>
              </tr>
            </thead>
            <tbody>
              {trades.map((t, i) => (
                <tr key={i}>
                  <td className="small muted">{new Date(t.dateTime).toLocaleString()}</td>
                  <td className="asset-symbol">{t.assetSymbol}</td>
                  <td>
                    <span className={`badge ${t.tradeType === 'BUY' ? 'badge-buy' : 'badge-sell'}`}>
                      {t.tradeType}
                    </span>
                  </td>
                  <td className="right mono">{Number(t.cryptoAmount)}</td>
                  <td className="right mono">
                    ${Number(t.executionPrice).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                  </td>
                  <td className="right mono">
                    ${Number(t.totalAmount).toLocaleString('en-US', { minimumFractionDigits: 2 })}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  )
}
