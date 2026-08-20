package com.erp.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findBySupplierId(Long supplierId);

    List<Purchase> findByStatus(PurchaseStatus status);

    @Query("SELECT COALESCE(SUM(p.total), 0) FROM Purchase p")
    BigDecimal getTotalPurchasesAmount();
}
