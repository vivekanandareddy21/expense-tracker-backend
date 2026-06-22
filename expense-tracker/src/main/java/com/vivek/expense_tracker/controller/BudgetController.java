package com.vivek.expense_tracker.controller;

import com.vivek.expense_tracker.dto.BudgetResponse;
import com.vivek.expense_tracker.dto.CreateBudgetRequest;
import com.vivek.expense_tracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public BudgetResponse createBudget(
            @Valid @RequestBody CreateBudgetRequest request
    ) {

        return budgetService.createBudget(request);
    }
    @GetMapping("/current")
    public BudgetResponse getCurrentBudget() {

        return budgetService.getCurrentBudget();
    }
}