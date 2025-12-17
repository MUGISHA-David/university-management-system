package com.university.dao;

import com.university.model.Instructor;
import java.util.List;

/**
 * Data Access Object Interface for Instructor entity.
 */
public interface InstructorDAO {
    
    boolean insert(Instructor instructor);
    boolean update(Instructor instructor);
    boolean delete(int instructorId);
    Instructor findById(int instructorId);
    List<Instructor> findAll();
    List<Instructor> searchByName(String name);
    boolean emailExists(String email);
}
