package com.erp.sale;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByCustomerId(Long customerId);

    List<Sale> findByStatus(SaleStatus status);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s")
    BigDecimal getTotalSalesAmount();
}
