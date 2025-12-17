package com.university.dao.impl;

import com.university.dao.InstructorDAO;
import com.university.model.Instructor;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of InstructorDAO.
 */
public class InstructorDAOImpl implements InstructorDAO {
    
    private Connection connection;
    
    public InstructorDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public boolean insert(Instructor instructor) {
        String sql = "INSERT INTO instructors (first_name, last_name, qualification, email, phone, department) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, instructor.getFirstName());
            stmt.setString(2, instructor.getLastName());
            stmt.setString(3, instructor.getQualification());
            stmt.setString(4, instructor.getEmail());
            stmt.setString(5, instructor.getPhone());
            stmt.setString(6, instructor.getDepartment());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Instructor instructor) {
        String sql = "UPDATE instructors SET first_name=?, last_name=?, qualification=?, email=?, phone=?, department=? WHERE instructor_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, instructor.getFirstName());
            stmt.setString(2, instructor.getLastName());
            stmt.setString(3, instructor.getQualification());
            stmt.setString(4, instructor.getEmail());
            stmt.setString(5, instructor.getPhone());
            stmt.setString(6, instructor.getDepartment());
            stmt.setInt(7, instructor.getInstructorId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int instructorId) {
        String sql = "DELETE FROM instructors WHERE instructor_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, instructorId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Instructor findById(int instructorId) {
        String sql = "SELECT * FROM instructors WHERE instructor_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToInstructor(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Instructor> findAll() {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructors ORDER BY instructor_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                instructors.add(mapRowToInstructor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }
    
    @Override
    public List<Instructor> searchByName(String name) {
        List<Instructor> instructors = new ArrayList<>();
        String sql = "SELECT * FROM instructors WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchParam = "%" + name.toLowerCase() + "%";
            stmt.setString(1, searchParam);
            stmt.setString(2, searchParam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                instructors.add(mapRowToInstructor(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instructors;
    }
    
    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM instructors WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Instructor mapRowToInstructor(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor();
        instructor.setInstructorId(rs.getInt("instructor_id"));
        instructor.setFirstName(rs.getString("first_name"));
        instructor.setLastName(rs.getString("last_name"));
        instructor.setQualification(rs.getString("qualification"));
        instructor.setEmail(rs.getString("email"));
        instructor.setPhone(rs.getString("phone"));
        instructor.setDepartment(rs.getString("department"));
        Date hireDate = rs.getDate("hire_date");
        if (hireDate != null) {
            instructor.setHireDate(hireDate.toLocalDate());
        }
        return instructor;
    }
}
