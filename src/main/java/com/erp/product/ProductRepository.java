package com.erp.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    List<Product> findByActiveTrue();

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByProductType(ProductType productType);

    @Query("SELECT p FROM Product p WHERE p.currentStock < p.minimumStockLevel AND p.active = true")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContaining(@Param("name") String name);
}
