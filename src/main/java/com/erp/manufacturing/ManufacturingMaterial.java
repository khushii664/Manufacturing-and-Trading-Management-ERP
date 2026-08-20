package com.erp.manufacturing;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Entity
@Table(name = "manufacturing_materials")
public class ManufacturingMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturing_order_id", nullable = false)
    private ManufacturingOrder manufacturingOrder;

    @NotNull
    @Column(nullable = false)
    private Long rawMaterialId;

    @Column(nullable = false, length = 150)
    private String rawMaterialName;

    @NotNull
    @Positive
    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityConsumed;

    public ManufacturingMaterial() {
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public ManufacturingOrder getManufacturingOrder() { return manufacturingOrder; }
    public void setManufacturingOrder(ManufacturingOrder manufacturingOrder) { this.manufacturingOrder = manufacturingOrder; }

    public Long getRawMaterialId() { return rawMaterialId; }
    public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }

    public String getRawMaterialName() { return rawMaterialName; }
    public void setRawMaterialName(String rawMaterialName) { this.rawMaterialName = rawMaterialName; }

    public BigDecimal getQuantityConsumed() { return quantityConsumed; }
    public void setQuantityConsumed(BigDecimal quantityConsumed) { this.quantityConsumed = quantityConsumed; }
}
