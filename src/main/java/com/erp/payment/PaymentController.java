package com.erp.payment;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // Customer payments
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerPayment>> getAllCustomerPayments() {
        return ResponseEntity.ok(service.getAllCustomerPayments());
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<List<CustomerPayment>> getCustomerPayments(@PathVariable Long customerId) {
        return ResponseEntity.ok(service.getCustomerPayments(customerId));
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerPayment> recordCustomerPayment(@Valid @RequestBody CustomerPaymentRequest request) {
        CustomerPayment payment = service.recordCustomerPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    // Supplier payments
    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierPayment>> getAllSupplierPayments() {
        return ResponseEntity.ok(service.getAllSupplierPayments());
    }

    @GetMapping("/suppliers/{supplierId}")
    public ResponseEntity<List<SupplierPayment>> getSupplierPayments(@PathVariable Long supplierId) {
        return ResponseEntity.ok(service.getSupplierPayments(supplierId));
    }

    @PostMapping("/suppliers")
    public ResponseEntity<SupplierPayment> recordSupplierPayment(@Valid @RequestBody SupplierPaymentRequest request) {
        SupplierPayment payment = service.recordSupplierPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }
}
