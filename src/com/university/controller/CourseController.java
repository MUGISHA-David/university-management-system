package com.university.controller;

import com.university.dao.CourseDAO;
import com.university.dao.impl.CourseDAOImpl;
import com.university.model.Course;
import com.university.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Course operations.
 */
public class CourseController {

    private CourseDAO courseDAO;
    private List<String> errors;

    public CourseController() {
        this.courseDAO = new CourseDAOImpl();
        this.errors = new ArrayList<>();
    }

    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    public List<Course> searchCourses(String name) {
        return courseDAO.searchByName(name);
    }

    public boolean addCourse(Course course) {
        errors.clear();

        if (!validateCourse(course, true)) {
            return false;
        }

        return courseDAO.insert(course);
    }

    public boolean updateCourse(Course course) {
        errors.clear();

        if (!validateCourse(course, false)) {
            return false;
        }

        return courseDAO.update(course);
    }

    public boolean deleteCourse(int courseId) {
        errors.clear();
        return courseDAO.delete(courseId);
    }

    private boolean validateCourse(Course course, boolean isNew) {
        // Technical: Course code not empty
        if (!ValidationUtil.isNotEmpty(course.getCourseCode())) {
            errors.add(ValidationUtil.getEmptyFieldError("Course Code"));
        } else if (isNew && courseDAO.codeExists(course.getCourseCode())) {
            errors.add("Course code already exists.");
        }

        // Technical: Course name not empty
        if (!ValidationUtil.isNotEmpty(course.getCourseName())) {
            errors.add(ValidationUtil.getEmptyFieldError("Course Name"));
        }

        // Technical: Department not empty
        if (!ValidationUtil.isNotEmpty(course.getDepartment())) {
            errors.add(ValidationUtil.getEmptyFieldError("Department"));
        }

        // Business: Credits between 1 and 6
        if (!ValidationUtil.isValidCredits(course.getCredits())) {
            errors.add(ValidationUtil.getCreditsError());
        }

        // Business: Capacity is valid
        if (!ValidationUtil.isValidCourseCapacity(course.getMaxCapacity())) {
            errors.add("Course capacity must be between 1 and 500.");
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
