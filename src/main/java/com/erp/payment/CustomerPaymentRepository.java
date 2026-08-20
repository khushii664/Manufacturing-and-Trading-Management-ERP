package com.erp.payment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Long> {
    List<CustomerPayment> findByCustomerId(Long customerId);
}
