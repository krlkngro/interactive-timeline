package ee.ut;

import ee.ut.dataObjects.Event;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.css.StyleableProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;

import static ee.ut.App.getData;
import static ee.ut.logic.Editor.deleteEvent;
import static ee.ut.logic.Editor.saveEvent;

public class EventsController implements Initializable {
    private Event eventToEdit;
    private boolean editingNewEvent;
    private boolean editorInitialized = false;
    private String imageResizeScript;
    private String cssText;

    @FXML
    private TableView<Event> savedEvents;

    @FXML
    private Button saveButton;

    @FXML
    private Button newEventButton;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private VBox events;

    @FXML
    private VBox editor;

    @FXML
    private TextField label;

    @FXML
    private Spinner<Integer> queueNr;

    @FXML
    private void newEvent() {
        this.eventToEdit = new Event();
        this.editingNewEvent = true;
        startEdit();
    }

    private void editEvent(Event event) {
        this.eventToEdit = event;
        this.editingNewEvent = false;
        startEdit();
    }

    private void startEdit() {
        toggleAll();
        setEditorText("""
                <html dir="ltr">
                <head>
                    <style>%s</style>
                </head>
                <body contentEditable="true" class="content" style="width: 600px">
                %s
                </body>
                <script>
                %s
                </script>
                </html>
                """.formatted(cssText,this.eventToEdit.getHtmlContent(), imageResizeScript));
        SpinnerValueFactory.IntegerSpinnerValueFactory queueValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) this.queueNr.getValueFactory();
        queueValueFactory.setMin(1);
        queueValueFactory.setMax(getData().getEvents().size() + (this.editingNewEvent? 1 : 0));
        if (this.editingNewEvent) {
            queueValueFactory.setValue(getData().getEvents().size()+1);
        } else {
            queueNr.getEditor().setText(String.valueOf(this.eventToEdit.getQueueNr()));
            queueValueFactory.setValue(this.eventToEdit.getQueueNr());
        }
        label.setText(this.eventToEdit.getLabel());
        if (!editorInitialized) {
            editorInitialized = true;
            //todo still not great but better than before
            Task<Void> sleeper = new Task<>() {
                @Override
                protected Void call() {
                    long start = System.currentTimeMillis();
                    while (htmlEditor.lookupAll(".font-menu-button").size() < 3 && System.currentTimeMillis() - start < 10000)
                        ;
                    return null;
                }
            };
            sleeper.setOnSucceeded(event -> {
                ComboBox<String> fontSelection = (ComboBox<String>) htmlEditor.lookupAll(".font-menu-button").toArray(new Node[0])[1];
                fontSelection.setValue("Rubik");
                fontSelection.setItems(FXCollections.observableArrayList("Rubik"));
                fontSelection.setVisible(false);
                fontSelection.setManaged(false);
                Node foreground = htmlEditor.lookup(".html-editor-foreground");
                if (foreground == null) {
                    editorInitialized = false;
                    startEdit();
                }
                HBox colorParent = (HBox) foreground.getParent();
                foreground.setVisible(false);
                foreground.setManaged(false);
                Node background = htmlEditor.lookup(".html-editor-background");
                background.setVisible(false);
                background.setManaged(false);
                Button imageButton = new Button();
                imageButton.setTooltip(new Tooltip("Insert image"));
                Image imageButtonIcon = new Image(Objects.requireNonNull(App.class.getResource("outline_image_black_24dp.png")).toString());
                ColorAdjust lighten = new ColorAdjust(0, 0, 0.5, 0);
                ImageView imageButtonIconView = new ImageView(imageButtonIcon);
                FileChooser imagePicker = new FileChooser();
                imagePicker.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"), new FileChooser.ExtensionFilter("All files", "*"));
                imageButton.setOnAction(actionEvent -> {
                    File imageFile = imagePicker.showOpenDialog(htmlEditor.getScene().getWindow());
                    if (imageFile != null) {
                        htmlEditor.setHtmlText(htmlEditor.getHtmlText().replace("</body>", "<img src=\"" + imageFile.toURI() + "\"></body>"));
                        eventToEdit.getImagePaths().add(imageFile.toURI());
                    }
                });
                imageButtonIconView.setEffect(lighten);
                ((StyleableProperty)imageButton.graphicProperty()).applyStyle(null, imageButtonIconView);
                colorParent.getChildren().add(imageButton);

            });
            new Thread(sleeper).start();
        }
    }

    @FXML
    private void save() {
        ((WebView) htmlEditor.lookup("WebView")).getEngine().executeScript("document.getElementsByTagName(\"body\")[0].dispatchEvent(new Event(\"scroll\"))");
        saveEvent(
                App.getData(),
                this.eventToEdit,
                this.htmlEditor.getHtmlText(),
                this.label.getText(),
                this.queueNr.getValue(),
                this.editingNewEvent
        );
        this.editingNewEvent = false;
        setEditorText("");
        this.label.setText("");
        this.queueNr.getEditor().setText("");
        this.queueNr.commitValue();
        toggleAll();
        savedEvents.refresh();
    }

    @FXML
    private void cancel() {
        setEditorText("");
        this.label.setText("");
        this.queueNr.getEditor().setText("");
        this.queueNr.commitValue();
        toggleAll();
    }

    private void setEditorText(String text) {
        //todo Only done like this because tests crash if setHtmlText is called normally
        // try to figure out a way to fix that or perhaps get rid of GUI tests
        Task<Void> clearHtml = new Task<>() {
            @Override
            protected Void call() {
                return null;
            }
        };
        clearHtml.setOnSucceeded(event -> htmlEditor.setHtmlText(text));
        new Thread(clearHtml).start();
    }

    private void toggle(Node node) {
        node.setVisible(!node.isVisible());
        node.setManaged(!node.isManaged());
    }

    private void toggleAll() {
        toggle(events);
        toggle(editor);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.savedEvents.setItems(FXCollections.observableList(getData().getEvents()));
        TableColumn<Event, Void> editButton = new TableColumn<>();
        TableColumn<Event, Void> deleteButton = new TableColumn<>();
        editButton.setCellFactory((param) -> new TableCell<>() {
            private final Button button = new Button("Muuda");
            {
                button.setOnAction(event -> editEvent(getTableRow().getItem()));
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
        deleteButton.setCellFactory((param) -> new TableCell<>() {
            private final Button button = new Button("Kustuta");
            {
                button.setOnAction(event -> {
                    deleteEvent(getData(), getTableRow().getItem());
                    savedEvents.refresh();
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        //Currently the max width value is set with a syntax of "percentage value of the table width to cover" * 10000
        // if a more reasonable way of achieving this is found, please replace this
        editButton.setMaxWidth(100000);
        deleteButton.setMaxWidth(100000);

        savedEvents.getColumns().addAll(editButton, deleteButton);

        File css = new File(System.getProperty("user.dir")+"\\result\\style.css");
        cssText = "";
        imageResizeScript = "";
        try {
            cssText = Files.readString(Path.of(css.getAbsolutePath()));
            // todo: this script has a bug when clicking on image while the top of the image is not on screen:
            //  the handle for resizing will appear at a wrong spot (too high).
            //  As this bug only occurs in javafx as far as I know and is not reproducible in an actual browser, I currently have no idea how to fix this
            imageResizeScript = Files.readString(Path.of(Objects.requireNonNull(App.class.getResource("resizeImages.js")).toURI()));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
