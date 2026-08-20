package com.erp.rawmaterial;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @GetMapping
    public ResponseEntity<List<RawMaterial>> getAllRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.getAllRawMaterials());
    }

    @GetMapping("/active")
    public ResponseEntity<List<RawMaterial>> getActiveRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.getActiveRawMaterials());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<RawMaterial>> getLowStockRawMaterials() {
        return ResponseEntity.ok(rawMaterialService.getLowStockRawMaterials());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> getRawMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.getRawMaterialById(id));
    }

    @PostMapping
    public ResponseEntity<RawMaterial> createRawMaterial(@Valid @RequestBody RawMaterialRequest request) {
        RawMaterial created = rawMaterialService.createRawMaterial(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> updateRawMaterial(
            @PathVariable Long id,
            @Valid @RequestBody RawMaterialRequest request) {

        return ResponseEntity.ok(rawMaterialService.updateRawMaterial(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
