import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function PurchasesPage() {
  const [purchases, setPurchases] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [products, setProducts] = useState([]);
  const [rawMaterials, setRawMaterials] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [supplierId, setSupplierId] = useState('');
  const [invoiceDate, setInvoiceDate] = useState(new Date().toISOString().split('T')[0]);
  const [amountPaid, setAmountPaid] = useState('0');

  const [items, setItems] = useState([
    { itemType: 'RAW_MATERIAL', itemId: '', quantity: '1', unitPrice: '0', tax: '0', discount: '0' }
  ]);

  useEffect(() => {
    fetchPurchases();
    fetchSuppliers();
    fetchProducts();
    fetchRawMaterials();
  }, []);

  const fetchPurchases = async () => {
    try {
      const data = await api.get('/purchases');
      setPurchases(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const fetchSuppliers = async () => {
    try { setSuppliers(await api.get('/suppliers')); } catch (e) {}
  };
  const fetchProducts = async () => {
    try { setProducts(await api.get('/products')); } catch (e) {}
  };
  const fetchRawMaterials = async () => {
    try { setRawMaterials(await api.get('/raw-materials')); } catch (e) {}
  };

  const addItemRow = () => {
    setItems([...items, { itemType: 'RAW_MATERIAL', itemId: '', quantity: '1', unitPrice: '0', tax: '0', discount: '0' }]);
  };

  const removeItemRow = (index) => {
    setItems(items.filter((_, i) => i !== index));
  };

  const handleItemChange = (index, field, value) => {
    const newItems = [...items];
    newItems[index][field] = value;
    setItems(newItems);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      const formattedItems = items.map(item => ({
        itemType: item.itemType,
        itemId: Number(item.itemId),
        quantity: Number(item.quantity),
        unitPrice: Number(item.unitPrice),
        tax: Number(item.tax || 0),
        discount: Number(item.discount || 0)
      }));

      await api.post('/purchases', {
        supplierId: Number(supplierId),
        invoiceDate,
        amountPaid: Number(amountPaid),
        items: formattedItems
      });

      setShowModal(false);
      fetchPurchases();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Purchases</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ New Purchase</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Invoice #</th>
              <th>Date</th>
              <th>Supplier</th>
              <th>Total</th>
              <th>Amount Paid</th>
              <th>Outstanding</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {purchases.map((p) => (
              <tr key={p.id}>
                <td><code>{p.invoiceNumber}</code></td>
                <td>{p.invoiceDate}</td>
                <td><strong>{p.supplier?.name}</strong></td>
                <td>₹{p.total}</td>
                <td>₹{p.amountPaid}</td>
                <td style={{ color: p.outstandingAmount > 0 ? '#dc2626' : 'inherit' }}>₹{p.outstandingAmount}</td>
                <td>
                  <span className={`badge ${p.status === 'PAID' ? 'badge-success' : p.status === 'PARTIAL' ? 'badge-warning' : 'badge-danger'}`}>
                    {p.status}
                  </span>
                </td>
              </tr>
            ))}
            {purchases.length === 0 && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No purchases found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '700px' }}>
            <div className="modal-header">Record New Purchase</div>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Supplier *</label>
                  <select className="form-control" required value={supplierId} onChange={(e) => setSupplierId(e.target.value)}>
                    <option value="">Select Supplier</option>
                    {suppliers.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Invoice Date *</label>
                  <input type="date" className="form-control" required value={invoiceDate} onChange={(e) => setInvoiceDate(e.target.value)} />
                </div>
              </div>

              <h4>Items Purchased</h4>
              {items.map((item, idx) => (
                <div key={idx} style={{ display: 'grid', gridTemplateColumns: '1.2fr 1.8fr 1fr 1fr 40px', gap: '0.5rem', marginBottom: '0.5rem', alignItems: 'center' }}>
                  <select className="form-control" value={item.itemType} onChange={(e) => handleItemChange(idx, 'itemType', e.target.value)}>
                    <option value="RAW_MATERIAL">Raw Material</option>
                    <option value="PRODUCT">Product</option>
                  </select>

                  <select className="form-control" required value={item.itemId} onChange={(e) => handleItemChange(idx, 'itemId', e.target.value)}>
                    <option value="">Select Item</option>
                    {item.itemType === 'RAW_MATERIAL'
                      ? rawMaterials.map(r => <option key={r.id} value={r.id}>{r.name} ({r.code})</option>)
                      : products.map(p => <option key={p.id} value={p.id}>{p.name} ({p.sku})</option>)
                    }
                  </select>

                  <input type="number" step="0.01" className="form-control" placeholder="Qty" required value={item.quantity} onChange={(e) => handleItemChange(idx, 'quantity', e.target.value)} />
                  <input type="number" step="0.01" className="form-control" placeholder="Unit Price" required value={item.unitPrice} onChange={(e) => handleItemChange(idx, 'unitPrice', e.target.value)} />

                  {items.length > 1 && (
                    <button type="button" className="btn btn-secondary" onClick={() => removeItemRow(idx)}>✕</button>
                  )}
                </div>
              ))}
              <button type="button" className="btn btn-secondary" style={{ marginBottom: '1rem', marginTop: '0.5rem' }} onClick={addItemRow}>+ Add Item</button>

              <div className="form-group">
                <label>Amount Paid Now (₹)</label>
                <input type="number" step="0.01" className="form-control" value={amountPaid} onChange={(e) => setAmountPaid(e.target.value)} />
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Purchase</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
