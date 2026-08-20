import React, { useEffect, useState } from 'react';
import { api } from '../api/apiClient';

export default function ProductsPage() {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [error, setError] = useState('');

  const [formData, setFormData] = useState({
    name: '',
    sku: '',
    categoryId: '',
    unit: 'pcs',
    sellingPrice: '',
    costPrice: '',
    minimumStockLevel: '0',
    productType: 'TRADING',
  });

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, []);

  const fetchProducts = async () => {
    try {
      const data = await api.get('/products');
      setProducts(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const fetchCategories = async () => {
    try {
      const data = await api.get('/categories');
      setCategories(data);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setError('');
      await api.post('/products', {
        ...formData,
        categoryId: Number(formData.categoryId),
        sellingPrice: Number(formData.sellingPrice),
        costPrice: Number(formData.costPrice),
        minimumStockLevel: Number(formData.minimumStockLevel),
      });
      setShowModal(false);
      fetchProducts();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div>
      <div className="action-bar">
        <h2>Products</h2>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>+ Add Product</button>
      </div>

      {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>SKU</th>
              <th>Product Name</th>
              <th>Category</th>
              <th>Type</th>
              <th>Cost Price</th>
              <th>Selling Price</th>
              <th>Current Stock</th>
              <th>Min Stock</th>
            </tr>
          </thead>
          <tbody>
            {products.map((p) => (
              <tr key={p.id}>
                <td><code>{p.sku}</code></td>
                <td><strong>{p.name}</strong></td>
                <td>{p.category?.name || '-'}</td>
                <td><span className="badge badge-success">{p.productType}</span></td>
                <td>₹{p.costPrice}</td>
                <td>₹{p.sellingPrice}</td>
                <td style={{ color: p.currentStock < p.minimumStockLevel ? 'red' : 'inherit', fontWeight: 'bold' }}>
                  {p.currentStock} {p.unit}
                </td>
                <td>{p.minimumStockLevel} {p.unit}</td>
              </tr>
            ))}
            {products.length === 0 && (
              <tr>
                <td colSpan="8" style={{ textAlign: 'center', color: '#64748b' }}>No products found</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">Add New Product</div>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Product Name *</label>
                <input className="form-control" required value={formData.name} onChange={(e) => setFormData({...formData, name: e.target.value})} />
              </div>
              <div className="form-group">
                <label>SKU / Code *</label>
                <input className="form-control" required value={formData.sku} onChange={(e) => setFormData({...formData, sku: e.target.value})} />
              </div>
              <div className="form-group">
                <label>Category *</label>
                <select className="form-control" required value={formData.categoryId} onChange={(e) => setFormData({...formData, categoryId: e.target.value})}>
                  <option value="">Select Category</option>
                  {categories.map((c) => (
                    <option key={c.id} value={c.id}>{c.name}</option>
                  ))}
                </select>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Unit (pcs, kg, etc.) *</label>
                  <input className="form-control" required value={formData.unit} onChange={(e) => setFormData({...formData, unit: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Product Type *</label>
                  <select className="form-control" value={formData.productType} onChange={(e) => setFormData({...formData, productType: e.target.value})}>
                    <option value="TRADING">TRADING</option>
                    <option value="FINISHED">FINISHED</option>
                  </select>
                </div>
              </div>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr', gap: '1rem' }}>
                <div className="form-group">
                  <label>Cost Price (₹) *</label>
                  <input type="number" step="0.01" className="form-control" required value={formData.costPrice} onChange={(e) => setFormData({...formData, costPrice: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Selling Price (₹) *</label>
                  <input type="number" step="0.01" className="form-control" required value={formData.sellingPrice} onChange={(e) => setFormData({...formData, sellingPrice: e.target.value})} />
                </div>
                <div className="form-group">
                  <label>Min Stock *</label>
                  <input type="number" className="form-control" required value={formData.minimumStockLevel} onChange={(e) => setFormData({...formData, minimumStockLevel: e.target.value})} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Save Product</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
