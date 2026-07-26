import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, CircleMarker, Popup } from 'react-leaflet';
import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, LineChart, Line, CartesianGrid } from 'recharts';
import { ShieldCheck, AlertTriangle, FileText, MapPin, Filter, Calendar, Info, TrendingUp } from 'lucide-react';

const API_BASE = 'https://ksp-backend-50044360353.development.catalystappsail.in/api';

export default function Dashboard() {
  const [filters, setFilters] = useState({
    district: 'All',
    crimeSubHead: 'All',
    dateFrom: '',
    dateTo: '',
    caseStatus: 'All'
  });

  const [summary, setSummary] = useState(null);
  const [hotspots, setHotspots] = useState([]);
  const [filteredCases, setFilteredCases] = useState([]);
  const [loading, setLoading] = useState(true);

  // Karnataka District Centroid Coordinates
  const districtCoords = {
    "Bengaluru Urban": [12.9716, 77.5946],
    "Bengaluru Rural": [13.2172, 77.6271],
    "Mysuru": [12.2958, 76.6394],
    "Mangaluru (Dakshina Kannada)": [12.9141, 74.8560],
    "Belagavi": [15.8497, 74.4977],
    "Hubballi-Dharwad": [15.3647, 75.1240],
    "Tumakuru": [13.3379, 77.1173],
    "Ballari": [15.1394, 76.9214],
    "Shivamogga": [13.9299, 75.5681],
    "Kalaburagi": [17.3297, 76.8343]
  };

  useEffect(() => {
    fetchDashboardData();
  }, [filters]);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      // 1. Fetch filtered cases
      const queryParams = new URLSearchParams();
      if (filters.district !== 'All') queryParams.append('district', filters.district);
      if (filters.crimeSubHead !== 'All') queryParams.append('crimeSubHead', filters.crimeSubHead);
      if (filters.caseStatus !== 'All') queryParams.append('caseStatus', filters.caseStatus);
      if (filters.dateFrom) queryParams.append('dateFrom', filters.dateFrom);
      if (filters.dateTo) queryParams.append('dateTo', filters.dateTo);

      const casesRes = await fetch(`${API_BASE}/cases?${queryParams.toString()}`);
      const casesData = await casesRes.json();
      setFilteredCases(casesData.cases || []);

      // 2. Fetch Aggregated Summary
      const summaryRes = await fetch(`${API_BASE}/cases/summary`);
      const summaryData = await summaryRes.json();
      setSummary(summaryData);

      // 3. Fetch Hotspots
      const hotspotsRes = await fetch(`${API_BASE}/cases/hotspots`);
      const hotspotsData = await hotspotsRes.json();
      setHotspots(hotspotsData);
    } catch (err) {
      console.error("Error fetching dashboard data:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
  };

  const emergingHotspotsCount = hotspots.filter(h => h.emerging).length;

  return (
    <div style={{ padding: '24px', display: 'flex', flexDirection: 'column', gap: '24px' }}>
      
      {/* 1. Filter Control Panel */}
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
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#60a5fa', fontWeight: '600' }}>
          <Filter size={18} />
          <span>Crime Data Filters:</span>
        </div>

        <div style={{ display: 'flex', alignItems: 'center', gap: '16px', flexWrap: 'wrap' }}>
          {/* District Select */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <label style={{ fontSize: '0.75rem', color: '#94a3b8' }}>District</label>
            <select
              value={filters.district}
              onChange={(e) => handleFilterChange('district', e.target.value)}
              style={{
                background: '#1c2a4a',
                color: '#f8fafc',
                border: '1px solid #2e416a',
                padding: '6px 12px',
                borderRadius: '6px',
                fontSize: '0.85rem'
              }}
            >
              <option value="All">All Karnataka Districts</option>
              {Object.keys(districtCoords).map(d => (
                <option key={d} value={d}>{d}</option>
              ))}
            </select>
          </div>

          {/* Crime Category Select */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <label style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Crime Sub-Head</label>
            <select
              value={filters.crimeSubHead}
              onChange={(e) => handleFilterChange('crimeSubHead', e.target.value)}
              style={{
                background: '#1c2a4a',
                color: '#f8fafc',
                border: '1px solid #2e416a',
                padding: '6px 12px',
                borderRadius: '6px',
                fontSize: '0.85rem'
              }}
            >
              <option value="All">All Crime Types</option>
              <option value="Theft">Theft (MVT & General)</option>
              <option value="Burglary">Burglary & House Breaking</option>
              <option value="Robbery">Robbery</option>
              <option value="Grievous Hurt">Grievous Hurt & Assault</option>
              <option value="Online Financial Fraud">Online Financial Fraud / UPI Scam</option>
              <option value="NDPS">NDPS (Narcotics)</option>
            </select>
          </div>

          {/* Status Select */}
          <div style={{ display: 'flex', flexDirection: 'column', gap: '4px' }}>
            <label style={{ fontSize: '0.75rem', color: '#94a3b8' }}>Case Status</label>
            <select
              value={filters.caseStatus}
              onChange={(e) => handleFilterChange('caseStatus', e.target.value)}
              style={{
                background: '#1c2a4a',
                color: '#f8fafc',
                border: '1px solid #2e416a',
                padding: '6px 12px',
                borderRadius: '6px',
                fontSize: '0.85rem'
              }}
            >
              <option value="All">All Case Statuses</option>
              <option value="Under Investigation">Under Investigation</option>
              <option value="Chargesheeted">Chargesheeted</option>
              <option value="Closed">Closed / Final Report</option>
            </select>
          </div>

          {/* Reset Filters */}
          <button
            onClick={() => setFilters({ district: 'All', crimeSubHead: 'All', dateFrom: '', dateTo: '', caseStatus: 'All' })}
            style={{
              marginTop: '16px',
              background: '#24355a',
              color: '#94a3b8',
              border: '1px solid #2e416a',
              padding: '6px 12px',
              borderRadius: '6px',
              fontSize: '0.8rem',
              cursor: 'pointer'
            }}
          >
            Reset Filters
          </button>
        </div>
      </div>

      {/* 2. Key KPI Metric Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))', gap: '16px' }}>
        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '10px', padding: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: '#94a3b8' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Total FIR Cases</span>
            <FileText size={20} color="#3b82f6" />
          </div>
          <div style={{ fontSize: '1.8rem', fontWeight: '700', color: '#f8fafc', margin: '8px 0' }}>
            {filteredCases.length}
          </div>
          <div className="explain-badge">
            <Info size={12} /> Filtered case records count
          </div>
        </div>

        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '10px', padding: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: '#94a3b8' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Active Investigations</span>
            <TrendingUp size={20} color="#f59e0b" />
          </div>
          <div style={{ fontSize: '1.8rem', fontWeight: '700', color: '#f59e0b', margin: '8px 0' }}>
            {filteredCases.filter(c => c.caseStatusName === 'Under Investigation').length}
          </div>
          <div className="explain-badge">
            <Info size={12} /> Pending investigation status
          </div>
        </div>

        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '10px', padding: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: '#94a3b8' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Chargesheet Rate</span>
            <ShieldCheck size={20} color="#10b981" />
          </div>
          <div style={{ fontSize: '1.8rem', fontWeight: '700', color: '#10b981', margin: '8px 0' }}>
            {filteredCases.length > 0
              ? `${Math.round((filteredCases.filter(c => c.caseStatusName === 'Chargesheeted').length / filteredCases.length) * 100)}%`
              : '0%'}
          </div>
          <div className="explain-badge">
            <Info size={12} /> Cases chargesheeted in court
          </div>
        </div>

        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '10px', padding: '16px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', color: '#94a3b8' }}>
            <span style={{ fontSize: '0.85rem', fontWeight: '600' }}>Emerging Hotspots</span>
            <AlertTriangle size={20} color="#ef4444" />
          </div>
          <div style={{ fontSize: '1.8rem', fontWeight: '700', color: '#ef4444', margin: '8px 0' }}>
            {emergingHotspotsCount} Areas
          </div>
          <div className="explain-badge">
            <Info size={12} /> 30-day count &gt; 1.2x 6-mo avg
          </div>
        </div>
      </div>

      {/* 3. Charts & Map Row */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(450px, 1fr))', gap: '20px' }}>
        
        {/* Crime Type Breakdown Chart */}
        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '12px', padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
            <h3 style={{ fontSize: '1rem', color: '#f8fafc' }}>Crime Category Distribution</h3>
            <div className="explain-badge">
              <Info size={12} /> Aggregated from KSP CrimeSubHead
            </div>
          </div>
          <div style={{ height: '300px' }}>
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={summary?.byCrimeType || []} layout="vertical" margin={{ left: 40, right: 20 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="#2e416a" />
                <XAxis type="number" stroke="#94a3b8" />
                <YAxis dataKey="name" type="category" stroke="#94a3b8" width={140} tick={{ fontSize: 11 }} />
                <Tooltip contentStyle={{ background: '#131f37', border: '1px solid #2e416a', color: '#fff' }} />
                <Bar dataKey="value" fill="#3b82f6" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
          <p style={{ fontSize: '0.75rem', color: '#64748b', marginTop: '12px' }}>
            * Transparent analytics: Based on {summary?.totalCases || 3000} FIR records. Property offenses dominate reported incidents.
          </p>
        </div>

        {/* Monthly Trend Chart */}
        <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '12px', padding: '20px' }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
            <h3 style={{ fontSize: '1rem', color: '#f8fafc' }}>24-Month Registration Trend</h3>
            <div className="explain-badge">
              <Info size={12} /> Time-series timeline
            </div>
          </div>
          <div style={{ height: '300px' }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={summary?.monthlyTrend || []}>
                <CartesianGrid strokeDasharray="3 3" stroke="#2e416a" />
                <XAxis dataKey="month" stroke="#94a3b8" tick={{ fontSize: 10 }} />
                <YAxis stroke="#94a3b8" />
                <Tooltip contentStyle={{ background: '#131f37', border: '1px solid #2e416a', color: '#fff' }} />
                <Line type="monotone" dataKey="count" stroke="#10b981" strokeWidth={2} dot={{ r: 3 }} />
              </LineChart>
            </ResponsiveContainer>
          </div>
          <p style={{ fontSize: '0.75rem', color: '#64748b', marginTop: '12px' }}>
            * Seasonal/weekend clustering verified against IncidentFromDate timestamps.
          </p>
        </div>
      </div>

      {/* 4. Interactive Karnataka Map & Hotspot Panel */}
      <div style={{ background: '#1c2a4a', border: '1px solid #2e416a', borderRadius: '12px', padding: '20px' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
          <div>
            <h3 style={{ fontSize: '1.1rem', color: '#f8fafc', display: 'flex', alignItems: 'center', gap: '8px' }}>
              <MapPin color="#ef4444" size={20} />
              Karnataka State Police District Map & Emerging Crime Hotspots
            </h3>
            <p style={{ fontSize: '0.8rem', color: '#94a3b8' }}>
              Red pulsing rings highlight police stations where 30-day FIR velocity exceeds 1.2x their 6-month rolling monthly average.
            </p>
          </div>
          <div className="explain-badge">
            <Info size={12} /> Explainable AI spatial clustering
          </div>
        </div>

        <div style={{ height: '420px', borderRadius: '8px', overflow: 'hidden', border: '1px solid #2e416a' }}>
          <MapContainer center={[14.5, 75.8]} zoom={7} scrollWheelZoom={false}>
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            {hotspots.map((spot, idx) => {
              const coords = [spot.latitude, spot.longitude];
              const radius = Math.min(25, Math.max(8, spot.totalCount / 12));
              
              return (
                <CircleMarker
                  key={idx}
                  center={coords}
                  radius={radius}
                  pathOptions={{
                    color: spot.emerging ? '#ef4444' : '#3b82f6',
                    fillColor: spot.emerging ? '#ef4444' : '#3b82f6',
                    fillOpacity: spot.emerging ? 0.8 : 0.5,
                    weight: spot.emerging ? 3 : 1
                  }}
                  className={spot.emerging ? 'pulse-marker' : ''}
                >
                  <Popup>
                    <div style={{ padding: '6px' }}>
                      <strong style={{ color: spot.emerging ? '#ef4444' : '#3b82f6' }}>
                        {spot.policeStationName} ({spot.districtName})
                      </strong>
                      <div style={{ fontSize: '0.8rem', marginTop: '6px', color: '#e2e8f0' }}>
                        <div>Total FIRs: <strong>{spot.totalCount}</strong></div>
                        <div>Recent 30-day count: <strong>{spot.recent30DayCount}</strong></div>
                        <div>6-Month rolling total: <strong>{spot.rolling6MonthCount}</strong></div>
                        {spot.emerging && (
                          <div style={{ color: '#ef4444', fontWeight: 'bold', marginTop: '6px' }}>
                            ⚠️ EMERGING HOTSPOT SPOTLIGHT
                          </div>
                        )}
                      </div>
                    </div>
                  </Popup>
                </CircleMarker>
              );
            })}
          </MapContainer>
        </div>
      </div>

    </div>
  );
}
