package com.erp.customer;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class CustomerRequest {

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name must not exceed 150 characters")
    private String name;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 100)
    private String email;

    @Size(max = 255)
    private String address;

    @Size(max = 30)
    private String gstNumber;

    @NotNull(message = "Opening balance is required")
    private BigDecimal openingBalance = BigDecimal.ZERO;

    private boolean active = true;

    // ─── Getters & Setters ────────────────────────────────────────────────────

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

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
