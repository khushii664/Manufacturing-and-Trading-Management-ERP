package com.erp.expense;

import com.erp.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseCategoryRepository categoryRepository;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseCategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", id));
    }

    public List<Expense> getExpensesByCategory(Long categoryId) {
        return expenseRepository.findByCategoryId(categoryId);
    }

    public BigDecimal getTotalExpensesAmount() {
        return expenseRepository.getTotalExpensesAmount();
    }

    @Transactional
    public Expense createExpense(ExpenseRequest request) {
        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseCategory", request.getCategoryId()));

        Expense expense = new Expense();
        expense.setCategory(category);
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setDescription(request.getDescription());
        expense.setReference(request.getReference());

        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense updateExpense(Long id, ExpenseRequest request) {
        Expense existing = getExpenseById(id);
        ExpenseCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpenseCategory", request.getCategoryId()));

        existing.setCategory(category);
        existing.setAmount(request.getAmount());
        existing.setExpenseDate(request.getExpenseDate());
        existing.setPaymentMethod(request.getPaymentMethod());
        existing.setDescription(request.getDescription());
        existing.setReference(request.getReference());

        return expenseRepository.save(existing);
    }

    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = getExpenseById(id);
        expenseRepository.delete(expense);
    }
}
