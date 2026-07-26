import React from 'react';
import { ShieldAlert, BarChart3, Network, MessageSquare, Activity } from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab }) {
  return (
    <header style={{
      background: 'linear-gradient(180deg, #131f37 0%, #0b1329 100%)',
      borderBottom: '1px solid #2e416a',
      padding: '12px 24px',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      flexWrap: 'wrap',
      gap: '16px'
    }}>
      {/* KSP Branding Header */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '14px' }}>
        <div style={{
          background: 'linear-gradient(135deg, #1e40af 0%, #1e3a8a 100%)',
          width: '42px',
          height: '42px',
          borderRadius: '10px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          border: '1px solid #3b82f6',
          boxShadow: '0 0 12px rgba(59, 130, 246, 0.4)'
        }}>
          <ShieldAlert size={26} color="#ffffff" />
        </div>
        <div>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <h1 style={{ fontSize: '1.25rem', fontWeight: '700', color: '#f8fafc', letterSpacing: '0.5px' }}>
              KSP Crime Intelligence Platform
            </h1>
            <span style={{
              fontSize: '0.7rem',
              background: 'rgba(59, 130, 246, 0.15)',
              color: '#60a5fa',
              padding: '2px 8px',
              borderRadius: '12px',
              border: '1px solid rgba(96, 165, 250, 0.3)',
              fontWeight: '600'
            }}>
              KSP Datathon 2026 PS-2
            </span>
          </div>
          <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
            Karnataka State Police • AI-Driven Analytics, Hotspot Detection & Transparent Explainability
          </p>
        </div>
      </div>

      {/* Navigation Tabs */}
      <div style={{
        display: 'flex',
        alignItems: 'center',
        background: '#1c2a4a',
        padding: '4px',
        borderRadius: '8px',
        border: '1px solid #2e416a'
      }}>
        <button
          onClick={() => setActiveTab('dashboard')}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            padding: '8px 16px',
            borderRadius: '6px',
            border: 'none',
            background: activeTab === 'dashboard' ? '#3b82f6' : 'transparent',
            color: activeTab === 'dashboard' ? '#ffffff' : '#94a3b8',
            fontSize: '0.85rem',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.2s'
          }}
        >
          <BarChart3 size={16} />
          Analytics Dashboard
        </button>

        <button
          onClick={() => setActiveTab('network')}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            padding: '8px 16px',
            borderRadius: '6px',
            border: 'none',
            background: activeTab === 'network' ? '#3b82f6' : 'transparent',
            color: activeTab === 'network' ? '#ffffff' : '#94a3b8',
            fontSize: '0.85rem',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.2s'
          }}
        >
          <Network size={16} />
          Offender Network Graph
        </button>

        <button
          onClick={() => setActiveTab('chat')}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            padding: '8px 16px',
            borderRadius: '6px',
            border: 'none',
            background: activeTab === 'chat' ? '#3b82f6' : 'transparent',
            color: activeTab === 'chat' ? '#ffffff' : '#94a3b8',
            fontSize: '0.85rem',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.2s'
          }}
        >
          <MessageSquare size={16} />
          AI Crime Assistant
        </button>
      </div>

      {/* System Status Pill */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.75rem', color: '#10b981' }}>
        <Activity size={14} className="pulse" />
        <span>Spring Boot & Groq AI Connected</span>
      </div>
    </header>
  );
}
