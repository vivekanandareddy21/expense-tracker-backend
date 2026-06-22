package com.vivek.expense_tracker.repository;

import com.vivek.expense_tracker.entity.Expense;
import com.vivek.expense_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
    @Query("""
SELECT e.category, SUM(e.amount)
FROM Expense e
WHERE e.user = :user
GROUP BY e.category
""")
    List<Object[]> getCategoryWiseReport(User user);
}