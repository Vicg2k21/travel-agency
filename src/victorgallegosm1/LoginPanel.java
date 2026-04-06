/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author 12546
 */
public class LoginPanel extends JPanel {
    private final TravelAgency agency;

    private CustomTextfield usernameField, regUsernameField;
    private JPasswordField passwordField, regPasswordField;

    private CustomButton loginButton, registerButton, guestButton, exitButton;
    private CustomLabel passwordStrengthLabel, lengthLabel, specialCharLabel, upperCaseLabel, digitLabel, statusLabel;

    private JPanel registerPanel;
    private boolean isRegistrationPanel = false;

    public LoginPanel(TravelAgency agency) {
        this.agency = agency;

        // Use BoxLayout for the entire panel to stack components vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(173, 216, 230));

        add(createTitlePanel());
        add(createCredentialsPanel());
        add(createButtonPanel());

        setName("LoginPanel");

        // Initialize the status label
        initializeStatusLabel();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 165, 0)); 
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        CustomLabel titleLabel = new CustomLabel("Welcome to Travel Agency");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(Color.WHITE);

        String subtitleText = isRegistrationPanel ? "Entering Register State" : "Entering Login State";
        CustomLabel subtitleLabel = new CustomLabel(subtitleText);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.WHITE);

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getPreferredSize().height));

        return titlePanel;
    }

    private JPanel createCredentialsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(getBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        panel.add(createLabel("Please enter your login details below:"));
        panel.add(Box.createVerticalStrut(10));

        panel.add(createLabel("Username:"));
        usernameField = createTextField(15);
        panel.add(usernameField);

        panel.add(createLabel("Password:"));
        passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(passwordField);

        return panel;
    }

    private CustomLabel createLabel(String text) {
        CustomLabel label = new CustomLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private CustomTextfield createTextField(int columns) {
        CustomTextfield textField = new CustomTextfield(columns);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(getBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        loginButton = createButton("Login");
        registerButton = createButton("Register");
        guestButton = createButton("Continue as Guest");
        exitButton = createButton("Exit");

        addLabeledButton(panel, "Click to login with your credentials:", loginButton);
        addLabeledButton(panel, "Click to create a new account:", registerButton);
        addLabeledButton(panel, "Continue without logging in:", guestButton);
        addLabeledButton(panel, "Close the application:", exitButton);

        // Action listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> switchToRegisterPanel());
        
        guestButton.addActionListener(e -> {
            agency.setCurrentCustomer(null); // indicate guest user
            agency.setState(new CustomerState(agency));
        });
       
        exitButton.addActionListener(e -> System.exit(0));

        return panel;
    }

    private CustomButton createButton(String text) {
        CustomButton button = new CustomButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    private void addLabeledButton(JPanel panel, String labelText, JButton button) {
        CustomLabel label = createLabel(labelText);

        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(button);
    }

    private void switchToRegisterPanel() {
        isRegistrationPanel = true;
        removeAll();

        add(createTitlePanel());

        registerPanel = createRegisterPanel();
        add(registerPanel);

        revalidate();
        repaint();
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));
        panel.setBackground(getBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 10, 50));

        regUsernameField = createTextField(15);
        regPasswordField = new JPasswordField(15);
        regPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));

        panel.add(createLabel("Username:"));
        panel.add(regUsernameField);
        panel.add(createLabel("Password:"));
        panel.add(regPasswordField);

        addPasswordRequirements(panel);

        CustomButton submitButton = createButton("Register");
        CustomButton backButton = new CustomButton("Back to Login");

        panel.add(submitButton);
        panel.add(backButton);

        submitButton.addActionListener(e -> handleRegistration());
        backButton.addActionListener(e -> agency.setState(new Login(agency)));

        regPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateButtonStates();
                updatePasswordStrength();
            }
        });

        return panel;
    }

    private void addPasswordRequirements(JPanel panel) {
        lengthLabel = createLabel("Length (8+ chars): ❌");
        specialCharLabel = createLabel("Special Character: ❌");
        upperCaseLabel = createLabel("Uppercase Letter: ❌");
        digitLabel = createLabel("Digit: ❌");
        passwordStrengthLabel = createLabel("Password Strength: Weak");

        panel.add(lengthLabel);
        panel.add(specialCharLabel);
        panel.add(upperCaseLabel);
        panel.add(digitLabel);
        panel.add(passwordStrengthLabel);
    }

    private void updatePasswordStrength() {
        String password = new String(regPasswordField.getPassword());

        lengthLabel.setText("Length (8+ chars): " + (password.length() >= 8 ? "✔️" : "❌"));
        specialCharLabel.setText("Special Character: " + (password.matches(".*\\W.*") ? "✔️" : "❌"));
        upperCaseLabel.setText("Uppercase Letter: " + (password.matches(".*[A-Z].*") ? "✔️" : "❌"));
        digitLabel.setText("Digit: " + (password.matches(".*\\d.*") ? "✔️" : "❌"));
        passwordStrengthLabel.setText("Password Strength: " + (isValidPassword(password) ? "Strong" : "Weak"));
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*\\W.*") &&
               password.matches(".*\\d.*");
    }

    private void handleRegistration() {
        String username = regUsernameField.getText();
        String password = new String(regPasswordField.getPassword());

        if (isValidPassword(password)) {
            Customer newCustomer = new Customer(username, "555-0000", "N/A", "C" + (agency.getCustomers().size() + 1), username, password, 0.0);
            agency.getCustomersFile().addCustomer(newCustomer);
            statusLabel.setText("Registration successful!");
            statusLabel.setForeground(new Color(0, 128, 0)); // Green for success
            regUsernameField.setText("");
            regPasswordField.setText("");
        } else {
            statusLabel.setText("Password does not meet requirements.");
            statusLabel.setForeground(new Color(255, 0, 0)); // Red for error
        }
    }
    
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check if the user is a travel agency employee
        for (TravelAgencyEmployee e : agency.getEmployees()) {
            if (e.getName().equals(username) && e.getPassword().equals(password)) {
                agency.setCurrentCustomer(null); // Not a customer, clear any previous customer
                agency.setState(e.isManager() ? new ManagerState(agency) : new EmployeeState(agency));
                return;
            }
        }

        // Check if the user is a registered customer
        for (Customer c : agency.getCustomers()) {
            if (c.getLogInName().equals(username) && c.getPassword().equals(password)) {
                agency.setCurrentCustomer(c); // Set the logged-in customer
                agency.setState(new CustomerState(agency)); // Switch to customer state
                return;
            }
        }

        // If login failed
        statusLabel.setText("Invalid credentials. Please try again.");
        statusLabel.setForeground(new Color(255, 0, 0)); // Red for error
    }

    // Initialize the status label to display status messages
    private void initializeStatusLabel() {
        statusLabel = createLabel("");
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setForeground(Color.RED); // red for error messages
        add(statusLabel);
    }

    // Enable or disable buttons based on input validation
    private void updateButtonStates() {
        loginButton.setEnabled(!usernameField.getText().isEmpty() && passwordField.getPassword().length > 0);
        registerButton.setEnabled(!regUsernameField.getText().isEmpty() && regPasswordField.getPassword().length > 0);
    }
}