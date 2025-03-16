package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import model.MoodEntry;
import service.UserService;
import org.bson.types.ObjectId;

import java.text.SimpleDateFormat;
import java.util.List;

public class MoodChartController {

    @FXML
    private LineChart<String, Number> moodChart;

    private UserService userService;
    private ObjectId userId;

    @FXML
    public void initialize() {
        userService = new UserService();
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
        loadMoodChart();
    }

    private void loadMoodChart() {
        List<MoodEntry> entries = userService.getMoodEntries(userId);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Estado de Ánimo");

        for (MoodEntry entry : entries) {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(entry.getDate());
            int moodValue = entry.getMood().ordinal();
            series.getData().add(new XYChart.Data<>(date, moodValue));
        }

        moodChart.getData().clear();
        moodChart.getData().add(series);
    }
}