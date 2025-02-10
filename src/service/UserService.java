package service;

import javafx.scene.control.Label;

public class UserService {
    public boolean registerUser(String name, String email, String password,
                                Label nameErrorLabel, Label emailErrorLabel, Label passwordErrorLabel) {
        boolean isValid = true;

        if (name == null || name.isEmpty()) {
            nameErrorLabel.setText("Name cannot be empty.");
            isValid = false;
        } else {
            nameErrorLabel.setText("");
        }

        if (email == null || email.isEmpty()) {
            emailErrorLabel.setText("Email cannot be empty.");
            isValid = false;
        } else if (!email.contains("@")) {
            emailErrorLabel.setText("Invalid email format.");
            isValid = false;
        } else {
            emailErrorLabel.setText("");
        }

        if (password == null || password.isEmpty()) {
            passwordErrorLabel.setText("Password cannot be empty.");
            isValid = false;
        } else if (password.length() < 6) {
            passwordErrorLabel.setText("Password must be at least 6 characters long.");
            isValid = false;
        } else {
            passwordErrorLabel.setText("");
        }

        if (!isValid) {
            return false;
        }

        System.out.println("UserService: User " + name + " (" + email + ") registered!");
        return true;
    }
}
