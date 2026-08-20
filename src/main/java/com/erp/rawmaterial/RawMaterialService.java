package com.erp.rawmaterial;

import com.erp.exception.DuplicateResourceException;
import com.erp.exception.ResourceNotFoundException;
import com.erp.supplier.Supplier;
import com.erp.supplier.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;
    private final SupplierRepository supplierRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository, SupplierRepository supplierRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialRepository.findAll();
    }

    public List<RawMaterial> getActiveRawMaterials() {
        return rawMaterialRepository.findByActiveTrue();
    }

    public List<RawMaterial> getLowStockRawMaterials() {
        return rawMaterialRepository.findLowStockRawMaterials();
    }

    public RawMaterial getRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RawMaterial", id));
    }

    @Transactional
    public RawMaterial createRawMaterial(RawMaterialRequest request) {
        if (rawMaterialRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("RawMaterial", "code", request.getCode());
        }

        Supplier supplier = null;
        if (request.getPreferredSupplierId() != null) {
            supplier = supplierRepository.findById(request.getPreferredSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getPreferredSupplierId()));
        }

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName(request.getName());
        rawMaterial.setCode(request.getCode());
        rawMaterial.setUnit(request.getUnit());
        rawMaterial.setPurchasePrice(request.getPurchasePrice());
        rawMaterial.setMinimumStockLevel(request.getMinimumStockLevel());
        rawMaterial.setPreferredSupplier(supplier);
        rawMaterial.setActive(request.isActive());

        return rawMaterialRepository.save(rawMaterial);
    }

    @Transactional
    public RawMaterial updateRawMaterial(Long id, RawMaterialRequest request) {
        RawMaterial existing = getRawMaterialById(id);

        if (rawMaterialRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new DuplicateResourceException("RawMaterial", "code", request.getCode());
        }

        Supplier supplier = null;
        if (request.getPreferredSupplierId() != null) {
            supplier = supplierRepository.findById(request.getPreferredSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getPreferredSupplierId()));
        }

        existing.setName(request.getName());
        existing.setCode(request.getCode());
        existing.setUnit(request.getUnit());
        existing.setPurchasePrice(request.getPurchasePrice());
        existing.setMinimumStockLevel(request.getMinimumStockLevel());
        existing.setPreferredSupplier(supplier);
        existing.setActive(request.isActive());

        return rawMaterialRepository.save(existing);
    }

    @Transactional
    public void deleteRawMaterial(Long id) {
        RawMaterial rawMaterial = getRawMaterialById(id);
        rawMaterialRepository.delete(rawMaterial);
    }
}
