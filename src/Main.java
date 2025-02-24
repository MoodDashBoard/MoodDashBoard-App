import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
            System.setProperty("org.slf4j.simpleLogger.log.org.mongodb", "off");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 400, 300);
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