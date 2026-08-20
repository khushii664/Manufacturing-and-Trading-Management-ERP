import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    username: '',
    fullName: '',
    email: '',
    role: 'ADMIN',
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setUsers(await api.get('/users'));
    } catch (e) {
      setError(e.message);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/users', formData);
      setShowModal(false);
      fetchUsers();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Users & Role Management</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ Add User</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Full Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id}>
                <td>#{u.id}</td>
                <td><code>{u.username}</code></td>
                <td><strong>{u.fullName}</strong></td>
                <td>{u.email}</td>
                <td><span className="badge badge-success">{u.role}</span></td>
                <td>{u.active ? 'Active' : 'Inactive'}</td>
              </tr>
            ))}
            {users.length === 0 && (
              <tr>
                <td colSpan="6" style={{ textAlign: 'center', color: '#64748b' }}>No users configured</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Add New User</div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Username *</label>
                <input className="form-control" required value={formData.username} onChange={(e) => setFormData({...formData, username: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Full Name *</label>
                <input className="form-control" required value={formData.fullName} onChange={(e) => setFormData({...formData, fullName: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Email *</label>
                <input type="email" className="form-control" required value={formData.email} onChange={(e) => setFormData({...formData, email: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Role *</label>
                <select className="form-control" value={formData.role} onChange={(e) => setFormData({...formData, role: e.target.value})}>
                  <option value="ADMIN">ADMIN</option>
                  <option value="MANAGER">MANAGER</option>
                  <option value="ACCOUNTANT">ACCOUNTANT</option>
                  <option value="SALES">SALES</option>
                  <option value="PURCHASE">PURCHASE</option>
                  <option value="INVENTORY">INVENTORY</option>
                </select>
              </div>

              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save User</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
