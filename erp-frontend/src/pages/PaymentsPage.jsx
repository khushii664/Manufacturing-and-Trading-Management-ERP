import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function PaymentsPage() {
  const [activeSubTab, setActiveSubTab] = useState('customers');
  const [customerPayments, setCustomerPayments] = useState([]);
  const [supplierPayments, setSupplierPayments] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    entityId: '',
    amount: '',
    paymentDate: new Date().toISOString().split('T')[0],
    paymentMethod: 'CASH',
    reference: '',
    notes: '',
  });

  useEffect(() => {
    fetchPayments();
    fetchEntities();
  }, [activeSubTab]);

  const fetchPayments = async () => {
    try {
      if (activeSubTab === 'customers') {
        setCustomerPayments(await api.get('/payments/customers'));
      } else {
        setSupplierPayments(await api.get('/payments/suppliers'));
      }
    } catch (e) {
      setError(e.message);
    }
  };

  const fetchEntities = async () => {
    try {
      if (activeSubTab === 'customers') {
        setCustomers(await api.get('/customers'));
      } else {
        setSuppliers(await api.get('/suppliers'));
      }
    } catch (e) {}
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      if (activeSubTab === 'customers') {
        await api.post('/payments/customers', {
          customerId: Number(formData.entityId),
          amount: Number(formData.amount),
          paymentDate: formData.paymentDate,
          paymentMethod: formData.paymentMethod,
          reference: formData.reference,
          notes: formData.notes,
        });
      } else {
        await api.post('/payments/suppliers', {
          supplierId: Number(formData.entityId),
          amount: Number(formData.amount),
          paymentDate: formData.paymentDate,
          paymentMethod: formData.paymentMethod,
          reference: formData.reference,
          notes: formData.notes,
        });
      }

      setShowModal(false);
      fetchPayments();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <div style={{ display: 'flex', gap: '1rem' }}>
          <button className={`btn ${activeSubTab === 'customers' ? 'btn-primary' : 'btn-secondary'}`} onClick={() => setActiveSubTab('customers')}>
            Customer Payments
          </button>
          <button className={`btn ${activeSubTab === 'suppliers' ? 'btn-primary' : 'btn-secondary'}`} onClick={() => setActiveSubTab('suppliers')}>
            Supplier Payments
          </button>
        </div>

        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          + Record {activeSubTab === 'customers' ? 'Customer' : 'Supplier'} Payment
        </button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Date</th>
              <th>{activeSubTab === 'customers' ? 'Customer' : 'Supplier'} Name</th>
              <th>Amount Paid</th>
              <th>Payment Method</th>
              <th>Reference</th>
              <th>Notes</th>
            </tr>
          </thead>
          <tbody>
            {activeSubTab === 'customers' ? (
              customerPayments.map(p => (
                <tr key={p.id}>
                  <td>#{p.id}</td>
                  <td>{p.paymentDate}</td>
                  <td><strong>{p.customer?.name}</strong></td>
                  <td style={{ color: '#16a34a', fontWeight: 'bold' }}>₹{p.amount}</td>
                  <td><span className="badge badge-success">{p.paymentMethod}</span></td>
                  <td><code>{p.reference || '-'}</code></td>
                  <td>{p.notes || '-'}</td>
                </tr>
              ))
            ) : (
              supplierPayments.map(p => (
                <tr key={p.id}>
                  <td>#{p.id}</td>
                  <td>{p.paymentDate}</td>
                  <td><strong>{p.supplier?.name}</strong></td>
                  <td style={{ color: '#16a34a', fontWeight: 'bold' }}>₹{p.amount}</td>
                  <td><span className="badge badge-success">{p.paymentMethod}</span></td>
                  <td><code>{p.reference || '-'}</code></td>
                  <td>{p.notes || '-'}</td>
                </tr>
              ))
            )}
            {((activeSubTab === 'customers' && customerPayments.length === 0) || (activeSubTab === 'suppliers' && supplierPayments.length === 0)) && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No payment transactions recorded</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Record {activeSubTab === 'customers' ? 'Customer' : 'Supplier'} Payment</div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Select {activeSubTab === 'customers' ? 'Customer' : 'Supplier'} *</label>
                <select className="form-control" required value={formData.entityId} onChange={(e) => setFormData({...formData, entityId: e.target.value})}>
                  <option value="">Select Option</option>
                  {activeSubTab === 'customers'
                    ? customers.map(c => <option key={c.id} value={c.id}>{c.name} (Due: ₹{c.currentReceivableBalance})</option>)
                    : suppliers.map(s => <option key={s.id} value={s.id}>{s.name} (Due: ₹{s.currentPayableBalance})</option>)
                  }
                </select>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Amount (₹) *</label>
                  <input type="number" step="0.01" className="form-control" required value={formData.amount} onChange={(e) => setFormData({...formData, amount: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Payment Date *</label>
                  <input type="date" className="form-control" required value={formData.paymentDate} onChange={(e) => setFormData({...formData, paymentDate: e.target.value})} />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Payment Method *</label>
                  <select className="form-control" value={formData.paymentMethod} onChange={(e) => setFormData({...formData, paymentMethod: e.target.value})}>
                    <option value="CASH">CASH</option>
                    <option value="BANK_TRANSFER">BANK TRANSFER</option>
                    <option value="UPI">UPI</option>
                    <option value="CHEQUE">CHEQUE</option>
                    <option value="OTHER">OTHER</option>
                  </select>
                </div>
                <div className="form-group">
                  <label>Reference / Txn #</label>
                  <input className="form-control" value={formData.reference} onChange={(e) => setFormData({...formData, reference: e.target.value})} />
                </div>
              </div>

              <div className="form-group">
                <label>Notes</label>
                <input className="form-control" value={formData.notes} onChange={(e) => setFormData({...formData, notes: e.target.value})} />
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Payment</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
