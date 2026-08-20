import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function SalesPage() {
  const [sales, setSales] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [customerId, setCustomerId] = useState('');
  const [invoiceDate, setInvoiceDate] = useState(new Date().toISOString().split('T')[0]);
  const [amountPaid, setAmountPaid] = useState('0');

  const [items, setItems] = useState([
    { productId: '', quantity: '1', unitPrice: '0', tax: '0', discount: '0' }
  ]);

  useEffect(() => {
    fetchSales();
    fetchCustomers();
    fetchProducts();
  }, []);

  const fetchSales = async () => {
    try {
      const data = await api.get('/sales');
      setSales(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const fetchCustomers = async () => {
    try { setCustomers(await api.get('/customers')); } catch (e) {}
  };
  const fetchProducts = async () => {
    try { setProducts(await api.get('/products')); } catch (e) {}
  };

  const addItemRow = () => {
    setItems([...items, { productId: '', quantity: '1', unitPrice: '0', tax: '0', discount: '0' }]);
  };

  const removeItemRow = (index) => {
    setItems(items.filter((_, i) => i !== index));
  };

  const handleItemChange = (index, field, value) => {
    const newItems = [...items];
    newItems[index][field] = value;

    if (field === 'productId') {
      const selectedProd = products.find(p => p.id === Number(value));
      if (selectedProd) {
        newItems[index].unitPrice = selectedProd.sellingPrice;
      }
    }

    setItems(newItems);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      const formattedItems = items.map(item => ({
        productId: Number(item.productId),
        quantity: Number(item.quantity),
        unitPrice: Number(item.unitPrice),
        tax: Number(item.tax || 0),
        discount: Number(item.discount || 0)
      }));

      await api.post('/sales', {
        customerId: Number(customerId),
        invoiceDate,
        amountPaid: Number(amountPaid),
        items: formattedItems
      });

      setShowModal(false);
      fetchSales();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Sales</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ New Sale</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Invoice #</th>
              <th>Date</th>
              <th>Customer</th>
              <th>Total</th>
              <th>Amount Paid</th>
              <th>Outstanding</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {sales.map((s) => (
              <tr key={s.id}>
                <td><code>{s.invoiceNumber}</code></td>
                <td>{s.invoiceDate}</td>
                <td><strong>{s.customer?.name}</strong></td>
                <td>₹{s.total}</td>
                <td>₹{s.amountPaid}</td>
                <td style={{ color: s.outstandingAmount > 0 ? '#d97706' : 'inherit' }}>₹{s.outstandingAmount}</td>
                <td>
                  <span className={`badge ${s.status === 'PAID' ? 'badge-success' : s.status === 'PARTIAL' ? 'badge-warning' : 'badge-danger'}`}>
                    {s.status}
                  </span>
                </td>
              </tr>
            ))}
            {sales.length === 0 && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No sales found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content" style={{ maxWidth: '700px' }}>
            <div className="modal-header">Create Sales Order / Invoice</div>
            <form onSubmit={handleSubmit}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Customer *</label>
                  <select className="form-control" required value={customerId} onChange={(e) => setCustomerId(e.target.value)}>
                    <option value="">Select Customer</option>
                    {customers.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Invoice Date *</label>
                  <input type="date" className="form-control" required value={invoiceDate} onChange={(e) => setInvoiceDate(e.target.value)} />
                </div>
              </div>

              <h4>Items Sold</h4>
              {items.map((item, idx) => {
                const prod = products.find(p => p.id === Number(item.productId));
                return (
                  <div key={idx} style={{ display: 'grid', gridTemplateColumns: '2.5fr 1fr 1fr 40px', gap: '0.5rem', marginBottom: '0.5rem', alignItems: 'center' }}>
                    <select className="form-control" required value={item.productId} onChange={(e) => handleItemChange(idx, 'productId', e.target.value)}>
                      <option value="">Select Product</option>
                      {products.map(p => <option key={p.id} value={p.id}>{p.name} (Stock: {p.currentStock} {p.unit})</option>)}
                    </select>

                    <input type="number" step="0.01" className="form-control" placeholder="Qty" required value={item.quantity} onChange={(e) => handleItemChange(idx, 'quantity', e.target.value)} />
                    <input type="number" step="0.01" className="form-control" placeholder="Selling Price" required value={item.unitPrice} onChange={(e) => handleItemChange(idx, 'unitPrice', e.target.value)} />

                    {items.length > 1 && (
                      <button type="button" className="btn btn-secondary" onClick={() => removeItemRow(idx)}>✕</button>
                    )}
                  </div>
                );
              })}
              <button type="button" className="btn btn-secondary" style={{ marginBottom: '1rem', marginTop: '0.5rem' }} onClick={addItemRow}>+ Add Item</button>

              <div className="form-group">
                <label>Amount Paid Now (₹)</label>
                <input type="number" step="0.01" className="form-control" value={amountPaid} onChange={(e) => setAmountPaid(e.target.value)} />
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Sale</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
