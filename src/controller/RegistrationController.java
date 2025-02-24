package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import service.UserService;

import java.io.IOException;
import java.util.Date;

public class RegistrationController {

    @FXML
    private TextField nameField;

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
    private javafx.scene.control.Button registerButton;

    private UserService userService = new UserService();

    // Método para manejar el registro
    @FXML
    public void handleRegistration() {
        // Limpiar mensajes de error
        nameErrorLabel.setText("");
        emailErrorLabel.setText("");
        passwordErrorLabel.setText("");

        // Validar los campos
        if (nameField.getText().isEmpty()) {
            nameErrorLabel.setText("El nombre es obligatorio.");
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
                "Denco",
                new Date(),
                emailField.getText(),
                "https://fastly.picsum.photos/id/8/200/300.jpg?hmac=t2Camsbqc4OfjWMxFDwB32A8N4eu7Ido7ZV1elq4o5M", // Avatar (puedes añadir un campo para esto si es necesario)
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
                passwordErrorLabel
        );

        if (isRegistered) {
            openMoodPanel(newUser);
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