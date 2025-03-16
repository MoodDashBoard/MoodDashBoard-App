package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.User;
import service.UserService;

import java.io.IOException;
import java.util.Date;

public class RegistrationController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField avatarField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label emailErrorLabel;

    @FXML
    private Label passwordErrorLabel;

    @FXML
    private Label avatarErrorLabel;

    @FXML
    private Label surnameErrorLabel;

    @FXML
    private javafx.scene.control.Button registerButton;

    private UserService userService = new UserService();

    @FXML
    public void handleRegistration() {
        nameErrorLabel.setText("");
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");
        surnameErrorLabel.setText("");

        if (nameField.getText().isEmpty()) {
            nameErrorLabel.setText("El nombre es obligatorio.");
            return;
        }
        if (surnameField.getText().isEmpty()) {
            surnameErrorLabel.setText("El apellido es obligatorio.");
            return;
        }
        if (emailField.getText().isEmpty() || !emailField.getText().contains("@")) {
            emailErrorLabel.setText("El email es inválido.");
            return;
        }
        if (passwordField.getText().isEmpty() || passwordField.getText().length() < 6) {
            passwordErrorLabel.setText("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        User newUser = new User(
                nameField.getText(),
                surnameField.getText(),
                new Date(),
                emailField.getText(),
                "Default",
                passwordField.getText()
        );

        boolean isRegistered = userService.registerUser(
                newUser.getName(),
                newUser.getSurname(),
                newUser.getBirthdate(),
                newUser.getEmail(),
                newUser.getAvatar(),
                newUser.getPassword(),
                nameErrorLabel,
                emailErrorLabel,
                passwordErrorLabel,
                surnameErrorLabel
        );

        if (isRegistered) {
            openMoodPanel(newUser);
            Stage currentStage = (Stage) registerButton.getScene().getWindow();
            currentStage.close();
        }
    }

    private void openMoodPanel(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController dashboardController = loader.getController();
            dashboardController.setUserId(user.getId());
            Scene scene = new Scene(root, 900, 800);
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Panel de Control");
            dashboardStage.setScene(scene);
            Image icon = new Image(getClass().getResourceAsStream("/view/icons/mood.png"));
            dashboardStage.getIcons().add(icon);
            dashboardController.loadMoodForm();
            dashboardStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}