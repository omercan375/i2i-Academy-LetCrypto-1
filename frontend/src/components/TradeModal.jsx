import { useEffect, useMemo, useState } from 'react'
import { api } from '../api/client'
import Spinner from './Spinner'

function Sparkline({ points }) {
  if (!points || points.length < 2) return null
  const prices = points.map((p) => Number(p.price))
  const min = Math.min(...prices)
  const max = Math.max(...prices)
  const range = max - min || 1
  const width = 320
  const height = 60
  const coords = prices.map((price, i) => {
    const x = (i / (prices.length - 1)) * width
    const y = height - ((price - min) / range) * (height - 8) - 4
    return `${x},${y}`
  })
  const rising = prices[prices.length - 1] >= prices[0]
  return (
    <svg viewBox={`0 0 ${width} ${height}`} className="sparkline" preserveAspectRatio="none">
      <polyline
        points={coords.join(' ')}
        fill="none"
        stroke={rising ? 'var(--green)' : 'var(--red)'}
        strokeWidth="2"
      />
    </svg>
  )
}

export default function TradeModal({ asset, portfolio, onClose, onTraded }) {
  const cashBalance = portfolio.length > 0 ? Number(portfolio[0].cashBalance) : null
  const holding = portfolio.find((p) => p.assetSymbol === asset.assetSymbol)
  const heldQuantity = holding ? Number(holding.assetQuantity) : 0

  // cashBalance === null means the backend returned no portfolio rows (new user);
  // let the backend validate eligibility on execution in that case
  const canBuy = cashBalance === null || cashBalance > 0
  const canSell = heldQuantity > 0

  const [tradeType, setTradeType] = useState(canBuy ? 'BUY' : 'SELL')
  const [amount, setAmount] = useState('')
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const [history, setHistory] = useState([])

  useEffect(() => {
    api
      .getPriceHistory(asset.assetId)
      .then((data) => setHistory(Array.isArray(data) ? data : []))
      .catch(() => setHistory([]))
  }, [asset.assetId])

  const price = Number(asset.assetPrice)
  const isSellAll = tradeType === 'SELL_ALL'
  const numericAmount = isSellAll ? heldQuantity : Number(amount)

  const estimatedTotal = useMemo(() => {
    if (isSellAll) return heldQuantity * price
    if (!amount || Number.isNaN(numericAmount)) return null
    return numericAmount * price
  }, [isSellAll, heldQuantity, amount, numericAmount, price])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (!isSellAll && (!amount || Number.isNaN(numericAmount) || numericAmount <= 0)) {
      setError('Invalid amount entered. Please enter a value greater than zero.')
      return
    }
    setSubmitting(true)
    try {
      // SELL_ALL: backend eldeki miktarla birebir esitlik bekliyor
      const sendAmount = isSellAll ? holding.assetQuantity : numericAmount
      await api.trade(asset.assetSymbol, tradeType, sendAmount)
      const verb = tradeType === 'BUY' ? 'Bought' : 'Sold'
      onTraded(`${verb} ${sendAmount} ${asset.assetSymbol} successfully.`)
      onClose()
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>
            {asset.assetName} <span className="muted">({asset.assetSymbol})</span>
          </h3>
          <button className="btn-close" onClick={onClose} type="button" aria-label="Close">
            ✕
          </button>
        </div>

        <div className="modal-price mono">
          ${price.toLocaleString('en-US', { minimumFractionDigits: 2 })}
        </div>
        <Sparkline points={history} />

        <div className="modal-balances small muted">
          <span>
            Cash:{' '}
            {cashBalance === null
              ? '—'
              : `$${cashBalance.toLocaleString('en-US', { minimumFractionDigits: 2 })}`}
          </span>
          <span>
            Holding: {heldQuantity} {asset.assetSymbol}
          </span>
        </div>

        {!canBuy && !canSell ? (
          <div className="alert alert-error">
            You have no available cash balance and no {asset.assetSymbol} holdings to trade.
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="trade-tabs">
              {canBuy && (
                <button
                  type="button"
                  className={`trade-tab buy ${tradeType === 'BUY' ? 'active' : ''}`}
                  onClick={() => setTradeType('BUY')}
                >
                  Buy
                </button>
              )}
              {canSell && (
                <button
                  type="button"
                  className={`trade-tab sell ${tradeType === 'SELL' ? 'active' : ''}`}
                  onClick={() => setTradeType('SELL')}
                >
                  Sell
                </button>
              )}
              {canSell && (
                <button
                  type="button"
                  className={`trade-tab sell ${isSellAll ? 'active' : ''}`}
                  onClick={() => setTradeType('SELL_ALL')}
                >
                  Sell All
                </button>
              )}
            </div>

            {isSellAll ? (
              <div className="sell-all-summary">
                You are about to sell your <strong>entire holding</strong> of{' '}
                <strong>
                  {heldQuantity} {asset.assetSymbol}
                </strong>
                .
              </div>
            ) : (
              <label className="modal-label">
                Amount ({asset.assetSymbol})
                <input
                  type="number"
                  step="any"
                  min="0"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="0.00"
                  autoFocus
                />
              </label>
            )}

            {estimatedTotal !== null && (
              <div className="small muted estimate">
                Estimated total: $
                {estimatedTotal.toLocaleString('en-US', { minimumFractionDigits: 2 })}
              </div>
            )}

            {error && <div className="alert alert-error">{error}</div>}

            <button
              type="submit"
              className={`btn btn-block ${tradeType === 'BUY' ? 'btn-buy' : 'btn-sell'}`}
              disabled={submitting}
            >
              {submitting ? <Spinner /> : 'Execute Order'}
            </button>
          </form>
        )}
      </div>
    </div>
  )
}
