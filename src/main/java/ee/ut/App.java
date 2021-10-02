package ee.ut;

import ee.ut.dataObjects.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Tab settingsTab;
    private static Tab eventsTab;
    private static Data data;

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        settingsTab = new Tab();
        settingsTab.setText("Sätted");
        settingsTab.setClosable(false);
        settingsTab.setContent(loadFXML("settings"));

        eventsTab = new Tab();
        eventsTab.setText("Sündmused");
        eventsTab.setClosable(false);
        eventsTab.setContent(loadFXML("events"));

        Scene scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void startTimeline(boolean isNew) {
        Group root = new Group();
        Scene scene = new Scene(root, 600, 500, Color.WHITE);
        if (isNew) {
            data = new Data();
        } else {
            //todo implement loading data from js
            throw new IllegalArgumentException("Editing and existing timeline not yet implemented");
        }

        TabPane tabPane = new TabPane(settingsTab, eventsTab);

        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setCenter(tabPane);
        root.getChildren().add(borderPane);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch();
    }

    static Data getData() {
        return data;
    }

}