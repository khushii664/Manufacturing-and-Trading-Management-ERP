package com.erp.product;

import com.erp.category.Category;
import com.erp.category.CategoryRepository;
import com.erp.exception.DuplicateResourceException;
import com.erp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        // JOIN FETCH avoids N+1 queries when serializing category inside each product
        return productRepository.findAllWithCategory();
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getProductsByType(ProductType productType) {
        return productRepository.findByProductType(productType);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Transactional
    public Product createProduct(ProductRequest request) {
        // 1. Validate SKU uniqueness
        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("Product", "SKU", request.getSku());
        }

        // 2. Validate category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        // 3. Map request → entity
        Product product = new Product();
        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setCategory(category);
        product.setUnit(request.getUnit());
        product.setSellingPrice(request.getSellingPrice());
        product.setCostPrice(request.getCostPrice());
        product.setMinimumStockLevel(request.getMinimumStockLevel());
        product.setProductType(request.getProductType());
        product.setActive(request.isActive());
        // currentStock is intentionally left at default ZERO

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductRequest request) {
        Product existingProduct = getProductById(id);

        // Block SKU change to one already taken by another product
        if (productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new DuplicateResourceException("Product", "SKU", request.getSku());
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        existingProduct.setName(request.getName());
        existingProduct.setSku(request.getSku());
        existingProduct.setCategory(category);
        existingProduct.setUnit(request.getUnit());
        existingProduct.setSellingPrice(request.getSellingPrice());
        existingProduct.setCostPrice(request.getCostPrice());
        existingProduct.setMinimumStockLevel(request.getMinimumStockLevel());
        existingProduct.setProductType(request.getProductType());
        existingProduct.setActive(request.isActive());
        // currentStock is NOT updated here — transactions handle stock

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
