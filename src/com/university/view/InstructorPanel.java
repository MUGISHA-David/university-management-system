package com.university.view;

import com.university.controller.InstructorController;
import com.university.model.Instructor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Instructor Panel - CRUD operations for Instructors.
 */
public class InstructorPanel extends JPanel {

    private InstructorController controller;
    private JTable instructorTable;
    private DefaultTableModel tableModel;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField qualificationField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField departmentField;

    private JTextField searchField;
    private int selectedInstructorId = -1;

    public InstructorPanel() {
        controller = new InstructorController();
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

        JLabel formTitle = new JLabel("Instructor Form");
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

        // Qualification
        gbc.gridy = 3;
        gbc.gridx = 0;
        formCard.add(createLabel("Qualification *"), gbc);
        gbc.gridx = 1;
        qualificationField = new JTextField(15);
        qualificationField.setToolTipText("e.g., PhD, MSc, BSc");
        StyleUtil.styleTextField(qualificationField);
        formCard.add(qualificationField, gbc);

        // Email
        gbc.gridy = 4;
        gbc.gridx = 0;
        formCard.add(createLabel("Email *"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        StyleUtil.styleTextField(emailField);
        formCard.add(emailField, gbc);

        // Phone
        gbc.gridy = 5;
        gbc.gridx = 0;
        formCard.add(createLabel("Phone *"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        StyleUtil.styleTextField(phoneField);
        formCard.add(phoneField, gbc);

        // Department
        gbc.gridy = 6;
        gbc.gridx = 0;
        formCard.add(createLabel("Department *"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(15);
        StyleUtil.styleTextField(departmentField);
        formCard.add(departmentField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add");
        StyleUtil.styleSuccessButton(addBtn);
        addBtn.addActionListener(e -> addInstructor());
        buttonPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        StyleUtil.stylePrimaryButton(updateBtn);
        updateBtn.addActionListener(e -> updateInstructor());
        buttonPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        StyleUtil.styleDangerButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteInstructor());
        buttonPanel.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        StyleUtil.styleSecondaryButton(clearBtn);
        clearBtn.addActionListener(e -> clearForm());
        buttonPanel.add(clearBtn);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        formCard.add(buttonPanel, gbc);

        return formCard;
    }

    private JPanel createTablePanel() {
        JPanel tableCard = StyleUtil.createCardPanel();
        tableCard.setLayout(new BorderLayout(10, 10));

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
        searchBtn.addActionListener(e -> searchInstructors());
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        StyleUtil.styleSecondaryButton(refreshBtn);
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.addActionListener(e -> loadData());
        searchPanel.add(refreshBtn);

        tableCard.add(searchPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "First Name", "Last Name", "Qualification", "Email", "Phone", "Department" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        instructorTable = new JTable(tableModel);
        StyleUtil.styleTable(instructorTable);

        instructorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedInstructor();
            }
        });

        JScrollPane scrollPane = new JScrollPane(instructorTable);
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
        List<Instructor> instructors = controller.getAllInstructors();

        for (Instructor i : instructors) {
            tableModel.addRow(new Object[] {
                    i.getInstructorId(),
                    i.getFirstName(),
                    i.getLastName(),
                    i.getQualification(),
                    i.getEmail(),
                    i.getPhone(),
                    i.getDepartment()
            });
        }
        clearForm();
    }

    public void refreshData() {
        loadData();
    }

    private void loadSelectedInstructor() {
        int selectedRow = instructorTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedInstructorId = (int) tableModel.getValueAt(selectedRow, 0);
            firstNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            lastNameField.setText((String) tableModel.getValueAt(selectedRow, 2));
            qualificationField.setText((String) tableModel.getValueAt(selectedRow, 3));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 4));
            phoneField.setText((String) tableModel.getValueAt(selectedRow, 5));
            departmentField.setText((String) tableModel.getValueAt(selectedRow, 6));
        }
    }

    private void addInstructor() {
        Instructor instructor = getInstructorFromForm();
        if (instructor == null)
            return;

        if (controller.addInstructor(instructor)) {
            JOptionPane.showMessageDialog(this,
                    "Instructor added successfully!",
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

    private void updateInstructor() {
        if (selectedInstructorId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an instructor to update.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Instructor instructor = getInstructorFromForm();
        if (instructor == null)
            return;
        instructor.setInstructorId(selectedInstructorId);

        if (controller.updateInstructor(instructor)) {
            JOptionPane.showMessageDialog(this,
                    "Instructor updated successfully!",
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

    private void deleteInstructor() {
        if (selectedInstructorId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select an instructor to delete.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this instructor?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteInstructor(selectedInstructorId)) {
                JOptionPane.showMessageDialog(this,
                        "Instructor deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete instructor.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedInstructorId = -1;
        firstNameField.setText("");
        lastNameField.setText("");
        qualificationField.setText("");
        emailField.setText("");
        phoneField.setText("");
        departmentField.setText("");
        instructorTable.clearSelection();
    }

    private void searchInstructors() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Instructor> instructors = controller.searchInstructors(searchTerm);

        for (Instructor i : instructors) {
            tableModel.addRow(new Object[] {
                    i.getInstructorId(),
                    i.getFirstName(),
                    i.getLastName(),
                    i.getQualification(),
                    i.getEmail(),
                    i.getPhone(),
                    i.getDepartment()
            });
        }
    }

    private Instructor getInstructorFromForm() {
        Instructor instructor = new Instructor();
        instructor.setFirstName(firstNameField.getText().trim());
        instructor.setLastName(lastNameField.getText().trim());
        instructor.setQualification(qualificationField.getText().trim());
        instructor.setEmail(emailField.getText().trim());
        instructor.setPhone(phoneField.getText().trim());
        instructor.setDepartment(departmentField.getText().trim());
        return instructor;
    }
}
