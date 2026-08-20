package com.erp.manufacturing;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturing")
public class ManufacturingController {

    private final ManufacturingService manufacturingService;

    public ManufacturingController(ManufacturingService manufacturingService) {
        this.manufacturingService = manufacturingService;
    }

    @GetMapping
    public ResponseEntity<List<ManufacturingOrder>> getAllManufacturingOrders() {
        return ResponseEntity.ok(manufacturingService.getAllManufacturingOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturingOrder> getManufacturingOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturingService.getManufacturingOrderById(id));
    }

    @PostMapping
    public ResponseEntity<ManufacturingOrder> createManufacturingOrder(@Valid @RequestBody ManufacturingOrderRequest request) {
        ManufacturingOrder created = manufacturingService.createManufacturingOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
