package com.vivek.expense_tracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBudgetRequest {

    @NotNull
    private Double amount;
}