package com.vivek.expense_tracker.controller;

import com.vivek.expense_tracker.dto.CreateExpenseRequest;
import com.vivek.expense_tracker.dto.ExpenseResponse;
import com.vivek.expense_tracker.dto.UpdateExpenseRequest;
import com.vivek.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ExpenseResponse createExpense(
            @Valid @RequestBody CreateExpenseRequest request
    ) {

        return expenseService.createExpense(request);
    }
    @GetMapping
    public List<ExpenseResponse> getMyExpenses()
    {

        return expenseService.getMyExpenses();
    }
    @GetMapping("/{id}")
    public ExpenseResponse getExpenseById(
            @PathVariable Long id
    ) {

        return expenseService.getExpenseById(id);
    }
    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody UpdateExpenseRequest request
    ) {

        return expenseService.updateExpense(
                id,
                request
        );
    }
    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable Long id
    ) {

        expenseService.deleteExpense(id);

        return "Expense deleted successfully";
    }
}