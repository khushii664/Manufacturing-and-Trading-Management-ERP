import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function ExpensesPage() {
  const [expenses, setExpenses] = useState([]);
  const [categories, setCategories] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [showCatModal, setShowCatModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    categoryId: '',
    amount: '',
    expenseDate: new Date().toISOString().split('T')[0],
    paymentMethod: 'CASH',
    description: '',
    reference: '',
  });

  const [catName, setCatName] = useState('');
  const [catDesc, setCatDesc] = useState('');

  useEffect(() => {
    fetchExpenses();
    fetchCategories();
  }, []);

  const fetchExpenses = async () => {
    try { setExpenses(await api.get('/expenses')); } catch (e) { setError(e.message); }
  };

  const fetchCategories = async () => {
    try { setCategories(await api.get('/expense-categories')); } catch (e) {}
  };

  const handleExpenseSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/expenses', {
        ...formData,
        categoryId: Number(formData.categoryId),
        amount: Number(formData.amount),
      });
      setShowModal(false);
      fetchExpenses();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleCatSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/expense-categories', { name: catName, description: catDesc });
      setShowCatModal(false);
      setCatName('');
      setCatDesc('');
      fetchCategories();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Expenses</h2>
        <div style={{ display: 'flex', gap: '0.5rem' }}>
          <button className="btn btn-secondary" onClick={() => setShowCatModal(true)}>+ Add Category</button>
          <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ Record Expense</button>
        </div>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Date</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Payment Method</th>
              <th>Reference</th>
              <th>Description</th>
            </tr>
          </thead>
          <tbody>
            {expenses.map((ex) => (
              <tr key={ex.id}>
                <td>#{ex.id}</td>
                <td>{ex.expenseDate}</td>
                <td><strong>{ex.category?.name}</strong></td>
                <td style={{ color: '#7c3aed', fontWeight: 'bold' }}>₹{ex.amount}</td>
                <td><span className="badge badge-warning">{ex.paymentMethod}</span></td>
                <td><code>{ex.reference || '-'}</code></td>
                <td>{ex.description || '-'}</td>
              </tr>
            ))}
            {expenses.length === 0 && (
              <tr>
                <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>No expenses recorded</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Record Expense Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Record Expense</div>
            <form onSubmit={handleExpenseSubmit}>
              <div className="form-group">
                <label>Expense Category *</label>
                <select className="form-control" required value={formData.categoryId} onChange={(e) => setFormData({...formData, categoryId: e.target.value})}>
                  <option value="">Select Expense Category</option>
                  {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                </select>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Amount (₹) *</label>
                  <input type="number" step="0.01" className="form-control" required value={formData.amount} onChange={(e) => setFormData({...formData, amount: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Expense Date *</label>
                  <input type="date" className="form-control" required value={formData.expenseDate} onChange={(e) => setFormData({...formData, expenseDate: e.target.value})} />
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
                  <label>Reference #</label>
                  <input className="form-control" value={formData.reference} onChange={(e) => setFormData({...formData, reference: e.target.value})} />
                </div>
              </div>

              <div className="form-group">
                <label>Description</label>
                <input className="form-control" value={formData.description} onChange={(e) => setFormData({...formData, description: e.target.value})} />
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Expense</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Add Expense Category Modal */}
      {showCatModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Add Expense Category</div>
            <form onSubmit={handleCatSubmit}>
              <div className="form-group">
                <label>Category Name *</label>
                <input className="form-control" required value={catName} onChange={(e) => setCatName(e.target.value)} placeholder="Rent, Electricity, Salaries, etc." />
              </div>
              <div className="form-group">
                <label>Description</label>
                <input className="form-control" value={catDesc} onChange={(e) => setCatDesc(e.target.value)} />
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowCatModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Category</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
