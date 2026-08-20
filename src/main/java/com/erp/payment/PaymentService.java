package com.erp.payment;

import com.erp.customer.Customer;
import com.erp.customer.CustomerRepository;
import com.erp.customer.CustomerService;
import com.erp.exception.ResourceNotFoundException;
import com.erp.supplier.Supplier;
import com.erp.supplier.SupplierRepository;
import com.erp.supplier.SupplierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final SupplierPaymentRepository supplierPaymentRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;

    public PaymentService(
            CustomerPaymentRepository customerPaymentRepository,
            SupplierPaymentRepository supplierPaymentRepository,
            CustomerRepository customerRepository,
            CustomerService customerService,
            SupplierRepository supplierRepository,
            SupplierService supplierService) {
        this.customerPaymentRepository = customerPaymentRepository;
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.supplierRepository = supplierRepository;
        this.supplierService = supplierService;
    }

    public List<CustomerPayment> getAllCustomerPayments() {
        return customerPaymentRepository.findAll();
    }

    public List<CustomerPayment> getCustomerPayments(Long customerId) {
        return customerPaymentRepository.findByCustomerId(customerId);
    }

    @Transactional
    public CustomerPayment recordCustomerPayment(CustomerPaymentRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer", request.getCustomerId()));

        CustomerPayment payment = new CustomerPayment();
        payment.setCustomer(customer);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReference(request.getReference());
        payment.setNotes(request.getNotes());

        // Outstanding receivable decreases on payment
        customerService.updateReceivableBalance(customer.getId(), request.getAmount().negate());

        return customerPaymentRepository.save(payment);
    }

    public List<SupplierPayment> getAllSupplierPayments() {
        return supplierPaymentRepository.findAll();
    }

    public List<SupplierPayment> getSupplierPayments(Long supplierId) {
        return supplierPaymentRepository.findBySupplierId(supplierId);
    }

    @Transactional
    public SupplierPayment recordSupplierPayment(SupplierPaymentRequest request) {
        Supplier supplier = supplierRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));

        SupplierPayment payment = new SupplierPayment();
        payment.setSupplier(supplier);
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReference(request.getReference());
        payment.setNotes(request.getNotes());

        // Outstanding payable decreases on payment
        supplierService.updatePayableBalance(supplier.getId(), request.getAmount().negate());

        return supplierPaymentRepository.save(payment);
    }
}
