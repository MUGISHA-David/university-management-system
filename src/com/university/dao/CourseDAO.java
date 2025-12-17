package com.university.dao;

import com.university.model.Course;
import java.util.List;

/**
 * Data Access Object Interface for Course entity.
 */
public interface CourseDAO {
    
    boolean insert(Course course);
    boolean update(Course course);
    boolean delete(int courseId);
    Course findById(int courseId);
    Course findByCode(String courseCode);
    List<Course> findAll();
    List<Course> searchByName(String name);
    boolean codeExists(String courseCode);
}
