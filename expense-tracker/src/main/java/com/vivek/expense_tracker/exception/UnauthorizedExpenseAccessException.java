package com.vivek.expense_tracker.exception;

public class UnauthorizedExpenseAccessException
        extends RuntimeException {

    public UnauthorizedExpenseAccessException(
            String message
    ) {
        super(message);
    }
}