const BASE_URL = '/api'

// Maps raw backend error strings to polished human-readable messages
const FRIENDLY_MESSAGES = {
  'INSUFFICIENT BALANCE': 'Insufficient funds to complete this trade.',
  'INSUFFICENT BALANCE': 'Insufficient funds to complete this trade.',
  'ASSET NOT FOUND': "You don't hold this asset, so it cannot be sold.",
  'Asset not found': 'This asset price is not available right now. Please try again shortly.',
  'PASSWORD NOT FOUND': 'The password you entered is incorrect.',
  'EMAIL ALREADY EXIST': 'This email address is already in use.',
  'Email already exists': 'An account with this email already exists.',
  'USERNAME ALREADY EXIST': 'This username is already taken.',
  'Invalid email or password': 'Invalid email or password.',
  'EMAIL NOT UPDATED': 'The email could not be updated. Check that the current email is correct.',
  'USERNAME NOT UPDATED': 'The username could not be updated. Please try again.',
  'PASSWORD NOT UPDATED': 'The password could not be updated. Please try again.',
  'CANT UPDATE ASSET': 'The trade could not be completed. Please try again.',
  'CANT SAVE ASSET': 'The trade could not be completed. Please try again.',
  'CANT UPDATE BALANCE': 'The trade could not be completed. Please try again.',
  'WALLET NOT FOUND': 'Your wallet could not be found. Please contact support.',
}

function toFriendlyMessage(raw) {
  return FRIENDLY_MESSAGES[raw.trim()] ?? raw
}

let onUnauthorized = null

export function setUnauthorizedHandler(handler) {
  onUnauthorized = handler
}

function getToken() {
  return localStorage.getItem('letcrypto_token')
}

async function parseError(response) {
  const text = await response.text()
  try {
    const json = JSON.parse(text)
    if (typeof json === 'object' && json !== null) {
      return Object.values(json).join(' • ')
    }
    return toFriendlyMessage(String(json))
  } catch {
    return text ? toFriendlyMessage(text) : `Request failed (${response.status})`
  }
}

async function request(path, { method = 'GET', body, auth = false } = {}) {
  const headers = { 'Content-Type': 'application/json' }
  if (auth) {
    headers['Authorization'] = `Bearer ${getToken()}`
  }

  const response = await fetch(`${BASE_URL}${path}`, {
    method,
    headers,
    body: body !== undefined ? JSON.stringify(body) : undefined,
  })

  if (!response.ok) {
    const message = await parseError(response)
    if (auth && response.status === 404 && message.toUpperCase().includes('USER NOT FOUND')) {
      onUnauthorized?.()
      throw new Error('Your session has expired. Please sign in again.')
    }
    throw new Error(message)
  }

  const text = await response.text()
  if (!text) return null
  try {
    return JSON.parse(text)
  } catch {
    return text
  }
}

export const api = {
  login: (email, password) =>
    request('/auth/login', { method: 'POST', body: { email, password } }),

  createAccount: (email, username, password) =>
    request('/auth/create-account', { method: 'POST', body: { email, username, password } }),

  getAssets: () => request('/asset-service/findAll'),

  getPortfolio: () => request('/user-assets/portfolio', { auth: true }),

  trade: (assetSymbol, tradeType, cryptoAmount) =>
    request('/transaction/trade', {
      method: 'POST',
      auth: true,
      body: { assetSymbol, tradeType, cryptoAmount },
    }),

  getTradeHistory: () => request('/transaction/history', { auth: true }),

  getPriceHistory: (assetId) => request(`/price-history/history?assetId=${assetId}`),

  aiQuery: (query) => request('/ai/query', { method: 'POST', auth: true, body: { query } }),

  getUserInfo: () => request('/user-service/info', { auth: true }),

  changePassword: (oldPassword, newPassword) =>
    request('/user-service/change-password', {
      method: 'PUT',
      auth: true,
      body: { oldPassword, newPassword },
    }),

  changeEmail: (oldEmail, newEmail, password) =>
    request('/user-service/change-email', {
      method: 'PUT',
      auth: true,
      body: { oldEmail, newEmail, password },
    }),

  changeUsername: (oldUsername, newUsername, password) =>
    request('/user-service/change-username', {
      method: 'PUT',
      auth: true,
      body: { oldUsername, newUsername, password },
    }),
}
