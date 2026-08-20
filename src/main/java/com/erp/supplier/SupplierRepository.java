package com.erp.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findByActiveTrue();

    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(s.phone) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Supplier> searchSuppliers(@Param("query") String query);

    @Query("SELECT COALESCE(SUM(s.currentPayableBalance), 0) FROM Supplier s WHERE s.active = true")
    BigDecimal getTotalPayables();
}
