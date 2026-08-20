package com.erp.sale;

import com.erp.customer.Customer;
import com.erp.customer.CustomerRepository;
import com.erp.customer.CustomerService;
import com.erp.exception.BadRequestException;
import com.erp.exception.ResourceNotFoundException;
import com.erp.product.Product;
import com.erp.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    public SaleService(
            SaleRepository saleRepository,
            CustomerRepository customerRepository,
            CustomerService customerService,
            ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
    }

    public List<Sale> getSalesByCustomer(Long customerId) {
        return saleRepository.findByCustomerId(customerId);
    }

    public BigDecimal getTotalSalesAmount() {
        return saleRepository.getTotalSalesAmount();
    }

    @Transactional
    public Sale createSale(SaleRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));

        Sale sale = new Sale();
        sale.setCustomer(customer);
        sale.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sale.setInvoiceDate(request.getInvoiceDate());

        BigDecimal calculatedSubtotal = BigDecimal.ZERO;
        BigDecimal calculatedTax = BigDecimal.ZERO;
        BigDecimal calculatedDiscount = BigDecimal.ZERO;
        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (SaleItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemReq.getProductId()));

            // 1. Stock Validation
            if (product.getCurrentStock().compareTo(itemReq.getQuantity()) < 0) {
                throw new BadRequestException("Insufficient stock for product '" + product.getName() + "'. Available: " + product.getCurrentStock() + " " + product.getUnit() + ", Requested: " + itemReq.getQuantity());
            }

            // 2. Decrease Stock
            product.setCurrentStock(product.getCurrentStock().subtract(itemReq.getQuantity()));
            productRepository.save(product);

            SaleItem item = new SaleItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());

            BigDecimal itemTax = itemReq.getTax() != null ? itemReq.getTax() : BigDecimal.ZERO;
            BigDecimal itemDiscount = itemReq.getDiscount() != null ? itemReq.getDiscount() : BigDecimal.ZERO;

            item.setTax(itemTax);
            item.setDiscount(itemDiscount);

            BigDecimal lineSubtotal = itemReq.getUnitPrice().multiply(itemReq.getQuantity());
            BigDecimal lineTotal = lineSubtotal.add(itemTax).subtract(itemDiscount);
            item.setTotal(lineTotal);

            calculatedSubtotal = calculatedSubtotal.add(lineSubtotal);
            calculatedTax = calculatedTax.add(itemTax);
            calculatedDiscount = calculatedDiscount.add(itemDiscount);
            calculatedTotal = calculatedTotal.add(lineTotal);

            sale.addItem(item);
        }

        sale.setSubtotal(calculatedSubtotal);
        sale.setTax(calculatedTax);
        sale.setDiscount(calculatedDiscount);
        sale.setTotal(calculatedTotal);

        BigDecimal paid = request.getAmountPaid() != null ? request.getAmountPaid() : BigDecimal.ZERO;
        if (paid.compareTo(calculatedTotal) > 0) {
            throw new BadRequestException("Amount paid (" + paid + ") cannot exceed total invoice amount (" + calculatedTotal + ")");
        }

        sale.setAmountPaid(paid);
        BigDecimal outstanding = calculatedTotal.subtract(paid);
        sale.setOutstandingAmount(outstanding);

        if (outstanding.compareTo(BigDecimal.ZERO) == 0) {
            sale.setStatus(SaleStatus.PAID);
        } else if (paid.compareTo(BigDecimal.ZERO) > 0) {
            sale.setStatus(SaleStatus.PARTIAL);
        } else {
            sale.setStatus(SaleStatus.UNPAID);
        }

        // Atomically update customer receivable balance with unpaid outstanding amount
        if (outstanding.compareTo(BigDecimal.ZERO) > 0) {
            customerService.updateReceivableBalance(customer.getId(), outstanding);
        }

        return saleRepository.save(sale);
    }
}
