package com.erp.manufacturing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ManufacturingOrderRequest {

    @NotNull(message = "Finished product ID is required")
    private Long finishedProductId;

    @NotNull(message = "Quantity produced is required")
    @Positive(message = "Quantity produced must be positive")
    private BigDecimal quantityProduced;

    @NotNull(message = "Order date is required")
    private LocalDate orderDate;

    @Size(max = 255)
    private String notes;

    @NotEmpty(message = "At least one raw material must be consumed")
    @Valid
    private List<ManufacturingMaterialRequest> materialsConsumed;

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getFinishedProductId() { return finishedProductId; }
    public void setFinishedProductId(Long finishedProductId) { this.finishedProductId = finishedProductId; }

    public BigDecimal getQuantityProduced() { return quantityProduced; }
    public void setQuantityProduced(BigDecimal quantityProduced) { this.quantityProduced = quantityProduced; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<ManufacturingMaterialRequest> getMaterialsConsumed() { return materialsConsumed; }
    public void setMaterialsConsumed(List<ManufacturingMaterialRequest> materialsConsumed) { this.materialsConsumed = materialsConsumed; }
}
