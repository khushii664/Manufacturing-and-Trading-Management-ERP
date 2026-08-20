package com.erp.sale;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @GetMapping("/by-customer/{customerId}")
    public ResponseEntity<List<Sale>> getSalesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(saleService.getSalesByCustomer(customerId));
    }

    @GetMapping("/total-amount")
    public ResponseEntity<BigDecimal> getTotalSalesAmount() {
        return ResponseEntity.ok(saleService.getTotalSalesAmount());
    }

    @PostMapping
    public ResponseEntity<Sale> createSale(@Valid @RequestBody SaleRequest request) {
        Sale created = saleService.createSale(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
