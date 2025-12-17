package com.university.model;

import java.time.LocalDateTime;

/**
 * Course Model/Entity Class (POJO)
 */
public class Course {
    
    private int courseId;
    private String courseCode;
    private String courseName;
    private int credits;
    private String department;
    private String description;
    private int maxCapacity;
    private LocalDateTime createdAt;
    
    // Default Constructor
    public Course() {
    }
    
    // Parameterized Constructor
    public Course(int courseId, String courseCode, String courseName, int credits, 
                  String department, String description, int maxCapacity) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.department = department;
        this.description = description;
        this.maxCapacity = maxCapacity;
    }
    
    // Getters and Setters
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}
