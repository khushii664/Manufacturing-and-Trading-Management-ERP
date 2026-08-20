# Manufacturing & Trading Management ERP

A full-stack **Manufacturing & Trading Management ERP** built with **Spring Boot, React, and MySQL** to manage products, raw materials, suppliers, customers, purchases, sales, manufacturing, expenses, payments, and business transactions.

The system follows a layered backend architecture and provides a React-based frontend for interacting with the ERP modules through REST APIs.

## Features

### Dashboard

* Business overview and summary information
* Centralized view of key ERP metrics

### Product & Category Management

* Create, update, view, and manage products
* Product categorization
* Support for different product types

### Raw Material Management

* Manage raw materials
* Track raw-material information used in manufacturing

### Supplier Management

* Maintain supplier records
* Manage supplier-related transactions
* Track supplier payments

### Customer Management

* Maintain customer records
* Manage customer-related transactions
* Track customer payments and outstanding amounts

### Purchase Management

* Create purchase transactions
* Manage purchase items
* Update inventory through purchase workflows
* Associate purchases with suppliers

### Sales Management

* Create sales transactions
* Manage sale items
* Update inventory through sales workflows
* Associate sales with customers

### Manufacturing

* Create manufacturing orders
* Define raw materials required for production
* Track manufacturing status
* Consume raw materials during manufacturing
* Produce finished products

### Expense Management

* Create and manage expense categories
* Record business expenses
* Track expense information

### Payment Management

* Record customer payments
* Record supplier payments
* Support multiple payment methods

### Exception Handling

* Centralized global exception handling
* Structured API error responses
* Validation and resource-not-found handling

---

## Technology Stack

### Backend

* **Java 25**
* **Spring Boot 4.1.0**
* Spring Web MVC
* Spring Data JPA
* Hibernate
* Spring Validation
* Maven

### Frontend

* **React 18**
* **Vite**
* JavaScript
* CSS
* Lucide React

### Database

* **MySQL**

### Development Tools

* Git
* GitHub
* IntelliJ IDEA / VS Code
* Postman (for API testing)

---

## Architecture

The application follows a layered architecture:

```text
┌─────────────────────────────┐
│       React Frontend        │
│        React + Vite         │
└──────────────┬──────────────┘
               │
               │ REST API
               ▼
┌─────────────────────────────┐
│      Spring Controllers     │
│       HTTP / REST Layer     │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│        Service Layer        │
│     Business Logic          │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│       Repository Layer      │
│    Spring Data JPA          │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│          MySQL              │
│       Relational DB         │
└─────────────────────────────┘
```

### Backend Structure

The backend is organized by business domain:

```text
src/main/java/com/erp/
│
├── category/
├── customer/
├── dashboard/
├── expense/
├── manufacturing/
├── payment/
├── product/
├── purchase/
├── rawmaterial/
├── sale/
├── supplier/
├── user/
├── common/
├── config/
└── exception/
```

Each major module generally follows the pattern:

```text
Entity
   ↓
Repository
   ↓
Service
   ↓
Controller
```

Request DTOs are used where appropriate to separate incoming API data from persistence entities.

---

## Key Business Workflows

### Purchase Workflow

```text
Supplier
   ↓
Purchase
   ↓
Purchase Items
   ↓
Inventory Updated
   ↓
Supplier Outstanding Updated
```

### Sales Workflow

```text
Customer
   ↓
Sale
   ↓
Sale Items
   ↓
Inventory Updated
   ↓
Customer Outstanding Updated
```

### Manufacturing Workflow

```text
Manufacturing Order
        ↓
Raw Materials Required
        ↓
Raw Material Inventory Consumed
        ↓
Production Completed
        ↓
Finished Product Inventory Increased
```

### Payment Workflow

```text
Customer / Supplier
        ↓
Payment
        ↓
Outstanding Balance Updated
```

These workflows allow individual ERP transactions to update related business information rather than treating each module as an isolated CRUD application.

---

## Project Structure

