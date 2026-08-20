import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function ManufacturingPage() {
  const [orders, setOrders] = useState([]);
  const [products, setProducts] = useState([]);
  const [rawMaterials, setRawMaterials] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [finishedProductId, setFinishedProductId] = useState('');
  const [quantityProduced, setQuantityProduced] = useState('1');
  const [orderDate, setOrderDate] = useState(new Date().toISOString().split('T')[0]);
  const [notes, setNotes] = useState('');

  const [materialsConsumed, setMaterialsConsumed] = useState([
    { rawMaterialId: '', quantityConsumed: '1' }
  ]);

  useEffect(() => {
    fetchOrders();
    fetchFinishedProducts();
    fetchRawMaterials();
  }, []);

  const fetchOrders = async () => {
    try {
      setOrders(await api.get('/manufacturing'));
    } catch (err) {
      setError(err.message);
    }
  };

  const fetchFinishedProducts = async () => {
    try {
      const data = await api.get('/products');
      setProducts(data.filter(p => p.productType === 'FINISHED'));
    } catch (e) {}
  };

  const fetchRawMaterials = async () => {
    try {
      setRawMaterials(await api.get('/raw-materials'));
    } catch (e) {}
  };

  const addMaterialRow = () => {
    setMaterialsConsumed([...materialsConsumed, { rawMaterialId: '', quantityConsumed: '1' }]);
  };

  const removeMaterialRow = (index) => {
    setMaterialsConsumed(materialsConsumed.filter((_, i) => i !== index));
  };

  const handleMaterialChange = (index, field, value) => {
    const newMats = [...materialsConsumed];
    newMats[index][field] = value;
    setMaterialsConsumed(newMats);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      const formattedMaterials = materialsConsumed.map(m => ({
        rawMaterialId: Number(m.rawMaterialId),
        quantityConsumed: Number(m.quantityConsumed)
      }));

      await api.post('/manufacturing', {
        finishedProductId: Number(finishedProductId),
        quantityProduced: Number(quantityProduced),
        orderDate,
        notes,
        materialsConsumed: formattedMaterials
      });

      setShowModal(false);
      fetchOrders();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Manufacturing Orders</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ New Production Run</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Order ID</th>
              <th>Date</th>
              <th>Finished Product</th>
              <th>Qty Produced</th>
              <th>Materials Consumed</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((o) => (
              <tr key={o.id}>
                <td>#{o.id}</td>
                <td>{o.orderDate}</td>
                <td><strong>{o.finishedProduct?.name}</strong></td>
                <td style={{ color: '#16a34a', fontWeight: 'bold' }}>+{o.quantityProduced} {o.finishedProduct?.unit}</td>
                <td>
                  {o.materialsConsumed?.map(m => (
                    <div key={m.id} style={{ fontSize: '0.85rem' }}>
                      • {m.rawMaterialName}: <span style={{ color: '#dc2626' }}>-{m.quantityConsumed}</span>
                    </div>
                  ))}
                </td>
                <td><span className="badge badge-success">{o.status}</span></td>
              </tr>
            ))}
            {orders.length === 0 && (
              <tr>
                <td colSpan="6" style={{ textAlign: 'center', color: '#64748b' }}>No manufacturing orders found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '650px' }}>
            <div className="modal-header">Record Production Run</div>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Finished Product to Produce *</label>
                  <select className="form-control" required value={finishedProductId} onChange={(e) => setFinishedProductId(e.target.value)}>
                    <option value="">Select Finished Product</option>
                    {products.map(p => <option key={p.id} value={p.id}>{p.name} (Current: {p.currentStock} {p.unit})</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Qty Produced *</label>
                  <input type="number" step="0.01" className="form-control" required value={quantityProduced} onChange={(e) => setQuantityProduced(e.target.value)} />
                </div>
              </div>

              <div className="form-group">
                <label>Production Date *</label>
                <input type="date" className="form-control" required value={orderDate} onChange={(e) => setOrderDate(e.target.value)} />
              </div>

              <h4>Raw Materials Consumed</h4>
              {materialsConsumed.map((mat, idx) => (
                <div key={idx} style={{ display: 'grid', gridTemplateColumns: '2.5fr 1fr 40px', gap: '0.5rem', marginBottom: '0.5rem', alignItems: 'center' }}>
                  <select className="form-control" required value={mat.rawMaterialId} onChange={(e) => handleMaterialChange(idx, 'rawMaterialId', e.target.value)}>
                    <option value="">Select Raw Material</option>
                    {rawMaterials.map(r => <option key={r.id} value={r.id}>{r.name} (Avail: {r.currentStock} {r.unit})</option>)}
                  </select>

                  <input type="number" step="0.01" className="form-control" placeholder="Qty Consumed" required value={mat.quantityConsumed} onChange={(e) => handleMaterialChange(idx, 'quantityConsumed', e.target.value)} />

                  {materialsConsumed.length > 1 && (
                    <button type="button" className="btn btn-secondary" onClick={() => removeMaterialRow(idx)}>✕</button>
                  )}
                </div>
              ))}
              <button type="button" className="btn btn-secondary" style={{ marginBottom: '1rem', marginTop: '0.5rem' }} onClick={addMaterialRow}>+ Add Material</button>

              <div className="form-group">
                <label>Notes / Batch Info</label>
                <input className="form-control" value={notes} onChange={(e) => setNotes(e.target.value)} />
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Execute Production</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
