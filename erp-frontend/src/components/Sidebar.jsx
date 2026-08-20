import React from 'react';

const navItems = [
  { id: 'dashboard', label: 'Dashboard', icon: '📊' },
  { id: 'categories', label: 'Categories', icon: '🏷️' },
  { id: 'products', label: 'Products', icon: '📦' },
  { id: 'raw-materials', label: 'Raw Materials', icon: '🧱' },
  { id: 'suppliers', label: 'Suppliers', icon: '🚚' },
  { id: 'customers', label: 'Customers', icon: '👥' },
  { id: 'purchases', label: 'Purchases', icon: '🛒' },
  { id: 'sales', label: 'Sales', icon: '💰' },
  { id: 'manufacturing', label: 'Manufacturing', icon: '⚙️' },
  { id: 'expenses', label: 'Expenses', icon: '💸' },
  { id: 'payments', label: 'Payments', icon: '💳' },
  { id: 'users', label: 'Users & Roles', icon: '👤' },
];

export default function Sidebar({ activeTab, setActiveTab }) {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        🏭 Manufacturing ERP
      </div>
      <nav className="sidebar-nav">
        {navItems.map((item) => (
          <button
            key={item.id}
            className={`nav-item ${activeTab === item.id ? 'active' : ''}`}
            onClick={() => setActiveTab(item.id)}
          >
            <span>{item.icon}</span>
            <span>{item.label}</span>
          </button>
        ))}
      </nav>
    </aside>
  );
}
