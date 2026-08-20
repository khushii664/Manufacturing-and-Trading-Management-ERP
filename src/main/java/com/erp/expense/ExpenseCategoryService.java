package com.erp.expense;

import com.erp.exception.DuplicateResourceException;
import com.erp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExpenseCategoryService {

    private final ExpenseCategoryRepository repository;

    public ExpenseCategoryService(ExpenseCategoryRepository repository) {
        this.repository = repository;
    }

    public List<ExpenseCategory> getAllCategories() {
        return repository.findAll();
    }

    public ExpenseCategory getCategoryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseCategory", id));
    }

    @Transactional
    public ExpenseCategory createCategory(ExpenseCategory category) {
        if (repository.existsByName(category.getName())) {
            throw new DuplicateResourceException("ExpenseCategory", "name", category.getName());
        }
        return repository.save(category);
    }

    @Transactional
    public ExpenseCategory updateCategory(Long id, ExpenseCategory category) {
        ExpenseCategory existing = getCategoryById(id);
        if (repository.existsByNameAndIdNot(category.getName(), id)) {
            throw new DuplicateResourceException("ExpenseCategory", "name", category.getName());
        }
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return repository.save(existing);
    }

    @Transactional
    public void deleteCategory(Long id) {
        ExpenseCategory category = getCategoryById(id);
        repository.delete(category);
    }
}
