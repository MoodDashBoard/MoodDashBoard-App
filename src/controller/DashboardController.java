package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.MoodEntry;
import model.Enums.MoodState;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import service.UserService;
import org.bson.types.ObjectId;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDFont;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.*;

public class DashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private HBox topBar;

    private ObjectId userId;

    private Button lastSelectedButton;

    private UserService userService = new UserService();

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    private void handleButtonSelection(Button selectedButton) {
        if (lastSelectedButton != null) {
            lastSelectedButton.getStyleClass().remove("selected");
        }
        selectedButton.getStyleClass().add("selected");
        lastSelectedButton = selectedButton;
    }

    @FXML
    private void showAverageMood() {
        List<MoodEntry> entries = userService.getMoodEntries(userId);
        double average = calculateAverageMood(entries);
        String description = getMoodDescription(average);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Media de Estados de Ánimo");
        alert.setHeaderText(String.format("Media: %.1f", average));
        alert.setContentText(description);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/view/css/dashboard.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-alert");

        alert.showAndWait();

        handleButtonSelection((Button) topBar.lookup("#averageMoodButton"));
    }

    private double calculateAverageMood(List<MoodEntry> entries) {
        if (entries.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (MoodEntry entry : entries) {
            sum += entry.getMood().getValue();
        }

        return (double) sum / entries.size();
    }

    private String getMoodDescription(double average) {
        int roundedAverage = (int) Math.round(average);
        MoodState moodState = MoodState.values()[roundedAverage - 1];

        switch (moodState) {
            case DEPRIMIDO:
                return "Tu estado de ánimo promedio ha sido Deprimido. ¡Esperamos que te sientas mejor pronto!";
            case TRISTE:
                return "Tu estado de ánimo promedio ha sido Triste. ¡Anímate, hay días mejores por venir!";
            case NEUTRAL:
                return "Tu estado de ánimo promedio ha sido Neutral. ¡Mantén el equilibrio!";
            case CONTENTO:
                return "Tu estado de ánimo promedio ha sido Contento. ¡Sigue así!";
            case EUFORICO:
                return "Tu estado de ánimo promedio ha sido Euforico. ¡Fantástico!";
            default:
                return "No hay suficientes datos para calcular tu estado de ánimo promedio.";
        }
    }

    @FXML
    public void loadMoodForm() {
        loadForm("/view/mood_form.fxml");
        handleButtonSelection((Button) topBar.lookup("#moodFormButton"));
    }

    @FXML
    public void loadMoodRecordsForm() {
        loadForm("/view/mood_records.fxml");
        handleButtonSelection((Button) topBar.lookup("#moodRecordsButton"));
    }

    @FXML
    public void loadMoodChart() {
        loadForm("/view/mood_chart.fxml");
        handleButtonSelection((Button) topBar.lookup("#moodChartButton"));
    }

    public void loadForm(String fxmlPath) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent form = loader.load();

            if (loader.getController() instanceof MoodController) {
                ((MoodController) loader.getController()).setUserId(userId);
            } else if (loader.getController() instanceof MoodRecordsController) {
                ((MoodRecordsController) loader.getController()).setUserId(userId);
            } else if (loader.getController() instanceof MoodChartController) {
                ((MoodChartController) loader.getController()).setUserId(userId);
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void generatePdf() {
        List<MoodEntry> historial = userService.getMoodEntries(userId);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                float marginLeft = 50;
                float marginRight = 50;
                float marginTop = 700;
                float lineHeight = 15;
                float yPosition = marginTop;

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                PDFont font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
                PDFont fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
                contentStream.setFont(font, 12);

                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, yPosition);
                contentStream.showText("Historial de Estados de Ánimo");
                contentStream.endText();
                yPosition -= lineHeight * 2;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                for (MoodEntry entry : historial) {
                    contentStream.beginText();
                    contentStream.setFont(font, 12);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    String moodLine = dateFormat.format(entry.getDate()) + " - " + entry.getMood().toString();
                    contentStream.showText(moodLine);
                    contentStream.endText();
                    yPosition -= lineHeight;

                    contentStream.beginText();
                    contentStream.setFont(fontRegular, 10);
                    contentStream.newLineAtOffset(marginLeft, yPosition);
                    String description = entry.getText();
                    float maxWidth = page.getMediaBox().getWidth() - marginLeft - marginRight;
                    List<String> lines = splitTextIntoLines(description, fontRegular, 10, maxWidth);
                    for (String line : lines) {
                        contentStream.showText(line);
                        yPosition -= lineHeight;
                        if (yPosition < 50) {
                            contentStream.endText();
                            contentStream.close();

                            page = new PDPage();
                            document.addPage(page);
                            contentStream = new PDPageContentStream(document, page);
                            yPosition = marginTop;
                            contentStream.setFont(fontRegular, 10);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(marginLeft, yPosition);
                        } else {
                            contentStream.newLineAtOffset(0, -lineHeight);
                        }
                    }
                    contentStream.endText();
                    yPosition -= lineHeight;
                }

                contentStream.close();

                document.save(file);
                showAlert("PDF generado", "El archivo PDF se ha guardado correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo generar el PDF.");
            }
        }
    }

    private List<String> splitTextIntoLines(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        for (String word : words) {
            String potentialLine = currentLine.length() == 0 ? word : currentLine + " " + word;
            float width = font.getStringWidth(potentialLine) / 1000 * fontSize;
            if (width <= maxWidth) {
                currentLine.append(currentLine.length() == 0 ? word : " " + word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}