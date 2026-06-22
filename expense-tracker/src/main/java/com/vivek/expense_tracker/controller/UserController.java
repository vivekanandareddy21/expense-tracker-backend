package com.vivek.expense_tracker.controller;

import com.vivek.expense_tracker.dto.CreateUserRequest;
import com.vivek.expense_tracker.dto.LoginRequest;
import com.vivek.expense_tracker.dto.UserResponse;
import com.vivek.expense_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
    @GetMapping("/profile")
    public String profile() {
        return "Protected Profile API Accessed";
    }
    @GetMapping("/me")
    public String getCurrentUser() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}