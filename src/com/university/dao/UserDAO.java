package com.university.dao;

import com.university.model.User;

/**
 * Data Access Object Interface for User entity (Login).
 */
public interface UserDAO {
    
    boolean insert(User user);
    boolean update(User user);
    boolean delete(int userId);
    User findById(int userId);
    User findByUsername(String username);
    User authenticate(String username, String password);
    boolean usernameExists(String username);
    boolean emailExists(String email);
}
