import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function RawMaterialsPage() {
  const [rawMaterials, setRawMaterials] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    name: '',
    code: '',
    unit: 'kg',
    purchasePrice: '',
    minimumStockLevel: '0',
    preferredSupplierId: '',
  });

  useEffect(() => {
    fetchRawMaterials();
    fetchSuppliers();
  }, []);

  const fetchRawMaterials = async () => {
    try {
      const data = await api.get('/raw-materials');
      setRawMaterials(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const fetchSuppliers = async () => {
    try {
      const data = await api.get('/suppliers');
      setSuppliers(data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/raw-materials', {
        ...formData,
        purchasePrice: Number(formData.purchasePrice),
        minimumStockLevel: Number(formData.minimumStockLevel),
        preferredSupplierId: formData.preferredSupplierId ? Number(formData.preferredSupplierId) : null,
      });
      setShowModal(false);
      fetchRawMaterials();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Raw Materials</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ Add Raw Material</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Code</th>
              <th>Material Name</th>
              <th>Unit</th>
              <th>Purchase Price</th>
              <th>Current Stock</th>
              <th>Min Stock</th>
              <th>Preferred Supplier</th>
            </tr>
          </thead>
          <tbody>
            {rawMaterials.map((r) => (
              <tr key={r.id}>
                <td><code>{r.code}</code></td>
                <td><strong>{r.name}</strong></td>
                <td>{r.unit}</td>
                <td>₹{r.purchasePrice}</td>
                <td style={{ color: r.currentStock < r.minimumStockLevel ? 'red' : 'inherit', fontWeight: 'bold' }}>
                  {r.currentStock} {r.unit}
                </td>
                <td>{r.minimumStockLevel} {r.unit}</td>
                <td>{r.preferredSupplier?.name || '-'}</td>
              </tr>
            ))}
            {rawMaterials.length === 0 && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No raw materials found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Add New Raw Material</div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Material Name *</label>
                <input className="form-control" required value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Material Code *</label>
                <input className="form-control" required value={formData.code} onChange={(e) => setFormData({...formData, code: e.target.value})} />
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Unit *</label>
                  <input className="form-control" required value={formData.unit} onChange={(e) => setFormData({...formData, unit: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Purchase Price (₹) *</label>
                  <input type="number" step="0.01" className="form-control" required value={formData.purchasePrice} onChange={(e) => setFormData({...formData, purchasePrice: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Min Stock *</label>
                  <input type="number" className="form-control" required value={formData.minimumStockLevel} onChange={(e) => setFormData({...formData, minimumStockLevel: e.target.value})} />
                </div>
              </div>
              <div className="form-group">
                <label>Preferred Supplier</label>
                <select className="form-control" value={formData.preferredSupplierId} onChange={(e) => setFormData({...formData, preferredSupplierId: e.target.value})}>
                  <option value="">None / Selective</option>
                  {suppliers.map((s) => (
                    <option key={s.id} value={s.id}>{s.name}</option>
                  ))}
                </select>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Raw Material</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
