package com.erp.manufacturing;

import com.erp.product.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manufacturing_orders")
public class ManufacturingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "finished_product_id", nullable = false)
    private Product finishedProduct;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityProduced;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ManufacturingStatus status = ManufacturingStatus.COMPLETED;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "manufacturingOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ManufacturingMaterial> materialsConsumed = new ArrayList<>();

    public ManufacturingOrder() {
    }

    public void addMaterialConsumed(ManufacturingMaterial material) {
        materialsConsumed.add(material);
        material.setManufacturingOrder(this);
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public Product getFinishedProduct() { return finishedProduct; }
    public void setFinishedProduct(Product finishedProduct) { this.finishedProduct = finishedProduct; }

    public BigDecimal getQuantityProduced() { return quantityProduced; }
    public void setQuantityProduced(BigDecimal quantityProduced) { this.quantityProduced = quantityProduced; }

    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }

    public ManufacturingStatus getStatus() { return status; }
    public void setStatus(ManufacturingStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<ManufacturingMaterial> getMaterialsConsumed() { return materialsConsumed; }
    public void setMaterialsConsumed(List<ManufacturingMaterial> materialsConsumed) { this.materialsConsumed = materialsConsumed; }
}
