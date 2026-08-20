package com.erp.rawmaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<RawMaterial> findByActiveTrue();

    @Query("SELECT r FROM RawMaterial r WHERE r.currentStock < r.minimumStockLevel AND r.active = true")
    List<RawMaterial> findLowStockRawMaterials();
}
