package com.vivek.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryReportResponse {

    private String category;
    private Double amount;
}