package com.university.dao.impl;

import com.university.dao.CourseDAO;
import com.university.model.Course;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of CourseDAO.
 */
public class CourseDAOImpl implements CourseDAO {
    
    private Connection connection;
    
    public CourseDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public boolean insert(Course course) {
        String sql = "INSERT INTO courses (course_code, course_name, credits, department, description, max_capacity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());
            stmt.setString(5, course.getDescription());
            stmt.setInt(6, course.getMaxCapacity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Course course) {
        String sql = "UPDATE courses SET course_code=?, course_name=?, credits=?, department=?, description=?, max_capacity=? WHERE course_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getDepartment());
            stmt.setString(5, course.getDescription());
            stmt.setInt(6, course.getMaxCapacity());
            stmt.setInt(7, course.getCourseId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Course findById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Course findByCode(String courseCode) {
        String sql = "SELECT * FROM courses WHERE course_code=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_code";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                courses.add(mapRowToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    @Override
    public List<Course> searchByName(String name) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses WHERE LOWER(course_name) LIKE ? OR LOWER(course_code) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchParam = "%" + name.toLowerCase() + "%";
            stmt.setString(1, searchParam);
            stmt.setString(2, searchParam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                courses.add(mapRowToCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    
    @Override
    public boolean codeExists(String courseCode) {
        String sql = "SELECT COUNT(*) FROM courses WHERE course_code=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Course mapRowToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseId(rs.getInt("course_id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartment(rs.getString("department"));
        course.setDescription(rs.getString("description"));
        course.setMaxCapacity(rs.getInt("max_capacity"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            course.setCreatedAt(createdAt.toLocalDateTime());
        }
        return course;
    }
}
