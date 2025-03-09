package com.bank.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import com.bank.DB;  // Assuming you have a DB class for database connection
import javafx.stage.Window;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.io.Serializable;
import java.sql.*;

public class RegisterController {

    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button okButton;

    // Method to handle OK button click (for registration)
    @FXML
    private void handleCreate() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate the input fields
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username or password is empty.");
            return;
        }

        // Hash the password before storing it
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Database connection details (adjust as necessary)
        String dbUrl = "jdbc:mysql://localhost:3306/bank"; // Assuming 'bank' database
        String dbUsername = "root"; // Replace with your database username
        String dbPassword = ""; // Replace with your database password

        // Insert the new user into the database
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, hashedPassword);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User created successfully!");
                } else {
                    System.out.println("Failed to create user.");
                }
            }

            // After user is created, return to login screen
            returnToLogin();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    // Method to return to the login screen
    private void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bank/Views/login.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(loginScene);
            stage.show();

            // Close the registration window
            Stage currentStage = (Stage) okButton.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            System.out.println("Error loading login screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bank/views/login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
