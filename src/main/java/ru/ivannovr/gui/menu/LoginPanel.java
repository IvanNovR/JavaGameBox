package ru.ivannovr.gui.menu;

import ru.ivannovr.gui.BasePanel;
import ru.ivannovr.utils.DatabaseManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends BasePanel {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(JFrame parentFrame, DatabaseManager dbManager) {
        super(parentFrame, dbManager);
        initializeComponents();
    }

    @Override
    protected void initializeComponents() {
        JLabel titleLabel = new JLabel("JavaGameBox", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(0, 50, 600, 50);
        add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(150, 170, 100, 30);
        add(usernameLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(150, 200, 300, 40);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        usernameField.setBackground(new Color(50, 50, 80));
        usernameField.setForeground(Color.WHITE);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 1),
                new EmptyBorder(5, 10, 5, 10)));
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(150, 250, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(150, 280, 300, 40);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBackground(new Color(50, 50, 80));
        passwordField.setForeground(Color.WHITE);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 1),
                new EmptyBorder(5, 10, 5, 10)));
        add(passwordField);

        JButton loginButton = createStyledButton("Login", new Color(80, 200, 120), e -> login());
        loginButton.setBounds(150, 350, 140, 40);
        add(loginButton);

        JButton registerButton = createStyledButton("Register", new Color(100, 150, 255), e -> register());
        registerButton.setBounds(310, 350, 140, 40);
        add(registerButton);
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!");
            return;
        }
        if (dbManager.authenticateUser(username, password)) {
            switchPanel(new MainPanel(parentFrame, username, dbManager));
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password!");
            return;
        }
        if (dbManager.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now log in.");
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!");
        }
    }
}