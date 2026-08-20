package com.erp.manufacturing;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class ManufacturingMaterialRequest {

    @NotNull(message = "Raw material ID is required")
    private Long rawMaterialId;

    @NotNull(message = "Quantity consumed is required")
    @Positive(message = "Quantity consumed must be positive")
    private BigDecimal quantityConsumed;

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getRawMaterialId() { return rawMaterialId; }
    public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }

    public BigDecimal getQuantityConsumed() { return quantityConsumed; }
    public void setQuantityConsumed(BigDecimal quantityConsumed) { this.quantityConsumed = quantityConsumed; }
}
