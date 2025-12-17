package com.university.controller;

import com.university.dao.InstructorDAO;
import com.university.dao.impl.InstructorDAOImpl;
import com.university.model.Instructor;
import com.university.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Instructor operations.
 */
public class InstructorController {

    private InstructorDAO instructorDAO;
    private List<String> errors;

    public InstructorController() {
        this.instructorDAO = new InstructorDAOImpl();
        this.errors = new ArrayList<>();
    }

    public List<Instructor> getAllInstructors() {
        return instructorDAO.findAll();
    }

    public Instructor getInstructorById(int id) {
        return instructorDAO.findById(id);
    }

    public List<Instructor> searchInstructors(String name) {
        return instructorDAO.searchByName(name);
    }

    public boolean addInstructor(Instructor instructor) {
        errors.clear();

        if (!validateInstructor(instructor, true)) {
            return false;
        }

        return instructorDAO.insert(instructor);
    }

    public boolean updateInstructor(Instructor instructor) {
        errors.clear();

        if (!validateInstructor(instructor, false)) {
            return false;
        }

        return instructorDAO.update(instructor);
    }

    public boolean deleteInstructor(int instructorId) {
        errors.clear();
        return instructorDAO.delete(instructorId);
    }

    private boolean validateInstructor(Instructor instructor, boolean isNew) {
        // Technical: First name not empty
        if (!ValidationUtil.isNotEmpty(instructor.getFirstName())) {
            errors.add(ValidationUtil.getEmptyFieldError("First Name"));
        }

        // Technical: Last name not empty
        if (!ValidationUtil.isNotEmpty(instructor.getLastName())) {
            errors.add(ValidationUtil.getEmptyFieldError("Last Name"));
        }

        // Technical: Email format
        if (!ValidationUtil.isValidEmail(instructor.getEmail())) {
            errors.add(ValidationUtil.getEmailError());
        } else if (isNew && instructorDAO.emailExists(instructor.getEmail())) {
            errors.add("Email already exists.");
        }

        // Technical: Phone format
        if (!ValidationUtil.isValidPhone(instructor.getPhone())) {
            errors.add(ValidationUtil.getPhoneError());
        }

        // Technical: Department not empty
        if (!ValidationUtil.isNotEmpty(instructor.getDepartment())) {
            errors.add(ValidationUtil.getEmptyFieldError("Department"));
        }

        // Business: Valid qualification
        if (!ValidationUtil.isValidQualification(instructor.getQualification())) {
            errors.add(ValidationUtil.getQualificationError());
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
        StringBuilder sb = new StringBuilder("Validation Errors:\n");
        for (String error : errors) {
            sb.append("• ").append(error).append("\n");
        }
        return sb.toString();
    }
}
