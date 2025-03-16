import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/view/icons/mood.png"));

            primaryStage.getIcons().add(icon);

            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
            System.setProperty("org.slf4j.simpleLogger.log.org.mongodb", "off");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"));
            StackPane root = loader.load();

            Scene scene = new Scene(root, 500, 500);
            primaryStage.setTitle("Mood Dashboard - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Error al cargar la vista login.fxml", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void hideStage(Stage stage) {
        stage.hide();
    }
}