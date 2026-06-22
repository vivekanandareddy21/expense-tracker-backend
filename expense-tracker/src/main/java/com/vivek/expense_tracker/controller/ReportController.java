package com.vivek.expense_tracker.controller;

import com.vivek.expense_tracker.dto.CategoryReportResponse;
import com.vivek.expense_tracker.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public List<CategoryReportResponse> getReport() {

        return reportService.getCategoryReport();
    }
}