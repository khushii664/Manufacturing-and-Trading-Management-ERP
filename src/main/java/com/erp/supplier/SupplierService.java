package com.erp.supplier;

import com.erp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findByActiveTrue();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }

    public List<Supplier> searchSuppliers(String query) {
        return supplierRepository.searchSuppliers(query);
    }

    public BigDecimal getTotalPayables() {
        return supplierRepository.getTotalPayables();
    }

    @Transactional
    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = new Supplier();
        supplier.setName(request.getName());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        supplier.setGstNumber(request.getGstNumber());
        supplier.setOpeningBalance(request.getOpeningBalance());
        supplier.setCurrentPayableBalance(request.getOpeningBalance()); // Default to opening balance
        supplier.setActive(request.isActive());

        return supplierRepository.save(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, SupplierRequest request) {
        Supplier existing = getSupplierById(id);

        existing.setName(request.getName());
        existing.setPhone(request.getPhone());
        existing.setEmail(request.getEmail());
        existing.setAddress(request.getAddress());
        existing.setGstNumber(request.getGstNumber());
        existing.setActive(request.isActive());

        return supplierRepository.save(existing);
    }

    @Transactional
    public void updatePayableBalance(Long supplierId, BigDecimal amountChange) {
        Supplier supplier = getSupplierById(supplierId);
        supplier.setCurrentPayableBalance(supplier.getCurrentPayableBalance().add(amountChange));
        supplierRepository.save(supplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplierRepository.delete(supplier);
    }
}
