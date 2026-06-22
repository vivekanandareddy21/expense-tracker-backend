package com.vivek.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResponse {

    private Double budget;
    private Double spent;
    private Double remaining;
    private Long expenseCount;
}