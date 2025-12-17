package com.university;

import com.university.view.LoginFrame;
import javax.swing.*;

/**
 * Main Application Entry Point.
 * University Management System - Java Swing Application.
 * 
 * Features:
 * - MVC Design Pattern
 * - DAO Design Pattern
 * - JDBC for PostgreSQL Database
 * - Swing GUI with modern styling
 * - CRUD operations for Students, Courses, Instructors, Enrollments
 * - Business and Technical Validations
 * 
 * @author Student
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run application on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
