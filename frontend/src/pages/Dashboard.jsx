import { useCallback, useEffect, useRef, useState } from 'react'
import { api } from '../api/client'
import { useAuth } from '../context/AuthContext'
import MarketTable from '../components/MarketTable'
import PortfolioCard from '../components/PortfolioCard'
import TradeHistory from '../components/TradeHistory'
import TradeModal from '../components/TradeModal'
import AccountModal from '../components/AccountModal'
import AiChat from '../components/AiChat'

const POLL_INTERVAL_MS = 15000

export default function Dashboard() {
  const { logout } = useAuth()

  const [assets, setAssets] = useState([])
  const [assetsLoading, setAssetsLoading] = useState(false)
  const [assetsError, setAssetsError] = useState('')
  const [lastUpdated, setLastUpdated] = useState(null)

  const [portfolio, setPortfolio] = useState([])
  const [portfolioLoading, setPortfolioLoading] = useState(true)
  const [portfolioError, setPortfolioError] = useState('')

  const [trades, setTrades] = useState([])
  const [tradesLoading, setTradesLoading] = useState(true)
  const [tradesError, setTradesError] = useState('')

  const [userInfo, setUserInfo] = useState(null)

  const [selectedAsset, setSelectedAsset] = useState(null)
  const [accountOpen, setAccountOpen] = useState(false)
  const [toast, setToast] = useState('')
  const toastTimer = useRef(null)

  const loadAssets = useCallback(async (showSpinner = false) => {
    if (showSpinner) setAssetsLoading(true)
    try {
      const data = await api.getAssets()
      setAssets(Array.isArray(data) ? data : [])
      setAssetsError('')
      setLastUpdated(new Date())
    } catch (err) {
      setAssetsError(err.message)
    } finally {
      if (showSpinner) setAssetsLoading(false)
    }
  }, [])

  const loadPortfolio = useCallback(async (silent = false) => {
    try {
      const data = await api.getPortfolio()
      setPortfolio(Array.isArray(data) ? data : [])
      setPortfolioError('')
    } catch (err) {
      // polling sirasindaki gecici hatalar karta yansimasin
      if (!silent) setPortfolioError(err.message)
    } finally {
      setPortfolioLoading(false)
    }
  }, [])

  const loadTrades = useCallback(async () => {
    try {
      const data = await api.getTradeHistory()
      setTrades(Array.isArray(data) ? data : [])
      setTradesError('')
    } catch (err) {
      setTradesError(err.message)
    } finally {
      setTradesLoading(false)
    }
  }, [])

  const loadUserInfo = useCallback(async () => {
    try {
      const data = await api.getUserInfo()
      setUserInfo(data)
    } catch {
      setUserInfo(null)
    }
  }, [])

  useEffect(() => {
    loadAssets(true)
    loadPortfolio()
    loadTrades()
    loadUserInfo()
    const interval = setInterval(() => {
      loadAssets(false)
      loadPortfolio(true)
    }, POLL_INTERVAL_MS)
    return () => clearInterval(interval)
  }, [loadAssets, loadPortfolio, loadTrades, loadUserInfo])

  const showToast = (message) => {
    setToast(message)
    clearTimeout(toastTimer.current)
    toastTimer.current = setTimeout(() => setToast(''), 4000)
  }

  const handlePortfolioSelect = (symbol) => {
    const asset = assets.find((a) => a.assetSymbol === symbol)
    if (asset) setSelectedAsset(asset)
  }

  const handleTraded = (message) => {
    showToast(message)
    loadPortfolio()
    loadTrades()
    loadUserInfo()
    loadAssets(false)
  }

  return (
    <div className="dashboard">
      <header className="topbar">
        <div className="topbar-brand">
          <span className="brand-mark small-mark">₿</span>
          <span className="brand-name">LetCrypto</span>
        </div>
        <div className="topbar-actions">
          <button
            className="btn btn-ghost user-chip"
            onClick={() => setAccountOpen(true)}
            type="button"
          >
            👤 {userInfo?.username ?? 'Account'}
          </button>
          <button className="btn btn-ghost" onClick={logout} type="button">
            Sign Out
          </button>
        </div>
      </header>

      <main className="dashboard-grid">
        <div className="col-main">
          <MarketTable
            assets={assets}
            loading={assetsLoading}
            error={assetsError}
            onRefresh={() => loadAssets(true)}
            onSelect={setSelectedAsset}
            lastUpdated={lastUpdated}
          />
          <TradeHistory trades={trades} loading={tradesLoading} error={tradesError} />
        </div>

        <div className="col-side">
          <PortfolioCard
            portfolio={portfolio}
            loading={portfolioLoading}
            error={portfolioError}
            onSelect={handlePortfolioSelect}
          />
          <AiChat />
        </div>
      </main>

      {selectedAsset && (
        <TradeModal
          asset={selectedAsset}
          portfolio={portfolio}
          onClose={() => setSelectedAsset(null)}
          onTraded={handleTraded}
        />
      )}

      {accountOpen && (
        <AccountModal
          userInfo={userInfo}
          onClose={() => setAccountOpen(false)}
          onUpdated={loadUserInfo}
        />
      )}

      {toast && <div className="toast">{toast}</div>}
    </div>
  )
}
