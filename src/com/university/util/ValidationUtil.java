package com.university.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

/**
 * Validation Utility Class.
 * Contains Business and Technical validation rules.
 */
public class ValidationUtil {
    
    // Technical Validation Patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^.{8,}$"  // Minimum 8 characters
    );
    
    // ==================== TECHNICAL VALIDATIONS ====================
    
    /**
     * Technical Rule 1: Validates email format.
     * @param email The email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Technical Rule 2: Validates phone number format (10-15 digits).
     * @param phone The phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * Technical Rule 3: Validates that a field is not empty.
     * @param value The value to check
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Technical Rule 4: Validates password strength (min 8 characters).
     * @param password The password to validate
     * @return true if password meets requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Technical Rule 5: Validates date is not in the future.
     * @param date The date to validate
     * @return true if date is not in future
     */
    public static boolean isNotFutureDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isAfter(LocalDate.now());
    }
    
    // ==================== BUSINESS VALIDATIONS ====================
    
    /**
     * Business Rule 1: Student must be at least 16 years old.
     * @param dateOfBirth The student's date of birth
     * @return true if student is 16 or older
     */
    public static boolean isValidStudentAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return age >= 16;
    }
    
    /**
     * Business Rule 2: Course credits must be between 1 and 6.
     * @param credits The number of credits
     * @return true if credits are valid
     */
    public static boolean isValidCredits(int credits) {
        return credits >= 1 && credits <= 6;
    }
    
    /**
     * Business Rule 3: Course capacity must be positive and reasonable.
     * @param capacity The maximum capacity
     * @return true if capacity is valid
     */
    public static boolean isValidCourseCapacity(int capacity) {
        return capacity > 0 && capacity <= 500;
    }
    
    /**
     * Business Rule 4: Instructor qualification must not be empty and meaningful.
     * @param qualification The qualification string
     * @return true if qualification is valid
     */
    public static boolean isValidQualification(String qualification) {
        if (qualification == null || qualification.trim().isEmpty()) {
            return false;
        }
        // Must have at least 3 characters (e.g., "PhD", "MSc", etc.)
        return qualification.trim().length() >= 3;
    }
    
    /**
     * Business Rule 5: Grade must be valid (A, B, C, D, F, or empty).
     * @param grade The grade to validate
     * @return true if grade is valid
     */
    public static boolean isValidGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            return true; // Empty grade is allowed (not yet graded)
        }
        String[] validGrades = {"A", "A+", "A-", "B", "B+", "B-", "C", "C+", "C-", "D", "D+", "D-", "F"};
        for (String valid : validGrades) {
            if (valid.equalsIgnoreCase(grade.trim())) {
                return true;
            }
        }
        return false;
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Get validation error message for email.
     */
    public static String getEmailError() {
        return "Invalid email format. Example: user@example.com";
    }
    
    /**
     * Get validation error message for phone.
     */
    public static String getPhoneError() {
        return "Phone must contain 10-15 digits only.";
    }
    
    /**
     * Get validation error message for empty field.
     */
    public static String getEmptyFieldError(String fieldName) {
        return fieldName + " cannot be empty.";
    }
    
    /**
     * Get validation error message for password.
     */
    public static String getPasswordError() {
        return "Password must be at least 8 characters.";
    }
    
    /**
     * Get validation error message for student age.
     */
    public static String getStudentAgeError() {
        return "Student must be at least 16 years old.";
    }
    
    /**
     * Get validation error message for credits.
     */
    public static String getCreditsError() {
        return "Credits must be between 1 and 6.";
    }
    
    /**
     * Get validation error message for qualification.
     */
    public static String getQualificationError() {
        return "Qualification must have at least 3 characters.";
    }
    
    /**
     * Get validation error message for grade.
     */
    public static String getGradeError() {
        return "Invalid grade. Valid grades: A, A+, A-, B, B+, B-, C, C+, C-, D, D+, D-, F";
    }
}
