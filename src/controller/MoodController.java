package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import model.MoodEntry;
import model.Enums.MoodState;
import org.bson.types.ObjectId;
import service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class MoodController {

    @FXML
    private TextArea notesField;

    @FXML
    private ComboBox<MoodState> moodComboBox;

    @FXML
    private StackPane root;

    private UserService userService;
    private ObjectId userId;

    @FXML
    public void initialize() {
        moodComboBox.getItems().setAll(MoodState.values());

        moodComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Color color = newValue.getColor();
                root.setStyle("-fx-background-color: #" + color.toString().substring(2, 8) + "; -fx-border-radius: 15px; -fx-background-radius: 15px;");
            }
        });

        userService = new UserService();
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

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
            // Obtener una recomendación aleatoria para el estado de ánimo
            String recommendation = getRandomRecommendation(moodState);
            showAlert("Éxito", "Entrada guardada correctamente.\n\nRecomendación: " + recommendation);
            clearForm();
        } else {
            showAlert("Error", "No se pudo guardar la entrada.");
        }
    }

    private String getRandomRecommendation(MoodState moodState) {
        String moodStateText = moodState.toString().toUpperCase();

        List<String> recommendations = userService.getRecommendationsByMoodState(moodStateText);

        if (recommendations != null && !recommendations.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(recommendations.size());
            return recommendations.get(index);
        } else {
            return "No hay recomendaciones disponibles para este estado de ánimo.";
        }
    }

    private void clearForm() {
        notesField.clear();
        moodComboBox.getSelectionModel().clearSelection();
        moodComboBox.getSelectionModel().selectFirst();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/view/css/mood_form.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();
    }
}