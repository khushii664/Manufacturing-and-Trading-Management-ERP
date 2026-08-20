package com.erp.customer;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 255)
    @Column(length = 255)
    private String address;

    @Size(max = 30)
    @Column(length = 30)
    private String gstNumber;

    @NotNull(message = "Opening balance is required")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal openingBalance = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentReceivableBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean active = true;

    public Customer() {
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getGstNumber() { return gstNumber; }
    public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }

    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }

    public BigDecimal getCurrentReceivableBalance() { return currentReceivableBalance; }
    public void setCurrentReceivableBalance(BigDecimal currentReceivableBalance) { this.currentReceivableBalance = currentReceivableBalance; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
