import { useState } from 'react'
import { api } from '../api/client'
import Spinner from './Spinner'

const TABS = [
  { id: 'profile', label: 'Profile' },
  { id: 'username', label: 'Username' },
  { id: 'email', label: 'Email' },
  { id: 'password', label: 'Password' },
]

function useFormState() {
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const run = async (action, successMessage) => {
    setError('')
    setSuccess('')
    setSubmitting(true)
    try {
      await action()
      setSuccess(successMessage)
      return true
    } catch (err) {
      setError(err.message)
      return false
    } finally {
      setSubmitting(false)
    }
  }

  const reset = () => {
    setError('')
    setSuccess('')
  }

  return { error, success, submitting, run, reset }
}

export default function AccountModal({ userInfo, onClose, onUpdated }) {
  const [tab, setTab] = useState('profile')
  const { error, success, submitting, run, reset } = useFormState()

  const [newUsername, setNewUsername] = useState('')
  const [newEmail, setNewEmail] = useState('')
  const [oldPassword, setOldPassword] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [currentPassword, setCurrentPassword] = useState('')

  const switchTab = (nextTab) => {
    setTab(nextTab)
    reset()
    setCurrentPassword('')
  }

  const handleUsername = async (e) => {
    e.preventDefault()
    const ok = await run(
      () => api.changeUsername(userInfo.username, newUsername, currentPassword),
      'Username updated successfully.',
    )
    if (ok) {
      setNewUsername('')
      setCurrentPassword('')
      onUpdated()
    }
  }

  const handleEmail = async (e) => {
    e.preventDefault()
    const ok = await run(
      () => api.changeEmail(userInfo.email, newEmail, currentPassword),
      'Email updated successfully.',
    )
    if (ok) {
      setNewEmail('')
      setCurrentPassword('')
      onUpdated()
    }
  }

  const handlePassword = async (e) => {
    e.preventDefault()
    if (newPassword !== confirmPassword) {
      await run(() => Promise.reject(new Error('New passwords do not match.')))
      return
    }
    const ok = await run(
      () => api.changePassword(oldPassword, newPassword),
      'Password updated successfully.',
    )
    if (ok) {
      setOldPassword('')
      setNewPassword('')
      setConfirmPassword('')
    }
  }

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="modal-header">
          <h3>Account Settings</h3>
          <button className="btn-close" onClick={onClose} type="button" aria-label="Close">
            ✕
          </button>
        </div>

        <div className="account-tabs">
          {TABS.map((t) => (
            <button
              key={t.id}
              type="button"
              className={tab === t.id ? 'active' : ''}
              onClick={() => switchTab(t.id)}
            >
              {t.label}
            </button>
          ))}
        </div>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        {tab === 'profile' && (
          <div className="profile-info">
            <div className="info-row">
              <span className="muted small">Username</span>
              <span>{userInfo?.username ?? '—'}</span>
            </div>
            <div className="info-row">
              <span className="muted small">Email</span>
              <span>{userInfo?.email ?? '—'}</span>
            </div>
            <div className="info-row">
              <span className="muted small">Cash Balance</span>
              <span className="mono">
                {userInfo
                  ? `$${Number(userInfo.cashBalance).toLocaleString('en-US', { minimumFractionDigits: 2 })}`
                  : '—'}
              </span>
            </div>
          </div>
        )}

        {tab === 'username' && (
          <form onSubmit={handleUsername} className="auth-form">
            <label>
              Current username
              <input type="text" value={userInfo?.username ?? ''} disabled />
            </label>
            <label>
              New username
              <input
                type="text"
                value={newUsername}
                onChange={(e) => setNewUsername(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <label>
              Password
              <input
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <button type="submit" className="btn btn-primary btn-block" disabled={submitting}>
              {submitting ? <Spinner /> : 'Update Username'}
            </button>
          </form>
        )}

        {tab === 'email' && (
          <form onSubmit={handleEmail} className="auth-form">
            <label>
              Current email
              <input type="email" value={userInfo?.email ?? ''} disabled />
            </label>
            <label>
              New email
              <input
                type="email"
                value={newEmail}
                onChange={(e) => setNewEmail(e.target.value)}
                required
              />
            </label>
            <label>
              Password
              <input
                type="password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <button type="submit" className="btn btn-primary btn-block" disabled={submitting}>
              {submitting ? <Spinner /> : 'Update Email'}
            </button>
          </form>
        )}

        {tab === 'password' && (
          <form onSubmit={handlePassword} className="auth-form">
            <label>
              Current password
              <input
                type="password"
                value={oldPassword}
                onChange={(e) => setOldPassword(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <label>
              New password
              <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <label>
              Confirm new password
              <input
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                minLength={3}
                required
              />
            </label>
            <button type="submit" className="btn btn-primary btn-block" disabled={submitting}>
              {submitting ? <Spinner /> : 'Update Password'}
            </button>
          </form>
        )}
      </div>
    </div>
  )
}
