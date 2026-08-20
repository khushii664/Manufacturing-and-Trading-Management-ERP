package com.erp.purchase;

import com.erp.exception.BadRequestException;
import com.erp.exception.ResourceNotFoundException;
import com.erp.product.Product;
import com.erp.product.ProductRepository;
import com.erp.rawmaterial.RawMaterial;
import com.erp.rawmaterial.RawMaterialRepository;
import com.erp.supplier.Supplier;
import com.erp.supplier.SupplierRepository;
import com.erp.supplier.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public PurchaseService(
            PurchaseRepository purchaseRepository,
            SupplierRepository supplierRepository,
            SupplierService supplierService,
            ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository) {
        this.purchaseRepository = purchaseRepository;
        this.supplierRepository = supplierRepository;
        this.supplierService = supplierService;
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase", id));
    }

    public List<Purchase> getPurchasesBySupplier(Long supplierId) {
        return purchaseRepository.findBySupplierId(supplierId);
    }

    public BigDecimal getTotalPurchasesAmount() {
        return purchaseRepository.getTotalPurchasesAmount();
    }

    @Transactional
    public Purchase createPurchase(PurchaseRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));

        Purchase purchase = new Purchase();
        purchase.setSupplier(supplier);
        purchase.setInvoiceNumber("PUR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        purchase.setInvoiceDate(request.getInvoiceDate());

        BigDecimal calculatedSubtotal = BigDecimal.ZERO;
        BigDecimal calculatedTax = BigDecimal.ZERO;
        BigDecimal calculatedDiscount = BigDecimal.ZERO;
        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (PurchaseItemRequest itemReq : request.getItems()) {
            PurchaseItem item = new PurchaseItem();
            item.setItemType(itemReq.getItemType().toUpperCase());
            item.setItemId(itemReq.getItemId());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());

            BigDecimal itemTax = itemReq.getTax() != null ? itemReq.getTax() : BigDecimal.ZERO;
            BigDecimal itemDiscount = itemReq.getDiscount() != null ? itemReq.getDiscount() : BigDecimal.ZERO;

            item.setTax(itemTax);
            item.setDiscount(itemDiscount);

            BigDecimal lineSubtotal = itemReq.getUnitPrice().multiply(itemReq.getQuantity());
            BigDecimal lineTotal = lineSubtotal.add(itemTax).subtract(itemDiscount);
            item.setTotal(lineTotal);

            // Update inventory based on item type
            if ("PRODUCT".equalsIgnoreCase(itemReq.getItemType())) {
                Product product = productRepository.findById(itemReq.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getItemId()));

                item.setItemName(product.getName());
                // Increase finished/trading product stock
                product.setCurrentStock(product.getCurrentStock().add(itemReq.getQuantity()));
                product.setCostPrice(itemReq.getUnitPrice()); // Update cost price to latest purchase price
                productRepository.save(product);

            } else if ("RAW_MATERIAL".equalsIgnoreCase(itemReq.getItemType())) {
                RawMaterial rawMaterial = rawMaterialRepository.findById(itemReq.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("RawMaterial", itemReq.getItemId()));

                item.setItemName(rawMaterial.getName());
                // Increase raw material stock
                rawMaterial.setCurrentStock(rawMaterial.getCurrentStock().add(itemReq.getQuantity()));
                rawMaterial.setPurchasePrice(itemReq.getUnitPrice());
                rawMaterialRepository.save(rawMaterial);

            } else {
                throw new BadRequestException("Invalid itemType: " + itemReq.getItemType() + ". Must be PRODUCT or RAW_MATERIAL");
            }

            calculatedSubtotal = calculatedSubtotal.add(lineSubtotal);
            calculatedTax = calculatedTax.add(itemTax);
            calculatedDiscount = calculatedDiscount.add(itemDiscount);
            calculatedTotal = calculatedTotal.add(lineTotal);

            purchase.addItem(item);
        }

        purchase.setSubtotal(calculatedSubtotal);
        purchase.setTax(calculatedTax);
        purchase.setDiscount(calculatedDiscount);
        purchase.setTotal(calculatedTotal);

        BigDecimal paid = request.getAmountPaid() != null ? request.getAmountPaid() : BigDecimal.ZERO;
        if (paid.compareTo(calculatedTotal) > 0) {
            throw new BadRequestException("Amount paid (" + paid + ") cannot exceed total purchase amount (" + calculatedTotal + ")");
        }

        purchase.setAmountPaid(paid);
        BigDecimal outstanding = calculatedTotal.subtract(paid);
        purchase.setOutstandingAmount(outstanding);

        if (outstanding.compareTo(BigDecimal.ZERO) == 0) {
            purchase.setStatus(PurchaseStatus.PAID);
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            purchase.setStatus(PurchaseStatus.PARTIAL);
        } else {
            purchase.setStatus(PurchaseStatus.UNPAID);
        }

        // Atomically update supplier payable balance with the unpaid outstanding amount
        if (outstanding.compareTo(BigDecimal.ZERO) > 0) {
            supplierService.updatePayableBalance(supplier.getId(), outstanding);
        }

        return purchaseRepository.save(purchase);
    }
}
