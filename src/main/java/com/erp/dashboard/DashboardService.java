package com.erp.dashboard;

import com.erp.customer.CustomerRepository;
import com.erp.customer.CustomerService;
import com.erp.expense.ExpenseService;
import com.erp.product.ProductRepository;
import com.erp.purchase.PurchaseService;
import com.erp.rawmaterial.RawMaterialRepository;
import com.erp.sale.SaleService;
import com.erp.supplier.SupplierRepository;
import com.erp.supplier.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final SaleService saleService;
    private final PurchaseService purchaseService;
    private final CustomerService customerService;
    private final SupplierService supplierService;
    private final ExpenseService expenseService;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    public DashboardService(
            SaleService saleService,
            PurchaseService purchaseService,
            CustomerService customerService,
            SupplierService supplierService,
            ExpenseService expenseService,
            ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository,
            CustomerRepository customerRepository,
            SupplierRepository supplierRepository) {
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.customerService = customerService;
        this.supplierService = supplierService;
        this.expenseService = expenseService;
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
        this.customerRepository = customerRepository;
        this.supplierRepository = supplierRepository;
    }

    public DashboardSummary getSummary() {
        DashboardSummary summary = new DashboardSummary();
        summary.setTotalSales(saleService.getTotalSalesAmount());
        summary.setTotalPurchases(purchaseService.getTotalPurchasesAmount());
        summary.setTotalReceivables(customerService.getTotalReceivables());
        summary.setTotalPayables(supplierService.getTotalPayables());
        summary.setTotalExpenses(expenseService.getTotalExpensesAmount());

        summary.setLowStockProductsCount(productRepository.findLowStockProducts().size());
        summary.setLowStockRawMaterialsCount(rawMaterialRepository.findLowStockRawMaterials().size());

        summary.setTotalProductsCount(productRepository.count());
        summary.setTotalCustomersCount(customerRepository.count());
        summary.setTotalSuppliersCount(supplierRepository.count());

        return summary;
    }
}
