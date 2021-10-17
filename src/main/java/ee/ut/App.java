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
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
/**
 * JavaFX App
 */
public class App extends Application {

    private static Tab settingsTab;
    private static Tab eventsTab;
    private static Data data;

    private static Scene scene;
    private static Stage previewStage;

    @Override
    public void start(Stage stage) throws IOException {
        previewStage = new Stage();
        Path resultFolder = Path.of(System.getProperty("user.dir") + "\\result");
        if (!Files.exists(resultFolder)) {
            Files.createDirectory(resultFolder);
            Files.write(resultFolder.resolve("style.css"), Objects.requireNonNull(App.class.getResourceAsStream("style.css")).readAllBytes());
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
            Path resultFolder = Path.of(System.getProperty("user.dir") + "\\result");
            Path file = resultFolder.resolve("data.js");
            Path imageFolder = resultFolder.resolve("images");
            try {
                if (data.getEvents().stream().anyMatch(e -> e.getImagePaths().size() > 0)) {
                    if (imageFolder.toFile().exists()) {
                        FileUtils.cleanDirectory(imageFolder.toFile());
                    } else {
                        Files.createDirectories(imageFolder);
                    }
                    data.getEvents().forEach(e -> e.getImagePaths().forEach(path -> {
                        Path imageFilePath = Path.of(path);
                        if (imageFolder.toUri().relativize(path).equals(path)) {
                            try {
                                FileUtils.copyFileToDirectory(imageFilePath.toFile(), imageFolder.toFile());
                            } catch (IOException ex) {
                                //todo notify user that copying image failed
                                ex.printStackTrace();
                            }
                        }
                        e.setHtmlContent(e.getHtmlContent().replaceFirst("file:/(//)?"+imageFilePath.toString().replaceAll("\\\\", "/"), "images/"+imageFilePath.getFileName()));
                    }));
                }

                Files.writeString(file, "const data = " + new ObjectMapper().writeValueAsString(data));
                scene.setRoot(loadFXML("primary"));
            } catch (IOException e) {
                //todo notify user
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
                content = content.replace("style.css", "file:///"+System.getProperty("user.dir")+"\\result\\style.css");

                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();
                webEngine.loadContent(content);
                VBox vBox = new VBox(webView);

                Scene scene = new Scene(vBox, 800, 600);
                previewStage.setTitle("Eelvaade");
                previewStage.setScene(scene);
                previewStage.show();

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