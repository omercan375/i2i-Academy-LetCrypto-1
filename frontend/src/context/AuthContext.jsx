import { createContext, useCallback, useContext, useEffect, useState } from 'react'
import { api, setUnauthorizedHandler } from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => localStorage.getItem('letcrypto_token'))

  const logout = useCallback(() => {
    localStorage.removeItem('letcrypto_token')
    setToken(null)
  }, [])

  useEffect(() => {
    setUnauthorizedHandler(logout)
  }, [logout])

  const login = useCallback(async (email, password) => {
    const receivedToken = await api.login(email, password)
    localStorage.setItem('letcrypto_token', receivedToken)
    setToken(receivedToken)
  }, [])

  const register = useCallback(async (email, username, password) => {
    await api.createAccount(email, username, password)
  }, [])

  return (
    <AuthContext.Provider value={{ token, isAuthenticated: !!token, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
