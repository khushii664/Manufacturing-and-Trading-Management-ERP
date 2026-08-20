package com.erp.rawmaterial;

import com.erp.supplier.Supplier;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "raw_materials")
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Raw material name is required")
    @Size(max = 150, message = "Name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Code is required")
    @Size(max = 50, message = "Code must not exceed 50 characters")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotBlank(message = "Unit is required (e.g., kg, liters, meters)")
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String unit;

    @NotNull(message = "Purchase price is required")
    @PositiveOrZero(message = "Purchase price must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @NotNull(message = "Minimum stock level is required")
    @PositiveOrZero(message = "Minimum stock level must be zero or positive")
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal minimumStockLevel = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supplier_id")
    private Supplier preferredSupplier;

    @Column(nullable = false)
    private boolean active = true;

    public RawMaterial() {
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }

    public BigDecimal getCurrentStock() { return currentStock; }
    public void setCurrentStock(BigDecimal currentStock) { this.currentStock = currentStock; }

    public BigDecimal getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(BigDecimal minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }

    public Supplier getPreferredSupplier() { return preferredSupplier; }
    public void setPreferredSupplier(Supplier preferredSupplier) { this.preferredSupplier = preferredSupplier; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
