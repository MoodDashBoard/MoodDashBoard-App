package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.MoodEntry;
import model.Enums.MoodState;
import org.bson.types.ObjectId;
import service.UserService;

import java.util.Date;

public class MoodController {

    @FXML
    private TextArea notesField;

    @FXML
    private ComboBox<MoodState> moodComboBox;

    private UserService userService;
    private ObjectId userId;

    // Método de inicialización
    @FXML
    public void initialize() {
        moodComboBox.getItems().setAll(MoodState.values());
        userService = new UserService();
    }

    // Método para establecer el ID del usuario actual
    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    // Método para manejar el envío del formulario
    @FXML
    public void handleMoodSubmission() {
        MoodState moodState = moodComboBox.getValue();
        String notes = notesField.getText();

        if (moodState == null || notes.isEmpty()) {
            showAlert("Error", "Por favor, completa todos los campos.");
            return;
        }

        MoodEntry entry = new MoodEntry(moodState, new Date(), notes);

        boolean isSaved = userService.addMoodEntry(userId, entry);

        if (isSaved) {
            showAlert("Éxito", "Entrada guardada correctamente.");
            clearForm();
        } else {
            showAlert("Error", "No se pudo guardar la entrada.");
        }
    }

    // Método para limpiar el formulario después de guardar
    private void clearForm() {
        notesField.clear();
        moodComboBox.getSelectionModel().clearSelection();
    }

    // Método para mostrar alertas
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}