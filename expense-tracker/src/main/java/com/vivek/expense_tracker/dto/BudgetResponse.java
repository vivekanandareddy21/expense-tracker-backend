package com.vivek.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BudgetResponse {

    private Double amount;
    private Integer month;
    private Integer year;
}