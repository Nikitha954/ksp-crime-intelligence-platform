import React, { useState } from 'react';
import Navbar from './Navbar';
import Dashboard from './Dashboard';
import NetworkView from './NetworkView';
import ChatView from './ChatView';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  return (
    <div style={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', background: '#0b1329' }}>
      <Navbar activeTab={activeTab} setActiveTab={setActiveTab} />
      
      <main style={{ flex: 1 }}>
        {activeTab === 'dashboard' && <Dashboard />}
        {activeTab === 'network' && <NetworkView />}
        {activeTab === 'chat' && <ChatView />}
      </main>

      <footer style={{
        background: '#0b1329',
        borderTop: '1px solid #2e416a',
        padding: '12px 24px',
        textAlign: 'center',
        fontSize: '0.75rem',
        color: '#64748b'
      }}>
        Karnataka State Police Datathon 2026 • Problem Statement 2: AI-Driven Crime Analytics & Visualization Platform
      </footer>
    </div>
  );
}

export default App;