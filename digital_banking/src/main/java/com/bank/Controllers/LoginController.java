package com.bank.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button createButton;

    @FXML
    private Label errorLabel;  // To show error messages in the UI

    private Runnable onLoginSuccess;

    // Method to handle login button click
    @FXML
    private void handleLogin() {
        // Get the entered username and password
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate the input fields
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username or password is empty.");
            return;
        }

        // Check login credentials with the database
        if (checkLoginCredentials(username, password)) {
            // Login successful, notify the Main class (here we just print success for now)
            System.out.println("Login successful!");

            // Trigger the action for successful login
            if (onLoginSuccess != null) {
                onLoginSuccess.run();  // Trigger the transition to sidebar
            }
        } else {
            // Handle failed login
            errorLabel.setText("Invalid username or password.");
        }
    }

    // Method to check the username and password in the database
    private boolean checkLoginCredentials(String username, String password) {
        boolean isValid = false;

        // Database connection details (adjust as necessary)
        String dbUrl = "jdbc:mysql://localhost:3306/bank"; // Assuming 'bank' database
        String dbUsername = "root"; // Replace with your database username
        String dbPassword = ""; // Replace with your database password

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // SQL query to check if the username and password exist in the database
            String sql = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username); // Set the username parameter

                // Execute the query
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String storedPasswordHash = resultSet.getString("password");

                    // Compare entered password with the stored password (hashed comparison)
                    if (BCrypt.checkpw(password, storedPasswordHash)) {
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            errorLabel.setText("Error connecting to the database.");
            System.out.println("Error connecting to the database: " + e.getMessage());
        }

        return isValid;
    }

    // Method to handle create user button click (for registration)
    @FXML
    private void handleCreate() {
        // Load the register form (register.fxml)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bank/Views/register.fxml"));
            Scene registerScene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Register New User");
            stage.setScene(registerScene);
            stage.show();

            // Optionally, you can close the current login screen after opening the registration form
            Stage currentStage = (Stage) createButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            errorLabel.setText("Error loading registration form.");
            e.printStackTrace();
        }
    }

    // Method to set the onLoginSuccess action (from Main class)
    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
}
