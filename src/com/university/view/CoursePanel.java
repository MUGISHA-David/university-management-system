package com.university.view;

import com.university.controller.CourseController;
import com.university.model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Course Panel - CRUD operations for Courses.
 */
public class CoursePanel extends JPanel {

    private CourseController controller;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    private JTextField courseCodeField;
    private JTextField courseNameField;
    private JSpinner creditsSpinner;
    private JTextField departmentField;
    private JTextArea descriptionArea;
    private JSpinner capacitySpinner;

    private JTextField searchField;
    private int selectedCourseId = -1;

    public CoursePanel() {
        controller = new CourseController();
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

        JLabel formTitle = new JLabel("Course Form");
        formTitle.setFont(StyleUtil.SUBTITLE_FONT);
        formTitle.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 5, 15, 5);
        formCard.add(formTitle, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Course Code
        gbc.gridy = 1;
        formCard.add(createLabel("Course Code *"), gbc);
        gbc.gridx = 1;
        courseCodeField = new JTextField(15);
        StyleUtil.styleTextField(courseCodeField);
        formCard.add(courseCodeField, gbc);

        // Course Name
        gbc.gridy = 2;
        gbc.gridx = 0;
        formCard.add(createLabel("Course Name *"), gbc);
        gbc.gridx = 1;
        courseNameField = new JTextField(15);
        StyleUtil.styleTextField(courseNameField);
        formCard.add(courseNameField, gbc);

        // Credits
        gbc.gridy = 3;
        gbc.gridx = 0;
        formCard.add(createLabel("Credits (1-6) *"), gbc);
        gbc.gridx = 1;
        creditsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
        creditsSpinner.setFont(StyleUtil.BODY_FONT);
        creditsSpinner.setPreferredSize(new Dimension(250, 35));
        formCard.add(creditsSpinner, gbc);

        // Department
        gbc.gridy = 4;
        gbc.gridx = 0;
        formCard.add(createLabel("Department *"), gbc);
        gbc.gridx = 1;
        departmentField = new JTextField(15);
        StyleUtil.styleTextField(departmentField);
        formCard.add(departmentField, gbc);

        // Description
        gbc.gridy = 5;
        gbc.gridx = 0;
        formCard.add(createLabel("Description"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 15);
        descriptionArea.setFont(StyleUtil.BODY_FONT);
        descriptionArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descScroll.setPreferredSize(new Dimension(250, 60));
        formCard.add(descScroll, gbc);

        // Max Capacity
        gbc.gridy = 6;
        gbc.gridx = 0;
        formCard.add(createLabel("Max Capacity"), gbc);
        gbc.gridx = 1;
        capacitySpinner = new JSpinner(new SpinnerNumberModel(50, 1, 500, 10));
        capacitySpinner.setFont(StyleUtil.BODY_FONT);
        capacitySpinner.setPreferredSize(new Dimension(250, 35));
        formCard.add(capacitySpinner, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add");
        StyleUtil.styleSuccessButton(addBtn);
        addBtn.addActionListener(e -> addCourse());
        buttonPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        StyleUtil.stylePrimaryButton(updateBtn);
        updateBtn.addActionListener(e -> updateCourse());
        buttonPanel.add(updateBtn);

        JButton deleteBtn = new JButton("Delete");
        StyleUtil.styleDangerButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteCourse());
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
        searchBtn.addActionListener(e -> searchCourses());
        searchPanel.add(searchBtn);

        JButton refreshBtn = new JButton("Refresh");
        StyleUtil.styleSecondaryButton(refreshBtn);
        refreshBtn.setPreferredSize(new Dimension(100, 35));
        refreshBtn.addActionListener(e -> loadData());
        searchPanel.add(refreshBtn);

        tableCard.add(searchPanel, BorderLayout.NORTH);

        String[] columns = { "ID", "Code", "Name", "Credits", "Department", "Capacity" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        StyleUtil.styleTable(courseTable);

        courseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCourse();
            }
        });

        JScrollPane scrollPane = new JScrollPane(courseTable);
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
        List<Course> courses = controller.getAllCourses();

        for (Course c : courses) {
            tableModel.addRow(new Object[] {
                    c.getCourseId(),
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getCredits(),
                    c.getDepartment(),
                    c.getMaxCapacity()
            });
        }
        clearForm();
    }

    public void refreshData() {
        loadData();
    }

    private void loadSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            selectedCourseId = (int) tableModel.getValueAt(selectedRow, 0);
            courseCodeField.setText((String) tableModel.getValueAt(selectedRow, 1));
            courseNameField.setText((String) tableModel.getValueAt(selectedRow, 2));
            creditsSpinner.setValue(tableModel.getValueAt(selectedRow, 3));
            departmentField.setText((String) tableModel.getValueAt(selectedRow, 4));
            capacitySpinner.setValue(tableModel.getValueAt(selectedRow, 5));

            Course course = controller.getCourseById(selectedCourseId);
            if (course != null) {
                descriptionArea.setText(course.getDescription());
            }
        }
    }

    private void addCourse() {
        Course course = getCourseFromForm();
        if (course == null)
            return;

        if (controller.addCourse(course)) {
            JOptionPane.showMessageDialog(this,
                    "Course added successfully!",
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

    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to update.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Course course = getCourseFromForm();
        if (course == null)
            return;
        course.setCourseId(selectedCourseId);

        if (controller.updateCourse(course)) {
            JOptionPane.showMessageDialog(this,
                    "Course updated successfully!",
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

    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a course to delete.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this course?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteCourse(selectedCourseId)) {
                JOptionPane.showMessageDialog(this,
                        "Course deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Failed to delete course.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedCourseId = -1;
        courseCodeField.setText("");
        courseNameField.setText("");
        creditsSpinner.setValue(3);
        departmentField.setText("");
        descriptionArea.setText("");
        capacitySpinner.setValue(50);
        courseTable.clearSelection();
    }

    private void searchCourses() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadData();
            return;
        }

        tableModel.setRowCount(0);
        List<Course> courses = controller.searchCourses(searchTerm);

        for (Course c : courses) {
            tableModel.addRow(new Object[] {
                    c.getCourseId(),
                    c.getCourseCode(),
                    c.getCourseName(),
                    c.getCredits(),
                    c.getDepartment(),
                    c.getMaxCapacity()
            });
        }
    }

    private Course getCourseFromForm() {
        Course course = new Course();
        course.setCourseCode(courseCodeField.getText().trim());
        course.setCourseName(courseNameField.getText().trim());
        course.setCredits((int) creditsSpinner.getValue());
        course.setDepartment(departmentField.getText().trim());
        course.setDescription(descriptionArea.getText().trim());
        course.setMaxCapacity((int) capacitySpinner.getValue());
        return course;
    }
}
