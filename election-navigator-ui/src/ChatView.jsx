import React, { useState } from 'react';
import { MessageSquare, Send, Sparkles, Shield, Info, Database, FileText, ChevronDown, ChevronUp } from 'lucide-react';

const API_BASE = 'https://ksp-backend-50044360353.development.catalystappsail.in/api';

export default function ChatView() {
  const [messages, setMessages] = useState([
    {
      sender: 'AI',
      text: 'Hello! I am the KSP Natural Language Crime Analytics Assistant powered by Groq LLM (llama-3.1-8b-instant). Ask me any question about Karnataka FIR records, district crime patterns, active investigations, or hotspot emerging clusters.',
      records: [],
      recordsCount: 0,
      explainabilityCaption: 'Connected to KSP Crime Master Database (~3000 synthetic FIR records).'
    }
  ]);

  const [inputMessage, setInputMessage] = useState('');
  const [sessionId, setSessionId] = useState(null);
  const [loading, setLoading] = useState(false);
  const [expandedTableIndex, setExpandedTableIndex] = useState(null);

  const sampleQueries = [
    "Show theft cases in Bengaluru Urban from last month",
    "How many cyber fraud cases are under investigation?",
    "Which districts have emerging crime hotspots?",
    "List burglary FIRs registered in Mysuru"
  ];

  const handleSendMessage = async (queryText) => {
    const textToSend = queryText || inputMessage;
    if (!textToSend.trim() || loading) return;

    // Append user message
    const userMsg = { sender: 'USER', text: textToSend };
    setMessages(prev => [...prev, userMsg]);
    setInputMessage('');
    setLoading(true);

    try {
      const res = await fetch(`${API_BASE}/chat`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: textToSend, sessionId: sessionId })
      });

      const data = await res.json();
      if (data.sessionId) setSessionId(data.sessionId);

      const aiMsg = {
        sender: 'AI',
        text: data.response || 'No response returned from microservice.',
        records: data.records || [],
        recordsCount: data.recordsCount || 0,
        parsedFilters: data.parsedFilters || {},
        explainabilityCaption: data.explainabilityCaption || 'Based on KSP database records.'
      };

      setMessages(prev => [...prev, aiMsg]);
    } catch (err) {
      console.error("Error sending chat message:", err);
      setMessages(prev => [...prev, {
        sender: 'AI',
        text: 'Error connecting to Spring Boot / Python FastAPI microservice. Ensure backend is running.',
        records: [],
        recordsCount: 0
      }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '20px', maxWidth: '1100px', margin: '0 auto', height: 'calc(100vh - 100px)' }}>
      
      {/* Header */}
      <div style={{
        background: '#131f37',
        border: '1px solid #2e416a',
        borderRadius: '12px',
        padding: '16px 20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Sparkles size={22} color="#60a5fa" />
          <div>
            <h2 style={{ fontSize: '1.1rem', color: '#f8fafc' }}>KSP AI Natural Language Query Assistant</h2>
            <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
              Groq LLM structured query parser translating English & Kannadiga crime prompts into transparent SQL/JPA queries.
            </p>
          </div>
        </div>
        <div className="explain-badge">
          <Info size={12} /> Explainable & Transparent Analytics
        </div>
      </div>

      {/* Suggested Query Chips */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '10px', flexWrap: 'wrap' }}>
        <span style={{ fontSize: '0.75rem', color: '#94a3b8', fontWeight: '600' }}>Sample Prompts:</span>
        {sampleQueries.map((q, idx) => (
          <button
            key={idx}
            onClick={() => handleSendMessage(q)}
            style={{
              background: '#1c2a4a',
              color: '#60a5fa',
              border: '1px solid #2e416a',
              padding: '6px 12px',
              borderRadius: '20px',
              fontSize: '0.78rem',
              cursor: 'pointer',
              transition: 'all 0.2s'
            }}
          >
            "{q}"
          </button>
        ))}
      </div>

      {/* Chat Conversation Window */}
      <div style={{
        flex: 1,
        background: '#1c2a4a',
        border: '1px solid #2e416a',
        borderRadius: '12px',
        padding: '20px',
        overflowY: 'auto',
        display: 'flex',
        flexDirection: 'column',
        gap: '16px'
      }}>
        {messages.map((msg, idx) => (
          <div
            key={idx}
            style={{
              alignSelf: msg.sender === 'USER' ? 'flex-end' : 'flex-start',
              maxWidth: msg.sender === 'USER' ? '70%' : '90%',
              display: 'flex',
              flexDirection: 'column',
              gap: '8px'
            }}
          >
            <div style={{
              background: msg.sender === 'USER' ? '#3b82f6' : '#131f37',
              color: '#f8fafc',
              padding: '12px 16px',
              borderRadius: '12px',
              border: msg.sender === 'USER' ? 'none' : '1px solid #2e416a',
              fontSize: '0.9rem',
              lineHeight: '1.5'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.75rem', color: msg.sender === 'USER' ? '#dbeafe' : '#60a5fa', marginBottom: '4px', fontWeight: '600' }}>
                {msg.sender === 'USER' ? 'Complainant / Investigator Query' : 'KSP Intelligence AI'}
              </div>
              <div dangerouslySetInnerHTML={{ __html: msg.text }} />

              {/* Explainability Caption Badge */}
              {msg.explainabilityCaption && (
                <div style={{ marginTop: '10px', paddingTop: '8px', borderTop: '1px solid rgba(255,255,255,0.1)', fontSize: '0.75rem', color: '#94a3b8', display: 'flex', alignItems: 'center', gap: '6px' }}>
                  <Shield size={12} color="#60a5fa" />
                  <span>{msg.explainabilityCaption}</span>
                </div>
              )}
            </div>

            {/* Inline Matching FIR Records Preview Table */}
            {msg.records && msg.records.length > 0 && (
              <div style={{
                background: '#0b1329',
                border: '1px solid #2e416a',
                borderRadius: '8px',
                padding: '12px',
                marginTop: '4px'
              }}>
                <div
                  onClick={() => setExpandedTableIndex(expandedTableIndex === idx ? null : idx)}
                  style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', cursor: 'pointer', color: '#60a5fa', fontSize: '0.8rem', fontWeight: '600' }}
                >
                  <span style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
                    <Database size={14} /> View {msg.recordsCount} Matching KSP FIR Source Records
                  </span>
                  {expandedTableIndex === idx ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                </div>

                {(expandedTableIndex === idx || idx === messages.length - 1) && (
                  <div style={{ marginTop: '10px', overflowX: 'auto' }}>
                    <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.78rem', color: '#cbd5e1' }}>
                      <thead>
                        <tr style={{ background: '#131f37', textTransform: 'uppercase', fontSize: '0.7rem', color: '#94a3b8' }}>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>Crime No</th>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>Date</th>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>District</th>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>Station</th>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>Crime Sub-Head</th>
                          <th style={{ padding: '6px 10px', textAlign: 'left' }}>Status</th>
                        </tr>
                      </thead>
                      <tbody>
                        {msg.records.map((c, rIdx) => (
                          <tr key={rIdx} style={{ borderBottom: '1px solid #1c2a4a' }}>
                            <td style={{ padding: '6px 10px', fontWeight: 'bold', color: '#f8fafc' }}>{c.crimeNo}</td>
                            <td style={{ padding: '6px 10px' }}>{c.crimeRegisteredDate}</td>
                            <td style={{ padding: '6px 10px' }}>{c.districtName}</td>
                            <td style={{ padding: '6px 10px' }}>{c.policeStationName}</td>
                            <td style={{ padding: '6px 10px' }}>{c.crimeSubHeadName}</td>
                            <td style={{ padding: '6px 10px' }}>
                              <span style={{
                                padding: '2px 6px',
                                borderRadius: '4px',
                                background: c.caseStatusName === 'Under Investigation' ? 'rgba(245, 158, 11, 0.2)' : 'rgba(16, 185, 129, 0.2)',
                                color: c.caseStatusName === 'Under Investigation' ? '#f59e0b' : '#10b981'
                              }}>
                                {c.caseStatusName}
                              </span>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                )}
              </div>
            )}

          </div>
        ))}
        {loading && (
          <div style={{ color: '#94a3b8', fontSize: '0.85rem', fontStyle: 'italic' }}>
            Querying Groq LLM & searching KSP CaseMaster records...
          </div>
        )}
      </div>

      {/* Input Form */}
      <form
        onSubmit={(e) => { e.preventDefault(); handleSendMessage(); }}
        style={{ display: 'flex', gap: '12px' }}
      >
        <input
          type="text"
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          placeholder="Ask a question about Karnataka crime statistics, districts, or FIR records..."
          style={{
            flex: 1,
            background: '#131f37',
            color: '#f8fafc',
            border: '1px solid #2e416a',
            padding: '14px 18px',
            borderRadius: '10px',
            fontSize: '0.9rem',
            outline: 'none'
          }}
        />
        <button
          type="submit"
          disabled={loading || !inputMessage.trim()}
          style={{
            background: '#3b82f6',
            color: '#ffffff',
            border: 'none',
            padding: '0 24px',
            borderRadius: '10px',
            fontWeight: '600',
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            opacity: loading || !inputMessage.trim() ? 0.6 : 1
          }}
        >
          <Send size={16} />
          Ask Assistant
        </button>
      </form>

    </div>
  );
}
