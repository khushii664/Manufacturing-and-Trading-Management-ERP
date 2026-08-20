package com.erp.dashboard;

import java.math.BigDecimal;

public class DashboardSummary {

    private BigDecimal totalSales;
    private BigDecimal totalPurchases;
    private BigDecimal totalReceivables;
    private BigDecimal totalPayables;
    private BigDecimal totalExpenses;
    private long lowStockProductsCount;
    private long lowStockRawMaterialsCount;
    private long totalProductsCount;
    private long totalCustomersCount;
    private long totalSuppliersCount;

    public DashboardSummary() {
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public BigDecimal getTotalSales() { return totalSales; }
    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }

    public BigDecimal getTotalPurchases() { return totalPurchases; }
    public void setTotalPurchases(BigDecimal totalPurchases) { this.totalPurchases = totalPurchases; }

    public BigDecimal getTotalReceivables() { return totalReceivables; }
    public void setTotalReceivables(BigDecimal totalReceivables) { this.totalReceivables = totalReceivables; }

    public BigDecimal getTotalPayables() { return totalPayables; }
    public void setTotalPayables(BigDecimal totalPayables) { this.totalPayables = totalPayables; }

    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }

    public long getLowStockProductsCount() { return lowStockProductsCount; }
    public void setLowStockProductsCount(long lowStockProductsCount) { this.lowStockProductsCount = lowStockProductsCount; }

    public long getLowStockRawMaterialsCount() { return lowStockRawMaterialsCount; }
    public void setLowStockRawMaterialsCount(long lowStockRawMaterialsCount) { this.lowStockRawMaterialsCount = lowStockRawMaterialsCount; }

    public long getTotalProductsCount() { return totalProductsCount; }
    public void setTotalProductsCount(long totalProductsCount) { this.totalProductsCount = totalProductsCount; }

    public long getTotalCustomersCount() { return totalCustomersCount; }
    public void setTotalCustomersCount(long totalCustomersCount) { this.totalCustomersCount = totalCustomersCount; }

    public long getTotalSuppliersCount() { return totalSuppliersCount; }
    public void setTotalSuppliersCount(long totalSuppliersCount) { this.totalSuppliersCount = totalSuppliersCount; }
}
