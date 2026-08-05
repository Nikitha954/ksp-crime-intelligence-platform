import React, { useState, useEffect } from 'react';
import { Network, UserCheck, Shield, AlertCircle, Info, Search, GitCommit } from 'lucide-react';

const API_BASE = 'https://ksp-crime-intelligence-platform-1-e024.onrender.com/api';

export default function NetworkView() {
  const [offenders, setOffenders] = useState([]);
  const [selectedAccusedId, setSelectedAccusedId] = useState(null);
  const [graphData, setGraphData] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchTopOffenders();
  }, []);

  const fetchTopOffenders = async () => {
    try {
      const res = await fetch(`${API_BASE}/network/offenders`);
      const data = await res.json();
      setOffenders(data || []);
      if (data && data.length > 0) {
        setSelectedAccusedId(data[0].accusedMasterId);
        fetchGraph(data[0].accusedMasterId);
      }
    } catch (err) {
      console.error("Error fetching offenders:", err);
    }
  };

  const fetchGraph = async (accusedId) => {
    setLoading(true);
    try {
      const res = await fetch(`${API_BASE}/network/${accusedId}`);
      const data = await res.json();
      setGraphData(data);
    } catch (err) {
      console.error("Error fetching network graph:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSelectOffender = (e) => {
    const id = parseInt(e.target.value);
    setSelectedAccusedId(id);
    fetchGraph(id);
  };

  return (
    <div style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* Selector Header */}
      <div style={{
        background: '#131f37',
        border: '1px solid #2e416a',
        borderRadius: '12px',
        padding: '16px 20px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '16px'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <Network size={22} color="#3b82f6" />
          <div>
            <h2 style={{ fontSize: '1.1rem', color: '#f8fafc' }}>Repeat Offender & Co-Accused Network Graph</h2>
            <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
              Link analysis tracing shared FIR cases, co-accused accomplices, and operating police station jurisdictions.
            </p>
          </div>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <label style={{ fontSize: '0.85rem', color: '#94a3b8' }}>Select Target Offender:</label>
          <select
            value={selectedAccusedId || ''}
            onChange={handleSelectOffender}
            style={{
              background: '#1c2a4a',
              color: '#f8fafc',
              border: '1px solid #3b82f6',
              padding: '8px 14px',
              borderRadius: '8px',
              fontSize: '0.85rem',
              fontWeight: '600'
            }}
          >
            {offenders.map(o => (
              <option key={o.accusedMasterId} value={o.accusedMasterId}>
                {o.accusedName} ({o.personID}) — {o.caseCount} Linked Cases
              </option>
            ))}
          </select>
        </div>
      </div>

      {/* Main Canvas & Detail Sidebar */}
      <div style={{ display: 'grid', gridTemplateColumns: '3fr 1fr', gap: '20px' }}>
        
        {/* SVG Graph Visualizer */}
        <div style={{
          background: '#1c2a4a',
          border: '1px solid #2e416a',
          borderRadius: '12px',
          padding: '20px',
          minHeight: '520px',
          display: 'flex',
          flexDirection: 'column'
        }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '16px', fontSize: '0.8rem', color: '#94a3b8' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '12px', height: '12px', background: '#ef4444', borderRadius: '50%', display: 'inline-block' }}></span> Target Accused
              </span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '12px', height: '12px', background: '#f59e0b', borderRadius: '50%', display: 'inline-block' }}></span> Co-Accused
              </span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '12px', height: '12px', background: '#3b82f6', borderRadius: '50%', display: 'inline-block' }}></span> FIR Case
              </span>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <span style={{ width: '12px', height: '12px', background: '#10b981', borderRadius: '50%', display: 'inline-block' }}></span> Police Station
              </span>
            </div>
            <div className="explain-badge">
              <Info size={12} /> Traceable to official KSP PersonID
            </div>
          </div>

          {loading ? (
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '400px', color: '#94a3b8' }}>
              Loading network topology...
            </div>
          ) : graphData ? (
            <div style={{ flex: 1, position: 'relative', width: '100%', minHeight: '450px', background: '#0b1329', borderRadius: '8px', border: '1px solid #2e416a', overflow: 'hidden' }}>
              <svg width="100%" height="100%" viewBox="0 0 800 450" style={{ width: '100%', height: '450px' }}>
                <defs>
                  <marker id="arrow" viewBox="0 0 10 10" refX="18" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
                    <path d="M 0 0 L 10 5 L 0 10 z" fill="#64748b" />
                  </marker>
                </defs>

                {/* Render Nodes with Circular Layout around Center */}
                {(() => {
                  const nodes = graphData.nodes || [];
                  const edges = graphData.edges || [];
                  const center = { x: 400, y: 225 };
                  const radius = 170;

                  // Position target accused at center
                  const nodePositions = {};
                  nodePositions[graphData.nodes[0]?.id] = center;

                  const outerNodes = nodes.slice(1);
                  outerNodes.forEach((node, i) => {
                    const angle = (i / outerNodes.length) * 2 * Math.PI;
                    nodePositions[node.id] = {
                      x: center.x + radius * Math.cos(angle),
                      y: center.y + radius * Math.sin(angle)
                    };
                  });

                  return (
                    <g>
                      {/* Render Edges */}
                      {edges.map((e, idx) => {
                        const sourcePos = nodePositions[e.source] || center;
                        const targetPos = nodePositions[e.target] || center;
                        return (
                          <g key={idx}>
                            <line
                              x1={sourcePos.x}
                              y1={sourcePos.y}
                              x2={targetPos.x}
                              y2={targetPos.y}
                              stroke="#2e416a"
                              strokeWidth="2"
                              strokeDasharray={e.type === 'CO_ACCUSED_IN' ? '4 4' : 'none'}
                              markerEnd="url(#arrow)"
                            />
                          </g>
                        );
                      })}

                      {/* Render Nodes */}
                      {nodes.map((n) => {
                        const pos = nodePositions[n.id] || center;
                        const isTarget = n.type === 'accused';
                        const isCoAccused = n.type === 'co-accused';
                        const isCase = n.type === 'case';
                        
                        let fill = '#10b981';
                        if (isTarget) fill = '#ef4444';
                        else if (isCoAccused) fill = '#f59e0b';
                        else if (isCase) fill = '#3b82f6';

                        return (
                          <g key={n.id} style={{ cursor: 'pointer' }}>
                            <circle
                              cx={pos.x}
                              cy={pos.y}
                              r={isTarget ? 24 : 16}
                              fill={fill}
                              stroke="#ffffff"
                              strokeWidth={isTarget ? 3 : 1.5}
                              style={{ filter: isTarget ? 'drop-shadow(0 0 8px rgba(239, 68, 68, 0.6))' : 'none' }}
                            />
                            <text
                              x={pos.x}
                              y={pos.y + (isTarget ? 38 : 28)}
                              textAnchor="middle"
                              fill="#f8fafc"
                              fontSize={isTarget ? '12' : '10'}
                              fontWeight={isTarget ? 'bold' : 'normal'}
                            >
                              {n.label.length > 24 ? n.label.substring(0, 24) + '...' : n.label}
                            </text>
                          </g>
                        );
                      })}
                    </g>
                  );
                })()}
              </svg>
            </div>
          ) : null}
        </div>

        {/* Detail Offender Inspector Card */}
        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '12px', padding: '20px', display: 'flex', flexDirection: 'column', gap: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px', color: '#f8fafc', fontWeight: '600' }}>
            <Shield size={20} color="#ef4444" />
            <span>Offender Intelligence Profile</span>
          </div>

          {graphData && (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', fontSize: '0.85rem' }}>
              <div style={{ background: '#131f37', padding: '12px', borderRadius: '8px', border: '1px solid #2e416a' }}>
                <div style={{ color: '#94a3b8', fontSize: '0.75rem' }}>Accused Name</div>
                <div style={{ fontSize: '1rem', fontWeight: 'bold', color: '#f8fafc', marginTop: '2px' }}>
                  {graphData.accusedName}
                </div>
                <div style={{ color: '#ef4444', fontSize: '0.75rem', marginTop: '4px', fontWeight: '600' }}>
                  PersonID: {graphData.personID}
                </div>
              </div>

              <div style={{ background: '#131f37', padding: '12px', borderRadius: '8px', border: '1px solid #2e416a' }}>
                <div style={{ color: '#94a3b8', fontSize: '0.75rem' }}>Total Linked FIRs</div>
                <div style={{ fontSize: '1.4rem', fontWeight: 'bold', color: '#3b82f6', marginTop: '2px' }}>
                  {graphData.totalLinkedCases} Cases
                </div>
              </div>

              <div style={{ background: '#131f37', padding: '12px', borderRadius: '8px', border: '1px solid #2e416a' }}>
                <div style={{ color: '#94a3b8', fontSize: '0.75rem' }}>Network Nodes Breakdown</div>
                <ul style={{ listStyle: 'none', marginTop: '6px', color: '#cbd5e1', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                  <li>• Nodes: {graphData.nodes?.length || 0}</li>
                  <li>• Relationship Edges: {graphData.edges?.length || 0}</li>
                </ul>
              </div>

              <div className="explain-badge" style={{ marginTop: 'auto' }}>
                <Info size={12} /> Graph generated via joint ArrestSurrender & AccusedMasterID joins.
              </div>
            </div>
          )}
        </div>

      </div>
    </div>
  );
}
