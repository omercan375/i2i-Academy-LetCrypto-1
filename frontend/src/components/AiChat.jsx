import { useEffect, useRef, useState } from 'react'
import { api } from '../api/client'
import Spinner from './Spinner'

function renderMarkdown(text) {
  let html = text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
  html = html.replace(/```([\s\S]*?)```/g, '<pre>$1</pre>')
  html = html.replace(/`([^`]+)`/g, '<code>$1</code>')
  html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
  html = html.replace(/\*([^*\n]+)\*/g, '<em>$1</em>')
  html = html.replace(/^### (.*)$/gm, '<h4>$1</h4>')
  html = html.replace(/^## (.*)$/gm, '<h4>$1</h4>')
  html = html.replace(/^# (.*)$/gm, '<h4>$1</h4>')
  html = html.replace(/^[-*] (.*)$/gm, '<li>$1</li>')
  html = html.replace(/(<li>[\s\S]*?<\/li>)(?!\s*<li>)/g, '<ul>$1</ul>')
  html = html.replace(/\n/g, '<br/>')
  html = html.replace(/<\/li><br\/>/g, '</li>')
  html = html.replace(/<\/h4><br\/>/g, '</h4>')
  return html
}

const SUGGESTIONS = [
  'How is my portfolio doing?',
  'What are the recent market trends?',
  'Summarize my recent transactions',
  'Should I diversify my holdings?',
]

export default function AiChat() {
  const [messages, setMessages] = useState([])
  const [input, setInput] = useState('')
  const [loading, setLoading] = useState(false)
  const bottomRef = useRef(null)

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [messages, loading])

  const send = async (text) => {
    const query = text.trim()
    if (!query || loading) return
    setInput('')
    setMessages((prev) => [...prev, { role: 'user', text: query }])
    setLoading(true)
    try {
      const response = await api.aiQuery(query)
      setMessages((prev) => [...prev, { role: 'ai', text: response.response }])
    } catch (err) {
      setMessages((prev) => [...prev, { role: 'error', text: err.message }])
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    send(input)
  }

  return (
    <section className="card chat-card">
      <div className="card-header">
        <h2>✨ LetCrypto AI</h2>
        <span className="muted small">Powered by Gemini</span>
      </div>

      <div className="chat-messages">
        {messages.length === 0 && !loading && (
          <div className="chat-empty">
            <p className="muted">
              Ask about your account, recent market trends, past transactions or crypto insights.
            </p>
            <div className="chat-suggestions">
              {SUGGESTIONS.map((s) => (
                <button key={s} type="button" className="chip" onClick={() => send(s)}>
                  {s}
                </button>
              ))}
            </div>
          </div>
        )}

        {messages.map((m, i) =>
          m.role === 'ai' ? (
            <div
              key={i}
              className="chat-bubble ai"
              dangerouslySetInnerHTML={{ __html: renderMarkdown(m.text) }}
            />
          ) : (
            <div key={i} className={`chat-bubble ${m.role}`}>
              {m.text}
            </div>
          ),
        )}

        {loading && (
          <div className="chat-bubble ai thinking">
            <Spinner size={14} /> Analyzing your data…
          </div>
        )}
        <div ref={bottomRef} />
      </div>

      <form className="chat-input" onSubmit={handleSubmit}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Ask LetCrypto AI…"
          maxLength={1000}
          disabled={loading}
        />
        <button type="submit" className="btn btn-primary" disabled={loading || !input.trim()}>
          {loading ? <Spinner /> : 'Send'}
        </button>
      </form>
    </section>
  )
}