```text
erp-backend/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/erp/
│       │       ├── category/
│       │       ├── customer/
│       │       ├── dashboard/
│       │       ├── expense/
│       │       ├── manufacturing/
│       │       ├── payment/
│       │       ├── product/
│       │       ├── purchase/
│       │       ├── rawmaterial/
│       │       ├── sale/
│       │       ├── supplier/
│       │       ├── user/
│       │       ├── common/
│       │       ├── config/
│       │       └── exception/
│       │
│       └── resources/
│
├── erp-frontend/
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   └── pages/
│   │       ├── CategoriesPage.jsx
│   │       ├── CustomersPage.jsx
│   │       ├── DashboardPage.jsx
│   │       ├── ExpensesPage.jsx
│   │       ├── ManufacturingPage.jsx
│   │       ├── PaymentsPage.jsx
│   │       ├── ProductsPage.jsx
│   │       ├── PurchasesPage.jsx
│   │       ├── RawMaterialsPage.jsx
│   │       ├── SalesPage.jsx
│   │       ├── SuppliersPage.jsx
│   │       └── UsersPage.jsx
│   ├── package.json
│   ├── vite.config.js
│   └── index.html
│
├── pom.xml
├── .gitignore
└── README.md
```

---

## Getting Started

### Prerequisites

Make sure the following are installed:

* Java 25
* Maven
* Node.js and npm
* MySQL
* Git

---

## 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd erp-backend
```

---

## 2. Configure MySQL

Create a MySQL database for the application.

For example:

```sql
CREATE DATABASE erp_db;
```

Configure the database connection in the Spring Boot application properties.

Do **not** commit your local database password or other credentials to GitHub.

---

## 3. Run the Backend

From the project root:

```bash
mvn spring-boot:run
```

The Spring Boot backend will start on its configured port.

---

## 4. Run the Frontend

Open another terminal:

```bash
cd erp-frontend
npm install
npm run dev
```

Vite will provide a local development URL, typically:

```text
http://localhost:5173
```

Open that URL in your browser.

---

## 5. Build the Frontend

To create a production build:

```bash
cd erp-frontend
npm run build
```

The generated `dist/` directory is intentionally excluded from Git.

---

## API Communication

The React frontend communicates with the Spring Boot backend through REST APIs.

Examples of API resources include:

```text
/api/categories
/api/products
/api/raw-materials
/api/suppliers
/api/customers
/api/purchases
/api/sales
/api/manufacturing
/api/expenses
/api/expense-categories
/api/payments/customers
/api/payments/suppliers
/api/users
/api/dashboard/summary
```

The Vite development server is configured to proxy API requests to the Spring Boot backend during local development.

---

## Error Handling

The backend includes centralized exception handling through a global exception handler.

The application handles common API errors such as:

* Resource not found
* Duplicate resources
* Invalid requests
* Validation errors
* Other application-level exceptions

This keeps API error responses consistent across different modules.

---

## Database & Persistence

The application uses:

* Spring Data JPA
* Hibernate
* MySQL

Entities are mapped to relational database tables using JPA annotations.

Repositories use Spring Data JPA to provide database access while the service layer contains the application's business logic.

---

## Git & Version Control

The project uses Git for version control.

Generated and environment-specific files such as:

```text
node_modules/
dist/
target/
.env
*.log
```

are excluded through `.gitignore`.

---

## Future Improvements

Possible future improvements include:

* Authentication and role-based authorization
* More comprehensive reporting and analytics
* Advanced inventory tracking
* Invoice generation and printing
* Automated testing
* API documentation with Swagger / OpenAPI
* Deployment using Docker
* Cloud deployment
* Audit logging
* Improved dashboard analytics
* Additional financial/accounting features

---

## Project Status

**Current Status: Core full-stack ERP application implemented and functional.**

The project currently contains the major ERP modules and their associated frontend interfaces, REST APIs, database persistence, business transaction workflows, and centralized exception handling.

The application is being further refined and tested as part of ongoing development.

---

## Author

**Khushii**
Computer Science & Engineering


---
