package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private UserService userService = new UserService();

    // Método para manejar el login
    @FXML
    public void handleLogin() {
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty()) {
            emailErrorLabel.setText("El email es obligatorio.");
            return;
        }
        if (password.isEmpty()) {
            passwordErrorLabel.setText("La contraseña es obligatoria.");
            return;
        }

        User user = userService.login(email, password);

        if (user != null) {
            loginButton.getScene().getWindow().hide();
            openMoodPanel(user);
        } else {
            passwordErrorLabel.setText("Email o contraseña incorrectos.");
        }
    }

    // Método para manejar el enlace de registro
    @FXML
    public void handleRegisterLink() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/registration.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 500);

            Stage registerStage = new Stage();
            registerStage.setTitle("Registro");
            registerStage.setScene(scene);

            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para abrir el panel de estado de ánimo
    private void openMoodPanel(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mood_form.fxml"));
            Parent root = loader.load();

            MoodController moodController = loader.getController();

            moodController.setUserId(user.getId());

            Scene scene = new Scene(root, 500, 400);

            Stage moodStage = new Stage();
            moodStage.setTitle("Mood Tracker");
            moodStage.setScene(scene);

            moodStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}