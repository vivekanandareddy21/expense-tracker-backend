package com.vivek.expense_tracker.service;

import com.vivek.expense_tracker.dto.BudgetResponse;
import com.vivek.expense_tracker.dto.CreateBudgetRequest;
import com.vivek.expense_tracker.entity.Budget;
import com.vivek.expense_tracker.entity.User;
import com.vivek.expense_tracker.exception.UserNotFoundException;
import com.vivek.expense_tracker.repository.BudgetRepository;
import com.vivek.expense_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    public BudgetResponse createBudget(
            CreateBudgetRequest request
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        LocalDate now = LocalDate.now();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(
                        user,
                        now.getMonthValue(),
                        now.getYear()
                )
                .orElse(
                        Budget.builder()
                                .user(user)
                                .month(now.getMonthValue())
                                .year(now.getYear())
                                .build()
                );

        budget.setAmount(request.getAmount());

        Budget savedBudget =
                budgetRepository.save(budget);

        return new BudgetResponse(
                savedBudget.getAmount(),
                savedBudget.getMonth(),
                savedBudget.getYear()
        );
    }
    public BudgetResponse getCurrentBudget() {

        System.out.println("===== GET CURRENT BUDGET =====");

        System.out.println(
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
        );

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        System.out.println("EMAIL = " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"
                        )
                );

        System.out.println("USER FOUND = " + user.getEmail());

        LocalDate now = LocalDate.now();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(
                        user,
                        now.getMonthValue(),
                        now.getYear()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Budget not found"
                        )
                );

        System.out.println("BUDGET FOUND = " + budget.getAmount());

        return new BudgetResponse(
                budget.getAmount(),
                budget.getMonth(),
                budget.getYear()
        );
    }
}