package com.university.view;

import com.university.controller.UserController;
import com.university.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Login Frame - Entry point of the application.
 * Modern styled login screen.
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private UserController userController;

    public LoginFrame() {
        userController = new UserController();
        initComponents();
    }

    private void initComponents() {
        setTitle("University Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main container with split layout
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Left panel - Branding
        JPanel leftPanel = StyleUtil.createGradientPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel brandLabel = new JLabel("🎓");
        brandLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        gbc.gridy = 0;
        leftPanel.add(brandLabel, gbc);

        JLabel titleLabel = new JLabel("University Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        leftPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        gbc.gridy = 2;
        leftPanel.add(subtitleLabel, gbc);

        JLabel descLabel = new JLabel(
                "<html><center>Manage students, courses, instructors<br/>and enrollments efficiently.</center></html>");
        descLabel.setFont(StyleUtil.BODY_FONT);
        descLabel.setForeground(new Color(255, 255, 255, 180));
        gbc.gridy = 3;
        gbc.insets = new Insets(30, 10, 10, 10);
        leftPanel.add(descLabel, gbc);

        // Right panel - Login Form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Welcome Back!");
        loginTitle.setFont(StyleUtil.TITLE_FONT);
        loginTitle.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginTitle, gbc);

        JLabel loginSubtitle = new JLabel("Please sign in to continue");
        loginSubtitle.setFont(StyleUtil.BODY_FONT);
        loginSubtitle.setForeground(StyleUtil.TEXT_LIGHT);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 30, 10);
        rightPanel.add(loginSubtitle, gbc);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(StyleUtil.HEADER_FONT);
        userLabel.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 5, 10);
        rightPanel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        StyleUtil.styleTextField(usernameField);
        usernameField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 10, 15, 10);
        rightPanel.add(usernameField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(StyleUtil.HEADER_FONT);
        passLabel.setForeground(StyleUtil.TEXT_COLOR);
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 10, 5, 10);
        rightPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        StyleUtil.stylePasswordField(passwordField);
        passwordField.setPreferredSize(new Dimension(300, 40));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 10, 20, 10);
        rightPanel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Sign In");
        StyleUtil.stylePrimaryButton(loginButton);
        loginButton.setPreferredSize(new Dimension(300, 45));
        gbc.gridy = 6;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(loginButton, gbc);

        // Default credentials hint
        JLabel hintLabel = new JLabel("<html><center>Default: admin / admin123</center></html>");
        hintLabel.setFont(StyleUtil.SMALL_FONT);
        hintLabel.setForeground(StyleUtil.TEXT_LIGHT);
        gbc.gridy = 7;
        gbc.insets = new Insets(15, 10, 10, 10);
        rightPanel.add(hintLabel, gbc);

        // Add panels to main
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(mainPanel);

        // Action listeners
        loginButton.addActionListener(this::performLogin);

        // Enter key to login
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin(null);
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    private void performLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = userController.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "Login successful! Welcome, " + user.getUsername(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Open Dashboard
            new DashboardFrame(user).setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    userController.getErrorMessage(),
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
