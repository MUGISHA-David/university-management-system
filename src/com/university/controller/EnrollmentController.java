package com.university.controller;

import com.university.dao.EnrollmentDAO;
import com.university.dao.CourseDAO;
import com.university.dao.impl.EnrollmentDAOImpl;
import com.university.dao.impl.CourseDAOImpl;
import com.university.model.Enrollment;
import com.university.model.Course;
import com.university.util.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for Enrollment operations.
 */
public class EnrollmentController {

    private EnrollmentDAO enrollmentDAO;
    private CourseDAO courseDAO;
    private List<String> errors;

    public EnrollmentController() {
        this.enrollmentDAO = new EnrollmentDAOImpl();
        this.courseDAO = new CourseDAOImpl();
        this.errors = new ArrayList<>();
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentDAO.findAll();
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }

    /**
     * Add enrollment with business validations.
     */
    public boolean addEnrollment(Enrollment enrollment) {
        errors.clear();

        if (!validateEnrollment(enrollment)) {
            return false;
        }

        return enrollmentDAO.insert(enrollment);
    }

    public boolean updateEnrollment(Enrollment enrollment) {
        errors.clear();

        // Validate grade if provided
        if (enrollment.getGrade() != null && !enrollment.getGrade().isEmpty()) {
            if (!ValidationUtil.isValidGrade(enrollment.getGrade())) {
                errors.add(ValidationUtil.getGradeError());
                return false;
            }
        }

        return enrollmentDAO.update(enrollment);
    }

    public boolean deleteEnrollment(int enrollmentId) {
        errors.clear();
        return enrollmentDAO.delete(enrollmentId);
    }

    private boolean validateEnrollment(Enrollment enrollment) {
        // Business Rule 1: Student cannot enroll in the same course twice
        if (enrollmentDAO.enrollmentExists(enrollment.getStudentId(), enrollment.getCourseId())) {
            errors.add("Student is already enrolled in this course.");
            return false;
        }

        // Business Rule: Course capacity check
        Course course = courseDAO.findById(enrollment.getCourseId());
        if (course != null) {
            int currentEnrollments = enrollmentDAO.countEnrollmentsByCourse(enrollment.getCourseId());
            if (currentEnrollments >= course.getMaxCapacity()) {
                errors.add("Course has reached maximum capacity (" + course.getMaxCapacity() + " students).");
                return false;
            }
        }

        // Validate grade if provided
        if (enrollment.getGrade() != null && !enrollment.getGrade().isEmpty()) {
            if (!ValidationUtil.isValidGrade(enrollment.getGrade())) {
                errors.add(ValidationUtil.getGradeError());
            }
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
