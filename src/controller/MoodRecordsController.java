package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import model.Enums.MoodState;
import model.MoodEntry;
import service.UserService;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.List;

public class MoodRecordsController {

    @FXML
    private ListView<String> recordsListView;

    @FXML
    private Label moodLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private TextArea notesTextArea;

    @FXML
    private LineChart<String, Number> moodChart;

    private UserService userService;
    private ObjectId userId;

    @FXML
    public void initialize() {
        userService = new UserService();

        recordsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int selectedIndex = recordsListView.getSelectionModel().getSelectedIndex();
                List<MoodEntry> entries = userService.getMoodEntries(userId);
                if (selectedIndex >= 0 && selectedIndex < entries.size()) {
                    MoodEntry entry = entries.get(selectedIndex);
                    showEntryDetails(entry);
                }
            }
        });
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
        loadUserMoodEntries();
    }

    private void loadUserMoodEntries() {
        List<MoodEntry> entries = userService.getMoodEntries(userId);
        for (MoodEntry entry : entries) {
            String formattedEntry = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(entry.getDate()) + " - " + entry.getMood().toString();
            recordsListView.getItems().add(formattedEntry);
        }
    }

    private void showEntryDetails(MoodEntry entry) {
        moodLabel.setText("Estado de Ánimo: " + entry.getMood().toString());
        dateLabel.setText("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(entry.getDate()));
        notesTextArea.setText(entry.getText());
    }
}