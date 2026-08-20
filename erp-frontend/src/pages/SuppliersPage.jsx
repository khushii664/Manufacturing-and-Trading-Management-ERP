import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function SuppliersPage() {
  const [suppliers, setSuppliers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    name: '',
    phone: '',
    email: '',
    address: '',
    gstNumber: '',
    openingBalance: '0',
  });

  useEffect(() => {
    fetchSuppliers();
  }, []);

  const fetchSuppliers = async () => {
    try {
      const data = await api.get('/suppliers');
      setSuppliers(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/suppliers', {
        ...formData,
        openingBalance: Number(formData.openingBalance),
      });
      setShowModal(false);
      fetchSuppliers();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Suppliers</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ Add Supplier</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Supplier Name</th>
              <th>Phone</th>
              <th>Email</th>
              <th>GST / Tax No</th>
              <th>Opening Balance</th>
              <th>Current Payable Balance</th>
            </tr>
          </thead>
          <tbody>
            {suppliers.map((s) => (
              <tr key={s.id}>
                <td>{s.id}</td>
                <td><strong>{s.name}</strong></td>
                <td>{s.phone}</td>
                <td>{s.email || '-'}</td>
                <td><code>{s.gstNumber || '-'}</code></td>
                <td>₹{s.openingBalance}</td>
                <td style={{ color: '#dc2626', fontWeight: 'bold' }}>
                  ₹{s.currentPayableBalance}
                </td>
              </tr>
            ))}
            {suppliers.length === 0 && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No suppliers found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Add New Supplier</div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Supplier Name *</label>
                <input className="form-control" required value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Phone *</label>
                  <input className="form-control" required value={formData.phone} onChange={(e) => setFormData({...formData, phone: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Email</label>
                  <input type="email" className="form-control" value={formData.email} onChange={(e) => setFormData({...formData, email: e.target.value})} />
                </div>
              </div>
              <div className="form-group">
                <label>Address</label>
                <input className="form-control" value={formData.address} onChange={(e) => setFormData({...formData, address: e.target.value})} />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>GST / Tax Number</label>
                  <input className="form-control" value={formData.gstNumber} onChange={(e) => setFormData({...formData, gstNumber: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Opening Balance (₹) *</label>
                  <input type="number" className="form-control" required value={formData.openingBalance} onChange={(e) => setFormData({...formData, openingBalance: e.target.value})} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Supplier</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
