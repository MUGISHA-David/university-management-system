package com.university.controller;

import com.university.dao.UserDAO;
import com.university.dao.impl.UserDAOImpl;
import com.university.model.User;
import com.university.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for User authentication and management.
 */
public class UserController {

    private UserDAO userDAO;
    private List<String> errors;
    private User currentUser;

    public UserController() {
        this.userDAO = new UserDAOImpl();
        this.errors = new ArrayList<>();
    }

    /**
     * Authenticate user login.
     * 
     * @return User if successful, null otherwise
     */
    public User login(String username, String password) {
        errors.clear();

        if (!ValidationUtil.isNotEmpty(username)) {
            errors.add("Username is required.");
            return null;
        }

        if (!ValidationUtil.isNotEmpty(password)) {
            errors.add("Password is required.");
            return null;
        }

        currentUser = userDAO.authenticate(username, password);
        if (currentUser == null) {
            errors.add("Invalid username or password.");
        }

        return currentUser;
    }

    /**
     * Register a new user.
     */
    public boolean registerUser(User user) {
        errors.clear();

        if (!validateUser(user)) {
            return false;
        }

        return userDAO.insert(user);
    }

    /**
     * Get current logged in user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Logout current user.
     */
    public void logout() {
        currentUser = null;
    }

    private boolean validateUser(User user) {
        // Technical: Username not empty
        if (!ValidationUtil.isNotEmpty(user.getUsername())) {
            errors.add(ValidationUtil.getEmptyFieldError("Username"));
        } else if (userDAO.usernameExists(user.getUsername())) {
            errors.add("Username already exists.");
        }

        // Technical: Password strength (users table has 2 technical validations)
        if (!ValidationUtil.isValidPassword(user.getPassword())) {
            errors.add(ValidationUtil.getPasswordError());
        }

        // Technical: Email format (second validation on users table)
        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            errors.add(ValidationUtil.getEmailError());
        } else if (userDAO.emailExists(user.getEmail())) {
            errors.add("Email already exists.");
        }

        return errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        if (errors.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String error : errors) {
            sb.append("• ").append(error).append("\n");
        }
        return sb.toString();
    }
}
