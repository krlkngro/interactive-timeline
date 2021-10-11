package ee.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ut.dataObjects.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Tab settingsTab;
    private static Tab eventsTab;
    private static Data data;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Path resultFolder = Path.of(System.getProperty("user.dir") + "\\result");
        if (!Files.exists(resultFolder)) {
            Files.createDirectory(resultFolder);
            Files.write(resultFolder.resolve("style.css"), Objects.requireNonNull(App.class.getResourceAsStream("style.css")).readAllBytes());
            Files.write(resultFolder.resolve("testCSS.css"), Objects.requireNonNull(App.class.getResourceAsStream("testCSS.css")).readAllBytes());
            Files.write(resultFolder.resolve("timeline.html"), Objects.requireNonNull(App.class.getResourceAsStream("timeline.html")).readAllBytes());
            Files.write(resultFolder.resolve("timelineGenerator.js"), Objects.requireNonNull(App.class.getResourceAsStream("timelineGenerator.js")).readAllBytes());
        }
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void startTimeline(boolean isNew) throws IOException {
        Group root = new Group();
        if (isNew) {
            data = new Data();
        } else {
            //todo implement loading data from js
            throw new IllegalArgumentException("Editing and existing timeline not yet implemented");
        }

        settingsTab = new Tab();
        settingsTab.setText("Sätted");
        settingsTab.setClosable(false);
        settingsTab.setContent(loadFXML("settings"));

        eventsTab = new Tab();
        eventsTab.setText("Sündmused");
        eventsTab.setClosable(false);
        eventsTab.setContent(loadFXML("events"));

        TabPane tabPane = new TabPane(settingsTab, eventsTab);
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("file");
        MenuItem saveFile = new MenuItem("Salvesta ajajoon");
        saveFile.setOnAction(event -> {
            Path file = Path.of(System.getProperty("user.dir") + "\\result\\data.js");
            try {
                Files.writeString(file, "const data = " + new ObjectMapper().writeValueAsString(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        Menu previewMenu = new Menu("eelvaade");
        MenuItem showPreview = new MenuItem("kuva eelvaade");

        showPreview.setOnAction(actionEvent -> {
            Path path = Path.of(System.getProperty("user.dir") + "\\result\\timeline.html");
            String content;
            try {
                content = Files.readString(path);
                content = content.replace("<script src=\"data.js\">", "<script> const data = " + new ObjectMapper().writeValueAsString(data));
                content = content.replace("<script src=\"", "<script src=\"file:///"+System.getProperty("user.dir")+"\\result\\");
                content = content.replace("testCSS.css", "file:///"+System.getProperty("user.dir")+"\\result\\testCSS.css");

                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();
                webEngine.loadContent(content);
                VBox vBox = new VBox(webView);

                Scene scene = new Scene(vBox, 800, 600);
                Stage stage = new Stage();
                stage.setTitle("Eelvaade");
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fileMenu.getItems().add(saveFile);
        previewMenu.getItems().add(showPreview);
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(previewMenu);
        BorderPane borderPane = new BorderPane();
        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);
        //borderPane.setBottom(scrollPane);
        root.getChildren().add(borderPane);
        scene.setRoot(root);
    }

    public static void main(String[] args) {
        launch();
    }

    static Data getData() {
        return data;
    }

    public Scene getScene() {
        return scene;
    }

}