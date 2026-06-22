package com.vivek.expense_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExpenseRequest {

    @NotBlank
    private String title;

    @NotNull
    private Double amount;

    @NotBlank
    private String category;

    private String description;
}