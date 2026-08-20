import React, { useState } from 'react';
import Sidebar from './components/Sidebar';

import DashboardPage from './pages/DashboardPage';
import CategoriesPage from './pages/CategoriesPage';
import ProductsPage from './pages/ProductsPage';
import RawMaterialsPage from './pages/RawMaterialsPage';
import SuppliersPage from './pages/SuppliersPage';
import CustomersPage from './pages/CustomersPage';
import PurchasesPage from './pages/PurchasesPage';
import SalesPage from './pages/SalesPage';
import ManufacturingPage from './pages/ManufacturingPage';
import ExpensesPage from './pages/ExpensesPage';
import PaymentsPage from './pages/PaymentsPage';
import UsersPage from './pages/UsersPage';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <DashboardPage />;
      case 'categories':
        return <CategoriesPage />;
      case 'products':
        return <ProductsPage />;
      case 'raw-materials':
        return <RawMaterialsPage />;
      case 'suppliers':
        return <SuppliersPage />;
      case 'customers':
        return <CustomersPage />;
      case 'purchases':
        return <PurchasesPage />;
      case 'sales':
        return <SalesPage />;
      case 'manufacturing':
        return <ManufacturingPage />;
      case 'expenses':
        return <ExpensesPage />;
      case 'payments':
        return <PaymentsPage />;
      case 'users':
        return <UsersPage />;
      default:
        return <DashboardPage />;
    }
  };

  const getTitle = () => {
    switch (activeTab) {
      case 'dashboard': return 'Dashboard & Summary Metrics';
      case 'categories': return 'Product Categories Management';
      case 'products': return 'Finished & Trading Products';
      case 'raw-materials': return 'Raw Materials Inventory';
      case 'suppliers': return 'Supplier Registry & Payables';
      case 'customers': return 'Customer Registry & Receivables';
      case 'purchases': return 'Purchases & Inventory Restock';
      case 'sales': return 'Sales Orders & Invoicing';
      case 'manufacturing': return 'Manufacturing Runs & Production';
      case 'expenses': return 'Expense Management & Accounts';
      case 'payments': return 'Customer & Supplier Payment Ledger';
      case 'users': return 'User Access & Role Management';
      default: return 'Manufacturing & Trading ERP';
    }
  };

  return (
    <div className="app-container">
      <Sidebar activeTab={activeTab} setActiveTab={setActiveTab} />
      <div className="main-wrapper">
        <header className="header">
          <div className="header-title">{getTitle()}</div>
        </header>
        <main className="content-body">
          {renderContent()}
        </main>
      </div>
    </div>
  );
}
