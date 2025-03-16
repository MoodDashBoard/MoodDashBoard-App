package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import service.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Button loginButton;

    private final UserService userService = new UserService();

    @FXML
    public void handleLogin() {
        clearErrorLabels();

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showErrorLabels(email, password);
            return;
        }

        User user = userService.login(email, password);

        if (user != null) {
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            openDashboard(user);
        } else {
            passwordErrorLabel.setText("Email o contraseña incorrectos.");
        }
    }

    @FXML
    public void handleRegisterLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 600, 700);
            Stage registerStage = new Stage();
            registerStage.setTitle("Registro");
            registerStage.setScene(scene);
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            Image icon = new Image(getClass().getResourceAsStream("/view/icons/mood.png"));
            registerStage.getIcons().add(icon);
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController dashboardController = loader.getController();
            dashboardController.setUserId(user.getId());
            Scene scene = new Scene(root, 900, 800);
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Panel de Control");
            dashboardStage.setScene(scene);
            dashboardController.loadMoodForm();
            dashboardStage.show();
            Image icon = new Image(getClass().getResourceAsStream("/view/icons/mood.png"));
            dashboardStage.getIcons().add(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearErrorLabels() {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    private void showErrorLabels(String email, String password) {
        if (email.isEmpty()) {
            emailErrorLabel.setText("El email es obligatorio.");
            emailErrorLabel.setAccessibleText("Error: El campo de correo electrónico está vacío.");
        }
        if (password.isEmpty()) {
            passwordErrorLabel.setText("La contraseña es obligatoria.");
            passwordErrorLabel.setAccessibleText("Error: El campo de contraseña está vacío.");
        }
    }
}