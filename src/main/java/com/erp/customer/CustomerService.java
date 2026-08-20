package com.erp.customer;

import com.erp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getActiveCustomers() {
        return customerRepository.findByActiveTrue();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    public List<Customer> searchCustomers(String query) {
        return customerRepository.searchCustomers(query);
    }

    public BigDecimal getTotalReceivables() {
        return customerRepository.getTotalReceivables();
    }

    @Transactional
    public Customer createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setGstNumber(request.getGstNumber());
        customer.setOpeningBalance(request.getOpeningBalance());
        customer.setCurrentReceivableBalance(request.getOpeningBalance());
        customer.setActive(request.isActive());

        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerRequest request) {
        Customer existing = getCustomerById(id);

        existing.setName(request.getName());
        existing.setPhone(request.getPhone());
        existing.setEmail(request.getEmail());
        existing.setAddress(request.getAddress());
        existing.setGstNumber(request.getGstNumber());
        existing.setActive(request.isActive());

        return customerRepository.save(existing);
    }

    @Transactional
    public void updateReceivableBalance(Long customerId, BigDecimal amountChange) {
        Customer customer = getCustomerById(customerId);
        customer.setCurrentReceivableBalance(customer.getCurrentReceivableBalance().add(amountChange));
        customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }
}
