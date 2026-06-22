package com.vivek.expense_tracker.service;

import com.vivek.expense_tracker.dto.*;
import com.vivek.expense_tracker.entity.Budget;
import com.vivek.expense_tracker.entity.Expense;
import com.vivek.expense_tracker.entity.User;
import com.vivek.expense_tracker.exception.ExpenseNotFoundException;
import com.vivek.expense_tracker.exception.UnauthorizedExpenseAccessException;
import com.vivek.expense_tracker.exception.UserNotFoundException;
import com.vivek.expense_tracker.repository.BudgetRepository;
import com.vivek.expense_tracker.repository.ExpenseRepository;
import com.vivek.expense_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    public ExpenseResponse createExpense(
            CreateExpenseRequest request
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .expenseDate(LocalDate.now())
                .user(user)
                .build();

        Expense savedExpense =
                expenseRepository.save(expense);

        return ExpenseResponse.builder()
                .id(savedExpense.getId())
                .title(savedExpense.getTitle())
                .amount(savedExpense.getAmount())
                .category(savedExpense.getCategory())
                .description(savedExpense.getDescription())
                .build();
    }
    public List<ExpenseResponse> getMyExpenses() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        return expenseRepository.findByUser(user)
                .stream()
                .map(expense -> ExpenseResponse.builder()
                        .id(expense.getId())
                        .title(expense.getTitle())
                        .amount(expense.getAmount())
                        .category(expense.getCategory())
                        .description(expense.getDescription())
                        .build())
                .toList();
    }
    public ExpenseResponse getExpenseById(Long expenseId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense not found")
                );

        // Authorization Check
        if (!expense.getUser().getId()
                .equals(currentUser.getId())) {

            throw new UnauthorizedExpenseAccessException(
                    "You are not allowed to access this expense"
            );
        }

        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .build();
    }
    private Expense getExpenseForCurrentUser(Long expenseId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() ->
                        new ExpenseNotFoundException("Expense not found")
                );

        if (!expense.getUser().getId()
                .equals(currentUser.getId())) {

            throw new UnauthorizedExpenseAccessException(
                    "You are not allowed to access this expense"
            );
        }

        return expense;
    }
    public ExpenseResponse updateExpense(
            Long expenseId,
            UpdateExpenseRequest request
    ) {

        Expense expense =
                getExpenseForCurrentUser(expenseId);

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());

        Expense updatedExpense =
                expenseRepository.save(expense);

        return ExpenseResponse.builder()
                .id(updatedExpense.getId())
                .title(updatedExpense.getTitle())
                .amount(updatedExpense.getAmount())
                .category(updatedExpense.getCategory())
                .description(updatedExpense.getDescription())
                .build();
    }
    public void deleteExpense(Long expenseId) {

        Expense expense =
                getExpenseForCurrentUser(expenseId);

        expenseRepository.delete(expense);
    }
    public DashboardResponse getDashboardSummary() {

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

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        double spent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        long expenseCount = expenses.size();

        LocalDate now = LocalDate.now();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(
                        user,
                        now.getMonthValue(),
                        now.getYear()
                )
                .orElse(null);

        double budgetAmount =
                budget != null ? budget.getAmount() : 0;

        double remaining =
                budgetAmount - spent;

        return new DashboardResponse(
                budgetAmount,
                spent,
                remaining,
                expenseCount
        );
    }
    public List<CategorySummaryResponse>
    getCategorySummary() {

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

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        Map<String, Double> categoryTotals =
                expenses.stream()
                        .collect(Collectors.groupingBy(
                                Expense::getCategory,
                                Collectors.summingDouble(
                                        Expense::getAmount
                                )
                        ));

        return categoryTotals.entrySet()
                .stream()
                .map(entry ->
                        new CategorySummaryResponse(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .toList();
    }
    public MonthlyReportResponse getMonthlyReport() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found")
                );

        List<Expense> expenses =
                expenseRepository.findByUser(user);

        double spent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        long expenseCount = expenses.size();

        LocalDate now = LocalDate.now();

        Budget budget = budgetRepository
                .findByUserAndMonthAndYear(
                        user,
                        now.getMonthValue(),
                        now.getYear()
                )
                .orElse(null);

        double budgetAmount =
                budget != null ? budget.getAmount() : 0;

        double remaining =
                budgetAmount - spent;

        String topCategory = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(
                                Expense::getAmount
                        )
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No Expenses");

        return new MonthlyReportResponse(
                now.getMonth().toString(),
                budgetAmount,
                spent,
                remaining,
                expenseCount,
                topCategory
        );
    }
}