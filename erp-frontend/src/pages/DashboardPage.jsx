import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function DashboardPage() {
  const [summary, setSummary] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchSummary();
  }, []);

  const fetchSummary = async () => {
    try {
      setLoading(true);
      const data = await api.get('/dashboard/summary');
      setSummary(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading dashboard...</div>;
  if (error) return <div style={{ color: 'red' }}>Error: {error}</div>;

  return (
    <div>
      <div className="metrics-grid">
        <div className="card">
          <div className="metric-title">Total Sales</div>
          <div className="metric-value" style={{ color: '#2563eb' }}>
            ₹{summary?.totalSales?.toLocaleString('en-IN') || 0}
          </div>
        </div>

        <div className="card">
          <div className="metric-title">Total Purchases</div>
          <div className="metric-value" style={{ color: '#059669' }}>
            ₹{summary?.totalPurchases?.toLocaleString('en-IN') || 0}
          </div>
        </div>

        <div className="card">
          <div className="metric-title">Customer Receivables</div>
          <div className="metric-value" style={{ color: '#d97706' }}>
            ₹{summary?.totalReceivables?.toLocaleString('en-IN') || 0}
          </div>
        </div>

        <div className="card">
          <div className="metric-title">Supplier Payables</div>
          <div className="metric-value" style={{ color: '#dc2626' }}>
            ₹{summary?.totalPayables?.toLocaleString('en-IN') || 0}
          </div>
        </div>

        <div className="card">
          <div className="metric-title">Total Expenses</div>
          <div className="metric-value" style={{ color: '#7c3aed' }}>
            ₹{summary?.totalExpenses?.toLocaleString('en-IN') || 0}
          </div>
        </div>

        <div className="card">
          <div className="metric-title">Low Stock Alerts</div>
          <div className="metric-value" style={{ color: (summary?.lowStockProductsCount + summary?.lowStockRawMaterialsCount) > 0 ? '#dc2626' : '#16a34a' }}>
            {(summary?.lowStockProductsCount || 0) + (summary?.lowStockRawMaterialsCount || 0)}
          </div>
          <div style={{ fontSize: '0.8rem', color: '#64748b', marginTop: '0.25rem' }}>
            Products: {summary?.lowStockProductsCount || 0} | Raw Materials: {summary?.lowStockRawMaterialsCount || 0}
          </div>
        </div>
      </div>

      <div className="card">
        <h3>Master Metrics Summary</h3>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginTop: '1rem' }}>
          <div>
            <strong>Total Products:</strong> {summary?.totalProductsCount || 0}
          </div>
          <div>
            <strong>Total Customers:</strong> {summary?.totalCustomersCount || 0}
          </div>
          <div>
            <strong>Total Suppliers:</strong> {summary?.totalSuppliersCount || 0}
          </div>
        </div>
      </div>
    </div>
  );
}
