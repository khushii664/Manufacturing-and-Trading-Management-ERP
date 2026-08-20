package com.erp.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    // Fetch with category in a single JOIN query (avoids N+1 lazy-load issue)
    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    // Filter by active status
    List<Product> findByActiveTrue();

    // Find by category
    List<Product> findByCategoryId(Long categoryId);

    // Find by type (FINISHED or TRADING)
    List<Product> findByProductType(ProductType productType);

    // Low stock alert — products where current stock is below minimum
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.currentStock < p.minimumStockLevel AND p.active = true")
    List<Product> findLowStockProducts();

    // Search by name (case-insensitive, partial match)
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContaining(@Param("name") String name);
}
