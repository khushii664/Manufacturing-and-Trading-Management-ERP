package com.erp.rawmaterial;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class RawMaterialRequest {

    @NotBlank(message = "Raw material name is required")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "Unit is required")
    @Size(max = 30)
    private String unit;

    @NotNull(message = "Purchase price is required")
    @PositiveOrZero(message = "Purchase price must be zero or positive")
    private BigDecimal purchasePrice;

    @NotNull(message = "Minimum stock level is required")
    @PositiveOrZero(message = "Minimum stock level must be zero or positive")
    private BigDecimal minimumStockLevel;

    private Long preferredSupplierId;

    private boolean active = true;

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }

    public Long getPreferredSupplierId() { return preferredSupplierId; }
    public void setPreferredSupplierId(Long preferredSupplierId) { this.preferredSupplierId = preferredSupplierId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
