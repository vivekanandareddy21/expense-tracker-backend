package com.vivek.expense_tracker.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpenseResponse {

    private Long id;
    private String title;
    private Double amount;
    private String category;
    private String description;
}