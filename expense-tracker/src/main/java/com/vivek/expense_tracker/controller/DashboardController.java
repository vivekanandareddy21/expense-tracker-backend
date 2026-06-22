package com.vivek.expense_tracker.controller;

import com.vivek.expense_tracker.dto.CategorySummaryResponse;
import com.vivek.expense_tracker.dto.DashboardResponse;
import com.vivek.expense_tracker.dto.MonthlyReportResponse;
import com.vivek.expense_tracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ExpenseService expenseService;

    @GetMapping("/summary")
    public DashboardResponse getSummary() {

        return expenseService.getDashboardSummary();
    }
    @GetMapping("/category-summary")
    public List<CategorySummaryResponse>
    getCategorySummary() {

        return expenseService.getCategorySummary();
    }
    @GetMapping("/monthly-report")
    public MonthlyReportResponse getMonthlyReport() {

        return expenseService.getMonthlyReport();
    }
}