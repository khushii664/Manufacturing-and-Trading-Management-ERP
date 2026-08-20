package com.erp.manufacturing;

import com.erp.exception.BadRequestException;
import com.erp.exception.ResourceNotFoundException;
import com.erp.product.Product;
import com.erp.product.ProductRepository;
import com.erp.rawmaterial.RawMaterial;
import com.erp.rawmaterial.RawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ManufacturingService {

    private final ManufacturingOrderRepository manufacturingOrderRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ManufacturingService(
            ManufacturingOrderRepository manufacturingOrderRepository,
            ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository) {
        this.manufacturingOrderRepository = manufacturingOrderRepository;
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public List<ManufacturingOrder> getAllManufacturingOrders() {
        return manufacturingOrderRepository.findAll();
    }

    public ManufacturingOrder getManufacturingOrderById(Long id) {
        return manufacturingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ManufacturingOrder", id));
    }

    @Transactional
    public ManufacturingOrder createManufacturingOrder(ManufacturingOrderRequest request) {
        Product finishedProduct = productRepository.findById(request.getFinishedProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.getFinishedProductId()));

        ManufacturingOrder order = new ManufacturingOrder();
        order.setFinishedProduct(finishedProduct);
        order.setQuantityProduced(request.getQuantityProduced());
        order.setOrderDate(request.getOrderDate());
        order.setNotes(request.getNotes());
        order.setStatus(ManufacturingStatus.COMPLETED);

        // 1. Validate raw materials stock & deduct stock
        for (ManufacturingMaterialRequest matReq : request.getMaterialsConsumed()) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(matReq.getRawMaterialId())
                    .orElseThrow(() -> new ResourceNotFoundException("RawMaterial", matReq.getRawMaterialId()));

            if (rawMaterial.getCurrentStock().compareTo(matReq.getQuantityConsumed()) < 0) {
                throw new BadRequestException("Insufficient stock for raw material '" + rawMaterial.getName() + "'. Available: " + rawMaterial.getCurrentStock() + " " + rawMaterial.getUnit() + ", Required: " + matReq.getQuantityConsumed());
            }

            // Deduct raw material stock
            rawMaterial.setCurrentStock(rawMaterial.getCurrentStock().subtract(matReq.getQuantityConsumed()));
            rawMaterialRepository.save(rawMaterial);

            ManufacturingMaterial matItem = new ManufacturingMaterial();
            matItem.setRawMaterialId(rawMaterial.getId());
            matItem.setRawMaterialName(rawMaterial.getName());
            matItem.setQuantityConsumed(matReq.getQuantityConsumed());

            order.addMaterialConsumed(matItem);
        }

        // 2. Increase finished product stock
        finishedProduct.setCurrentStock(finishedProduct.getCurrentStock().add(request.getQuantityProduced()));
        productRepository.save(finishedProduct);

        return manufacturingOrderRepository.save(order);
    }
}
