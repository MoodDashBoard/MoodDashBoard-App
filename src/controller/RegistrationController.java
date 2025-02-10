package controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.UserService;

public class RegistrationController {
    private final UserService userService;

    public RegistrationController() {
        this.userService = new UserService();
    }

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
    private void handleRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (userService.registerUser(name, email, password, nameErrorLabel, emailErrorLabel, passwordErrorLabel)) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("There were errors during registration.");
        }
    }
}
