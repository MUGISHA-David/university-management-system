package com.university.controller;

import com.university.dao.StudentDAO;
import com.university.dao.impl.StudentDAOImpl;
import com.university.model.Student;
import com.university.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Student operations.
 * Handles business logic between View and DAO.
 */
public class StudentController {

    private StudentDAO studentDAO;
    private List<String> errors;

    public StudentController() {
        this.studentDAO = new StudentDAOImpl();
        this.errors = new ArrayList<>();
    }

    /**
     * Get all students.
     */
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    /**
     * Get student by ID.
     */
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    /**
     * Search students by name.
     */
    public List<Student> searchStudents(String name) {
        return studentDAO.searchByName(name);
    }

    /**
     * Add a new student with validation.
     * 
     * @return true if successful
     */
    public boolean addStudent(Student student) {
        errors.clear();

        // Validate student data
        if (!validateStudent(student, true)) {
            return false;
        }

        return studentDAO.insert(student);
    }

    /**
     * Update an existing student with validation.
     * 
     * @return true if successful
     */
    public boolean updateStudent(Student student) {
        errors.clear();

        // Validate student data
        if (!validateStudent(student, false)) {
            return false;
        }

        return studentDAO.update(student);
    }

    /**
     * Delete a student.
     * 
     * @return true if successful
     */
    public boolean deleteStudent(int studentId) {
        errors.clear();
        return studentDAO.delete(studentId);
    }

    /**
     * Validate student data.
     * 
     * @param student The student to validate
     * @param isNew   Whether this is a new student (for unique checks)
     * @return true if all validations pass
     */
    private boolean validateStudent(Student student, boolean isNew) {
        // Technical Validation 1: First name not empty
        if (!ValidationUtil.isNotEmpty(student.getFirstName())) {
            errors.add(ValidationUtil.getEmptyFieldError("First Name"));
        }

        // Technical Validation 2: Last name not empty
        if (!ValidationUtil.isNotEmpty(student.getLastName())) {
            errors.add(ValidationUtil.getEmptyFieldError("Last Name"));
        }

        // Technical Validation 3: Email format
        if (!ValidationUtil.isValidEmail(student.getEmail())) {
            errors.add(ValidationUtil.getEmailError());
        } else if (isNew && studentDAO.emailExists(student.getEmail())) {
            errors.add("Email already exists in the system.");
        }

        // Technical Validation 4: Phone format
        if (!ValidationUtil.isValidPhone(student.getPhone())) {
            errors.add(ValidationUtil.getPhoneError());
        } else if (isNew && studentDAO.phoneExists(student.getPhone())) {
            errors.add("Phone number already exists in the system.");
        }

        // Technical Validation 5: Date of birth not in future
        if (!ValidationUtil.isNotFutureDate(student.getDateOfBirth())) {
            errors.add("Date of birth cannot be in the future.");
        }

        // Business Validation 1: Student must be at least 16 years old
        if (!ValidationUtil.isValidStudentAge(student.getDateOfBirth())) {
            errors.add(ValidationUtil.getStudentAgeError());
        }

        // Gender validation
        if (!ValidationUtil.isNotEmpty(student.getGender())) {
            errors.add(ValidationUtil.getEmptyFieldError("Gender"));
        }

        return errors.isEmpty();
    }

    /**
     * Get validation errors.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Get formatted error message.
     */
    public String getErrorMessage() {
        if (errors.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Validation Errors:\n");
        for (String error : errors) {
            sb.append("• ").append(error).append("\n");
        }
        return sb.toString();
    }
}
