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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

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

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Vali soovitud ajajoone js fail");
            File file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                String dataFromFile = Files.readString(file.toPath());
                System.out.println(dataFromFile);
                //TODO: actually read data from file

            }
            //throw new IllegalArgumentException("Editing and existing timeline not yet implemented");
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
                if (!imageFolder.toFile().exists()) {
                    Files.createDirectories(imageFolder);
                }
                if (data.getEvents().stream().anyMatch(e -> e.getImagePaths().size() > 0)) {
                    data.getEvents().forEach(e -> {
                        try {
                            Path eventImageFolder = imageFolder.resolve(e.getUuid().toString());
                            if (!eventImageFolder.toFile().exists()) {
                                Files.createDirectories(eventImageFolder);
                            }
                            AtomicInteger largestNumber = new AtomicInteger(
                                    Arrays.stream(eventImageFolder.toFile().listFiles())
                                    .map(imageFile -> Integer.parseInt(imageFile.getName().split("\\.")[0]))
                                    .max(Integer::compareTo)
                                    .orElse(0)
                            );
                            e.getImagePaths().forEach(path -> {
                                try {
                                    Path imageFilePath = Path.of(path);
                                    Path savedImage = imageFilePath;
                                    if (eventImageFolder.toUri().relativize(path).equals(path) && e.getHtmlContent().contains("src=\"" + path + "\"")) {
                                        String fileName = imageFilePath.getFileName().toString();
                                        savedImage = eventImageFolder.resolve(largestNumber.incrementAndGet() + (fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : ""));
                                        Files.copy(imageFilePath, savedImage, StandardCopyOption.REPLACE_EXISTING);
                                    }
                                    e.setHtmlContent(e.getHtmlContent().replaceFirst(path.toString(), "images/" + e.getUuid().toString() + "/" + savedImage.getFileName()));
                                } catch (IOException ex) {
                                    //todo notify user that copying image failed
                                    ex.printStackTrace();
                                }
                            });
                            Arrays.stream(eventImageFolder.toFile().listFiles())
                                    .filter(imageFile -> !e.getHtmlContent().contains("src=\"images/"+e.getUuid().toString()+"/"+imageFile.getName()+"\""))
                                    .forEach(File::delete);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

                }
                Arrays.stream(imageFolder.toFile().listFiles()).filter(
                        f -> data.getEvents().stream().noneMatch(e -> e.getUuid().toString().equals(f.getName()))
                ).forEach(f -> {
                    if (f.isDirectory()) {
                        deleteDir(f);
                    } else {
                        f.delete();
                    }
                });
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

    public static void updatePreview(){
        if (previewStage != null && previewStage.isShowing()) {
            Path path = Path.of(System.getProperty("user.dir") + "\\result\\timeline.html");
            String content;
            try {
                content = Files.readString(path);
                content = content.replace("<script src=\"data.js\">", "<script> const data = " + new ObjectMapper().writeValueAsString(data));
                content = content.replace("<script src=\"", "<script src=\"file:///" + System.getProperty("user.dir") + "\\result\\");
                content = content.replace("style.css", "file:///" + System.getProperty("user.dir") + "\\result\\style.css");

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
        }
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

    private static void deleteDir(File dir) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(f -> {
            if (f.isDirectory()) {
                deleteDir(f);
            }
            f.delete();
        });
        dir.delete();
    }
}