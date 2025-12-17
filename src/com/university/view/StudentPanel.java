package com.university.view;

import com.university.controller.StudentController;
import com.university.model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Student Panel - CRUD operations for Students.
 * Displayed in Dashboard content area.
 */
public class StudentPanel extends JPanel {

    private StudentController controller;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField dobField;
    private JComboBox<String> genderCombo;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextArea addressArea;
    private JComboBox<String> statusCombo;

    private JTextField searchField;
    private int selectedStudentId = -1;

    public StudentPanel() {
        controller = new StudentController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(StyleUtil.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left: Form Panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.WEST);

        // Right: Table Panel
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

        JLabel formTitle = new JLabel("Student Form");
        formTitle.setFont(StyleUtil.SUBTITLE_FONT);
        formTitle.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 15, 5);
        formCard.add(formTitle, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First Name
        gbc.gridy = 1;
        formCard.add(createLabel("First Name *"), gbc);
        gbc.gridx = 1;
        firstNameField = new JTextField(15);
        StyleUtil.styleTextField(firstNameField);
        formCard.add(firstNameField, gbc);

        // Last Name
        gbc.gridy = 2;
        gbc.gridx = 0;
        formCard.add(createLabel("Last Name *"), gbc);
        gbc.gridx = 1;
        lastNameField = new JTextField(15);
        StyleUtil.styleTextField(lastNameField);
        formCard.add(lastNameField, gbc);

        // Date of Birth
        gbc.gridy = 3;
        gbc.gridx = 0;
        formCard.add(createLabel("Date of Birth *"), gbc);
        gbc.gridx = 1;
        dobField = new JTextField(15);
        dobField.setToolTipText("Format: YYYY-MM-DD");
        StyleUtil.styleTextField(dobField);
        formCard.add(dobField, gbc);

        // Gender
        gbc.gridy = 4;
        gbc.gridx = 0;
        formCard.add(createLabel("Gender *"), gbc);
        gbc.gridx = 1;
        genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        StyleUtil.styleComboBox(genderCombo);
        formCard.add(genderCombo, gbc);

        // Email
        gbc.gridy = 5;
        gbc.gridx = 0;
        formCard.add(createLabel("Email *"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        StyleUtil.styleTextField(emailField);
        formCard.add(emailField, gbc);

        // Phone
        gbc.gridy = 6;
        gbc.gridx = 0;
        formCard.add(createLabel("Phone *"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        phoneField.setToolTipText("10-15 digits only");
        StyleUtil.styleTextField(phoneField);
        formCard.add(phoneField, gbc);

        // Address
        gbc.gridy = 7;
        gbc.gridx = 0;
        formCard.add(createLabel("Address"), gbc);
        gbc.gridx = 1;
        addressArea = new JTextArea(3, 15);
        addressArea.setFont(StyleUtil.BODY_FONT);
        addressArea.setLineWrap(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(250, 60));
        formCard.add(addressScroll, gbc);

        // Status
        gbc.gridy = 8;
        gbc.gridx = 0;
        formCard.add(createLabel("Status"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[] { "ACTIVE", "INACTIVE", "GRADUATED", "SUSPENDED" });
        StyleUtil.styleComboBox(statusCombo);
        formCard.add(statusCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add");
        StyleUtil.styleSuccessButton(addBtn);
        addBtn.addActionListener(e -> addStudent());
        buttonPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        StyleUtil.stylePrimaryButton(updateBtn);
        updateBtn.addActionListener(e -> updateStudent());
        buttonPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        StyleUtil.styleDangerButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteStudent());
        buttonPanel.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        StyleUtil.styleSecondaryButton(clearBtn);
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);

        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formCard.add(buttonPanel, gbc);

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableCard = StyleUtil.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

        // Top: Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search: ");
        searchLabel.setFont(StyleUtil.BODY_FONT);
        searchPanel.add(searchLabel);

        searchField = new JTextField(20);
        StyleUtil.styleTextField(searchField);
        searchPanel.add(searchField);

        JButton searchBtn = new JButton("Search");
        StyleUtil.stylePrimaryButton(searchBtn);
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchBtn.addActionListener(e -> searchStudents());
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        StyleUtil.styleSecondaryButton(refreshBtn);
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.addActionListener(e -> loadData());
        searchPanel.add(refreshBtn);

        tableCard.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "ID", "First Name", "Last Name", "DOB", "Gender", "Email", "Phone", "Status" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        StyleUtil.styleTable(studentTable);

        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(scrollPane, BorderLayout.CENTER);

        return tableCard;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        StyleUtil.styleLabel(label);
        return label;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Student> students = controller.getAllStudents();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Student s : students) {
            tableModel.addRow(new Object[] {
                    s.getStudentId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getDateOfBirth() != null ? s.getDateOfBirth().format(formatter) : "",
                    s.getGender(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getStatus()
            });
        }
        clearForm();
    }

    public void refreshData() {
        loadData();
    }

    private void loadSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedStudentId = (int) tableModel.getValueAt(selectedRow, 0);
            firstNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            lastNameField.setText((String) tableModel.getValueAt(selectedRow, 2));
            dobField.setText((String) tableModel.getValueAt(selectedRow, 3));
            genderCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 5));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 6));
            statusCombo.setSelectedItem(tableModel.getValueAt(selectedRow, 7));

            // Load full student for address
            Student student = controller.getStudentById(selectedStudentId);
            if (student != null) {
                addressArea.setText(student.getAddress());
            }
        }
    }

    private void addStudent() {
        Student student = getStudentFromForm();
        if (student == null)
            return;

        if (controller.addStudent(student)) {
            JOptionPane.showMessageDialog(this,
                    "Student added successfully!",
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

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to update.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = getStudentFromForm();
        if (student == null)
            return;
        student.setStudentId(selectedStudentId);

        if (controller.updateStudent(student)) {
            JOptionPane.showMessageDialog(this,
                    "Student updated successfully!",
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

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to delete.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteStudent(selectedStudentId)) {
                JOptionPane.showMessageDialog(this,
                        "Student deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete student.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedStudentId = -1;
        firstNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        genderCombo.setSelectedIndex(0);
        emailField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        statusCombo.setSelectedIndex(0);
        studentTable.clearSelection();
    }

    private void searchStudents() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Student> students = controller.searchStudents(searchTerm);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Student s : students) {
            tableModel.addRow(new Object[] {
                    s.getStudentId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getDateOfBirth() != null ? s.getDateOfBirth().format(formatter) : "",
                    s.getGender(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getStatus()
            });
        }
    }

    private Student getStudentFromForm() {
        try {
            Student student = new Student();
            student.setFirstName(firstNameField.getText().trim());
            student.setLastName(lastNameField.getText().trim());

            String dobText = dobField.getText().trim();
            if (!dobText.isEmpty()) {
                student.setDateOfBirth(LocalDate.parse(dobText));
            }

            student.setGender((String) genderCombo.getSelectedItem());
            student.setEmail(emailField.getText().trim());
            student.setPhone(phoneField.getText().trim());
            student.setAddress(addressArea.getText().trim());
            student.setStatus((String) statusCombo.getSelectedItem());

            return student;
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid date format. Please use YYYY-MM-DD.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
