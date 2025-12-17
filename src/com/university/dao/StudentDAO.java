package com.university.dao;

import com.university.model.Student;
import java.util.List;

/**
 * Data Access Object Interface for Student entity.
 * Defines CRUD operations for Student.
 */
public interface StudentDAO {
    
    /**
     * Insert a new student into the database.
     * @param student The student to insert
     * @return true if successful, false otherwise
     */
    boolean insert(Student student);
    
    /**
     * Update an existing student in the database.
     * @param student The student to update
     * @return true if successful, false otherwise
     */
    boolean update(Student student);
    
    /**
     * Delete a student by ID.
     * @param studentId The ID of the student to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int studentId);
    
    /**
     * Find a student by ID.
     * @param studentId The ID to search for
     * @return The Student object if found, null otherwise
     */
    Student findById(int studentId);
    
    /**
     * Get all students from the database.
     * @return List of all students
     */
    List<Student> findAll();
    
    /**
     * Search students by name.
     * @param name The name or partial name to search
     * @return List of matching students
     */
    List<Student> searchByName(String name);
    
    /**
     * Check if email exists in database.
     * @param email The email to check
     * @return true if exists, false otherwise
     */
    boolean emailExists(String email);
    
    /**
     * Check if phone number exists in database.
     * @param phone The phone to check
     * @return true if exists, false otherwise
     */
    boolean phoneExists(String phone);
}
