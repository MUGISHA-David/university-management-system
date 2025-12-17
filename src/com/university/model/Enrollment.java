package com.university.model;

import java.time.LocalDate;

/**
 * Enrollment Model/Entity Class (POJO) - Many-to-Many relationship
 */
public class Enrollment {
    
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private LocalDate enrollmentDate;
    private String grade;
    private String status;
    
    // For display purposes
    private String studentName;
    private String courseName;
    
    // Default Constructor
    public Enrollment() {
    }
    
    // Parameterized Constructor
    public Enrollment(int enrollmentId, int studentId, int courseId, 
                      LocalDate enrollmentDate, String grade, String status) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.grade = grade;
        this.status = status;
    }
    
    // Getters and Setters
    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }
    
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }
    
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    @Override
    public String toString() {
        return "Enrollment{" + "studentId=" + studentId + ", courseId=" + courseId + ", status=" + status + "}";
    }
}
