package com.university.view;

import com.university.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Dashboard Frame - Main navigation hub after login.
 * Contains sidebar navigation and main content area.
 */
public class DashboardFrame extends JFrame {

    private User currentUser;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private CardLayout cardLayout;

    // Panels for different sections
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private InstructorPanel instructorPanel;
    private EnrollmentPanel enrollmentPanel;

    public DashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("University Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        // Main layout
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Main content area
        JPanel mainArea = new JPanel(new BorderLayout());
        mainArea.setBackground(StyleUtil.BACKGROUND_COLOR);

        // Top bar
        JPanel topBar = createTopBar();
        mainArea.add(topBar, BorderLayout.NORTH);

        // Content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(StyleUtil.BACKGROUND_COLOR);

        // Add panels
        contentPanel.add(createHomePanel(), "HOME");

        studentPanel = new StudentPanel();
        contentPanel.add(studentPanel, "STUDENTS");

        coursePanel = new CoursePanel();
        contentPanel.add(coursePanel, "COURSES");

        instructorPanel = new InstructorPanel();
        contentPanel.add(instructorPanel, "INSTRUCTORS");

        enrollmentPanel = new EnrollmentPanel();
        contentPanel.add(enrollmentPanel, "ENROLLMENTS");

        mainArea.add(contentPanel, BorderLayout.CENTER);

        add(mainArea, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(StyleUtil.SECONDARY_COLOR);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // Logo/Brand section
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        brandPanel.setBackground(StyleUtil.SECONDARY_COLOR);
        brandPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel brandIcon = new JLabel("🎓");
        brandIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        brandPanel.add(brandIcon);

        JLabel brandName = new JLabel("UMS");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        brandName.setForeground(Color.WHITE);
        brandPanel.add(brandName);

        sidebar.add(brandPanel);

        // Separator
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setForeground(new Color(255, 255, 255, 50));
        sidebar.add(sep);

        sidebar.add(Box.createVerticalStrut(20));

        // Menu buttons
        sidebar.add(createMenuButton("🏠  Home", e -> showPanel("HOME")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("👨‍🎓  Students", e -> showPanel("STUDENTS")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("📚  Courses", e -> showPanel("COURSES")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("👨‍🏫  Instructors", e -> showPanel("INSTRUCTORS")));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createMenuButton("📝  Enrollments", e -> showPanel("ENROLLMENTS")));

        sidebar.add(Box.createVerticalGlue());

        // Logout button at bottom
        JButton logoutBtn = createMenuButton("🚪  Logout", e -> logout());
        logoutBtn.setBackground(StyleUtil.DANGER_COLOR);
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(20));

        return sidebar;
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(StyleUtil.BODY_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(StyleUtil.SECONDARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(220, 45));
        button.setPreferredSize(new Dimension(220, 45));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(StyleUtil.PRIMARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getText().contains("Logout")) {
                    button.setBackground(StyleUtil.SECONDARY_COLOR);
                } else {
                    button.setBackground(StyleUtil.DANGER_COLOR);
                }
            }
        });

        button.addActionListener(listener);
        return button;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setPreferredSize(new Dimension(0, 60));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(189, 195, 199)));

        // Left side - Page title
        welcomeLabel = new JLabel("  Dashboard");
        welcomeLabel.setFont(StyleUtil.SUBTITLE_FONT);
        welcomeLabel.setForeground(StyleUtil.TEXT_COLOR);
        topBar.add(welcomeLabel, BorderLayout.WEST);

        // Right side - User info and time
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        rightPanel.setBackground(Color.WHITE);

        // Date Time
        JLabel dateLabel = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(StyleUtil.SMALL_FONT);
        dateLabel.setForeground(StyleUtil.TEXT_LIGHT);
        rightPanel.add(dateLabel);

        // User info
        JLabel userLabel = new JLabel("👤 " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(StyleUtil.BODY_FONT);
        userLabel.setForeground(StyleUtil.TEXT_COLOR);
        rightPanel.add(userLabel);

        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(StyleUtil.BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        // Welcome card
        JPanel welcomeCard = StyleUtil.createCardPanel();
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setPreferredSize(new Dimension(600, 150));

        JLabel welcomeTitle = new JLabel("Welcome to University Management System!");
        welcomeTitle.setFont(StyleUtil.TITLE_FONT);
        welcomeTitle.setForeground(StyleUtil.TEXT_COLOR);
        welcomeCard.add(welcomeTitle, BorderLayout.NORTH);

        JLabel welcomeDesc = new JLabel("<html>Use the sidebar navigation to manage:<br/>" +
                "• Students - Add, edit, or remove student records<br/>" +
                "• Courses - Manage course offerings<br/>" +
                "• Instructors - Handle instructor information<br/>" +
                "• Enrollments - Enroll students in courses</html>");
        welcomeDesc.setFont(StyleUtil.BODY_FONT);
        welcomeDesc.setForeground(StyleUtil.TEXT_COLOR);
        welcomeDesc.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        welcomeCard.add(welcomeDesc, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        homePanel.add(welcomeCard, gbc);

        // Stats cards
        gbc.gridwidth = 1;
        gbc.gridy = 1;

        homePanel.add(createStatCard("👨‍🎓", "Students", "Manage student records"), gbc);
        gbc.gridx = 1;
        homePanel.add(createStatCard("📚", "Courses", "View and edit courses"), gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        homePanel.add(createStatCard("👨‍🏫", "Instructors", "Instructor management"), gbc);
        gbc.gridx = 1;
        homePanel.add(createStatCard("📝", "Enrollments", "Student enrollments"), gbc);

        return homePanel;
    }

    private JPanel createStatCard(String icon, String title, String description) {
        JPanel card = StyleUtil.createCardPanel();
        card.setLayout(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(280, 100));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        card.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(StyleUtil.HEADER_FONT);
        titleLabel.setForeground(StyleUtil.TEXT_COLOR);
        textPanel.add(titleLabel);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(StyleUtil.SMALL_FONT);
        descLabel.setForeground(StyleUtil.TEXT_LIGHT);
        textPanel.add(descLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);

        switch (name) {
            case "HOME":
                welcomeLabel.setText("  Dashboard");
                break;
            case "STUDENTS":
                welcomeLabel.setText("  Student Management");
                studentPanel.refreshData();
                break;
            case "COURSES":
                welcomeLabel.setText("  Course Management");
                coursePanel.refreshData();
                break;
            case "INSTRUCTORS":
                welcomeLabel.setText("  Instructor Management");
                instructorPanel.refreshData();
                break;
            case "ENROLLMENTS":
                welcomeLabel.setText("  Enrollment Management");
                enrollmentPanel.refreshData();
                break;
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            this.dispose();
        }
    }
}
