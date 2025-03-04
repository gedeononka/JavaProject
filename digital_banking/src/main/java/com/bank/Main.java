package com.bank;

import com.bank.Controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // First load and display the login screen
        FXMLLoader loginLoader = new FXMLLoader(Main.class.getResource("Views/login.fxml"));
        Parent loginRoot = loginLoader.load();
        Scene loginScene = new Scene(loginRoot);

        // Set the login scene
        stage.setScene(loginScene);
        stage.setTitle("Login - Gedeon Banking System");
        stage.show();

        // Set the action for login success
        LoginController loginController = loginLoader.getController();
        loginController.setOnLoginSuccess(() -> {
            try {
                // Load sidebar screen after successful login
                FXMLLoader sidebarLoader = new FXMLLoader(Main.class.getResource("Views/sidebar.fxml"));
                Parent sidebarRoot = sidebarLoader.load();
                Scene sidebarScene = new Scene(sidebarRoot);
                stage.setScene(sidebarScene); // Switch to sidebar scene
                stage.setTitle("Gedeon Banking System");
            } catch (IOException e) {
                e.printStackTrace(); // Handle loading error
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
