package com.university.dao.impl;

import com.university.dao.StudentDAO;
import com.university.model.Student;
import com.university.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of StudentDAO.
 */
public class StudentDAOImpl implements StudentDAO {
    
    private Connection connection;
    
    public StudentDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public boolean insert(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, date_of_birth, gender, email, phone, address, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.setString(8, student.getStatus() != null ? student.getStatus() : "ACTIVE");
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Student student) {
        String sql = "UPDATE students SET first_name=?, last_name=?, date_of_birth=?, gender=?, email=?, phone=?, address=?, status=? WHERE student_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            stmt.setString(4, student.getGender());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getAddress());
            stmt.setString(8, student.getStatus());
            stmt.setInt(9, student.getStudentId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int studentId) {
        String sql = "DELETE FROM students WHERE student_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Student findById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRowToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    @Override
    public List<Student> searchByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchParam = "%" + name.toLowerCase() + "%";
            stmt.setString(1, searchParam);
            stmt.setString(2, searchParam);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    
    @Override
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM students WHERE email=?";
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
    
    @Override
    public boolean phoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM students WHERE phone=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setStudentId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        student.setGender(rs.getString("gender"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setAddress(rs.getString("address"));
        Date enrollDate = rs.getDate("enrollment_date");
        if (enrollDate != null) {
            student.setEnrollmentDate(enrollDate.toLocalDate());
        }
        student.setStatus(rs.getString("status"));
        return student;
    }
}
