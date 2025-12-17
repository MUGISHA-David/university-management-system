package com.university.view;

import com.university.controller.EnrollmentController;
import com.university.controller.StudentController;
import com.university.controller.CourseController;
import com.university.model.Enrollment;
import com.university.model.Student;
import com.university.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Enrollment Panel - CRUD operations for Enrollments.
 */
public class EnrollmentPanel extends JPanel {

    private EnrollmentController controller;
    private StudentController studentController;
    private CourseController courseController;

    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    private JComboBox<StudentItem> studentCombo;
    private JComboBox<CourseItem> courseCombo;
    private JTextField gradeField;
    private JComboBox<String> statusCombo;

    private int selectedEnrollmentId = -1;

    public EnrollmentPanel() {
        controller = new EnrollmentController();
        studentController = new StudentController();
        courseController = new CourseController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(StyleUtil.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.WEST);

        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formCard = StyleUtil.createCardPanel();
        formCard.setLayout(new GridBagLayout());
        formCard.setPreferredSize(new Dimension(350, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel formTitle = new JLabel("Enrollment Form");
        formTitle.setFont(StyleUtil.SUBTITLE_FONT);
        formTitle.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 15, 5);
        formCard.add(formTitle, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Student
        gbc.gridy = 1;
        formCard.add(createLabel("Student *"), gbc);
        gbc.gridx = 1;
        studentCombo = new JComboBox<>();
        StyleUtil.styleComboBox(studentCombo);
        formCard.add(studentCombo, gbc);

        // Course
        gbc.gridy = 2;
        gbc.gridx = 0;
        formCard.add(createLabel("Course *"), gbc);
        gbc.gridx = 1;
        courseCombo = new JComboBox<>();
        StyleUtil.styleComboBox(courseCombo);
        formCard.add(courseCombo, gbc);

        // Grade
        gbc.gridy = 3;
        gbc.gridx = 0;
        formCard.add(createLabel("Grade"), gbc);
        gbc.gridx = 1;
        gradeField = new JTextField(15);
        gradeField.setToolTipText("e.g., A, B+, C-");
        StyleUtil.styleTextField(gradeField);
        formCard.add(gradeField, gbc);

        // Status
        gbc.gridy = 4;
        gbc.gridx = 0;
        formCard.add(createLabel("Status"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[] { "ENROLLED", "COMPLETED", "DROPPED" });
        StyleUtil.styleComboBox(statusCombo);
        formCard.add(statusCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton enrollBtn = new JButton("Enroll");
        StyleUtil.styleSuccessButton(enrollBtn);
        enrollBtn.addActionListener(e -> addEnrollment());
        buttonPanel.add(enrollBtn);

        JButton updateBtn = new JButton("Update");
        StyleUtil.stylePrimaryButton(updateBtn);
        updateBtn.addActionListener(e -> updateEnrollment());
        buttonPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Remove");
        StyleUtil.styleDangerButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteEnrollment());
        buttonPanel.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        StyleUtil.styleSecondaryButton(clearBtn);
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formCard.add(buttonPanel, gbc);

        // Refresh combos button
        JButton refreshCombosBtn = new JButton("Refresh Students/Courses");
        StyleUtil.styleSecondaryButton(refreshCombosBtn);
        refreshCombosBtn.setPreferredSize(new Dimension(250, 35));
        refreshCombosBtn.addActionListener(e -> loadCombos());
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 5, 5, 5);
        formCard.add(refreshCombosBtn, gbc);

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableCard = StyleUtil.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        StyleUtil.stylePrimaryButton(refreshBtn);
        refreshBtn.addActionListener(e -> loadData());
        topPanel.add(refreshBtn);

        tableCard.add(topPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "Student", "Course", "Enrollment Date", "Grade", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        enrollmentTable = new JTable(tableModel);
        StyleUtil.styleTable(enrollmentTable);

        enrollmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedEnrollment();
            }
        });

        JScrollPane scrollPane = new JScrollPane(enrollmentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        StyleUtil.styleLabel(label);
        return label;
    }

    private void loadCombos() {
        studentCombo.removeAllItems();
        List<Student> students = studentController.getAllStudents();
        for (Student s : students) {
            studentCombo.addItem(new StudentItem(s.getStudentId(), s.getFullName()));
        }

        courseCombo.removeAllItems();
        List<Course> courses = courseController.getAllCourses();
        for (Course c : courses) {
            courseCombo.addItem(new CourseItem(c.getCourseId(), c.getCourseCode() + " - " + c.getCourseName()));
        }
    }

    private void loadData() {
        loadCombos();

        tableModel.setRowCount(0);
        List<Enrollment> enrollments = controller.getAllEnrollments();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Enrollment e : enrollments) {
            tableModel.addRow(new Object[] {
                    e.getEnrollmentId(),
                    e.getStudentName(),
                    e.getCourseName(),
                    e.getEnrollmentDate() != null ? e.getEnrollmentDate().format(formatter) : "",
                    e.getGrade() != null ? e.getGrade() : "",
                    e.getStatus()
            });
        }
        clearForm();
    }

    public void refreshData() {
        loadData();
    }

    private void loadSelectedEnrollment() {
        int selectedRow = enrollmentTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedEnrollmentId = (int) tableModel.getValueAt(selectedRow, 0);

            String grade = (String) tableModel.getValueAt(selectedRow, 4);
            gradeField.setText(grade != null ? grade : "");

            String status = (String) tableModel.getValueAt(selectedRow, 5);
            statusCombo.setSelectedItem(status);
        }
    }

    private void addEnrollment() {
        if (studentCombo.getSelectedItem() == null || courseCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select both a student and a course.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(((StudentItem) studentCombo.getSelectedItem()).getId());
        enrollment.setCourseId(((CourseItem) courseCombo.getSelectedItem()).getId());
        enrollment.setGrade(gradeField.getText().trim());
        enrollment.setStatus((String) statusCombo.getSelectedItem());

        if (controller.addEnrollment(enrollment)) {
            JOptionPane.showMessageDialog(this,
                    "Student enrolled successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    controller.getErrorMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateEnrollment() {
        if (selectedEnrollmentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an enrollment to update.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(selectedEnrollmentId);
        enrollment.setStudentId(((StudentItem) studentCombo.getSelectedItem()).getId());
        enrollment.setCourseId(((CourseItem) courseCombo.getSelectedItem()).getId());
        enrollment.setGrade(gradeField.getText().trim());
        enrollment.setStatus((String) statusCombo.getSelectedItem());

        if (controller.updateEnrollment(enrollment)) {
            JOptionPane.showMessageDialog(this,
                    "Enrollment updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            loadData();
        } else {
            JOptionPane.showMessageDialog(this,
                    controller.getErrorMessage(),
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteEnrollment() {
        if (selectedEnrollmentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an enrollment to remove.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove this enrollment?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteEnrollment(selectedEnrollmentId)) {
                JOptionPane.showMessageDialog(this,
                        "Enrollment removed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to remove enrollment.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedEnrollmentId = -1;
        if (studentCombo.getItemCount() > 0)
            studentCombo.setSelectedIndex(0);
        if (courseCombo.getItemCount() > 0)
            courseCombo.setSelectedIndex(0);
        gradeField.setText("");
        statusCombo.setSelectedIndex(0);
        enrollmentTable.clearSelection();
    }

    // Helper classes for ComboBox items
    private static class StudentItem {
        private int id;
        private String name;

        public StudentItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class CourseItem {
        private int id;
        private String name;

        public CourseItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
