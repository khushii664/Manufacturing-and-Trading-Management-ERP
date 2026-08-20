package com.erp.purchase;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @GetMapping("/by-supplier/{supplierId}")
    public ResponseEntity<List<Purchase>> getPurchasesBySupplier(@PathVariable Long supplierId) {
        return ResponseEntity.ok(purchaseService.getPurchasesBySupplier(supplierId));
    }

    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalPurchasesAmount() {
        return ResponseEntity.ok(purchaseService.getTotalPurchasesAmount());
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@Valid @RequestBody PurchaseRequest request) {
        Purchase created = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
