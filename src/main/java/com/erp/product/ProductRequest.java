package com.erp.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO used for creating and updating a Product.
 *
 * Why a separate DTO instead of using the entity directly?
 * - The entity has fields we don't want clients to set (e.g., currentStock).
 * - The client sends categoryId (a Long), not the full Category object.
 * - Keeps the API contract stable even if the entity changes internally.
 */
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    private String sku;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotBlank(message = "Unit is required (e.g. pcs, kg, liters)")
    @Size(max = 30)
    private String unit;

    @NotNull(message = "Selling price is required")
    @PositiveOrZero(message = "Selling price must be zero or positive")
    private BigDecimal sellingPrice;

    @NotNull(message = "Cost price is required")
    @PositiveOrZero(message = "Cost price must be zero or positive")
    private BigDecimal costPrice;

    @NotNull(message = "Minimum stock level is required")
    @PositiveOrZero(message = "Minimum stock level must be zero or positive")
    private BigDecimal minimumStockLevel;

    @NotNull(message = "Product type is required (FINISHED or TRADING)")
    private ProductType productType;

    private boolean active = true;

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }

    public ProductType getProductType() { return productType; }
    public void setProductType(ProductType productType) { this.productType = productType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
