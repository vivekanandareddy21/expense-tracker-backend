package com.vivek.expense_tracker.service;

import com.vivek.expense_tracker.dto.CategoryReportResponse;
import com.vivek.expense_tracker.entity.User;
import com.vivek.expense_tracker.exception.UserNotFoundException;
import com.vivek.expense_tracker.repository.ExpenseRepository;
import com.vivek.expense_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public List<CategoryReportResponse> getCategoryReport() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        List<Object[]> results =
                expenseRepository.getCategoryWiseReport(user);

        List<CategoryReportResponse> report =
                new ArrayList<>();

        for (Object[] row : results) {

            report.add(
                    new CategoryReportResponse(
                            (String) row[0],
                            ((Number) row[1]).doubleValue()
                    )
            );
        }

        return report;
    }
}