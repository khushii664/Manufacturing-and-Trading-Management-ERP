package com.erp.product;

import com.erp.category.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 150, message = "Product name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "SKU is required")
    @Size(max = 50, message = "SKU must not exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    /**
     * Many products → one category.
     * LAZY loading — category is only fetched when explicitly accessed.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotBlank(message = "Unit is required (e.g. pcs, kg, liters)")
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String unit;

    @NotNull(message = "Selling price is required")
    @PositiveOrZero(message = "Selling price must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal sellingPrice;

    @NotNull(message = "Cost price is required")
    @PositiveOrZero(message = "Cost price must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costPrice;

    /**
     * Current stock quantity.
     * This value is NOT updated directly via this entity's API.
     * It is updated automatically by purchase, sale, and manufacturing transactions.
     */
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @NotNull(message = "Minimum stock level is required")
    @PositiveOrZero(message = "Minimum stock level must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal minimumStockLevel = BigDecimal.ZERO;

    @NotNull(message = "Product type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductType productType;

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    public Product() {
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }

    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }

    public ProductType getProductType() { return productType; }
    public void setProductType(ProductType productType) { this.productType = productType; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
