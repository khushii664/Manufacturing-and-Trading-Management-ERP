package com.erp.sale;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SaleRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Amount paid is required")
    @PositiveOrZero(message = "Amount paid must be zero or positive")
    private BigDecimal amountPaid = BigDecimal.ZERO;

    @NotEmpty(message = "Sale must contain at least one line item")
    @Valid
    private List<SaleItemRequest> items;

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    public List<SaleItemRequest> getItems() { return items; }
    public void setItems(List<SaleItemRequest> items) { this.items = items; }
}
