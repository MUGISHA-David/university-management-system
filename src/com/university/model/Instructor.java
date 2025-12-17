package com.university.model;

import java.time.LocalDate;

/**
 * Instructor Model/Entity Class (POJO)
 */
public class Instructor {
    
    private int instructorId;
    private String firstName;
    private String lastName;
    private String qualification;
    private String email;
    private String phone;
    private String department;
    private LocalDate hireDate;
    
    // Default Constructor
    public Instructor() {
    }
    
    // Parameterized Constructor
    public Instructor(int instructorId, String firstName, String lastName, 
                      String qualification, String email, String phone, 
                      String department, LocalDate hireDate) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.qualification = qualification;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.hireDate = hireDate;
    }
    
    // Getters and Setters
    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + qualification + ")";
    }
}
