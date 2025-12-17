package com.university.dao;

import com.university.model.Enrollment;
import java.util.List;

/**
 * Data Access Object Interface for Enrollment entity.
 */
public interface EnrollmentDAO {
    
    boolean insert(Enrollment enrollment);
    boolean update(Enrollment enrollment);
    boolean delete(int enrollmentId);
    Enrollment findById(int enrollmentId);
    List<Enrollment> findAll();
    List<Enrollment> findByStudentId(int studentId);
    List<Enrollment> findByCourseId(int courseId);
    boolean enrollmentExists(int studentId, int courseId);
    int countEnrollmentsByCourse(int courseId);
}
