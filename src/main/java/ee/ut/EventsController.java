package ee.ut;

import ee.ut.dataObjects.Event;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ee.ut.App.getData;
import static ee.ut.logic.Editor.saveEvent;

public class EventsController {
    private Event eventToEdit;
    private boolean editingNewEvent;
    private boolean editorInitialized = false;

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
        toggleAll();
        this.eventToEdit = new Event();
        this.editingNewEvent = true;
        File css = new File(System.getProperty("user.dir")+"\\result\\style.css");
        String cssText = "";
        try {
            cssText = Files.readString(Path.of(css.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setEditorText("""
                <html dir="ltr">
                <head>
                    <style>%s</style>
                </head>
                <body contentEditable="true">
                %s
                </body>
                </html>
                """.formatted(cssText,this.eventToEdit.getHtmlContent()));
        SpinnerValueFactory.IntegerSpinnerValueFactory queueValueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) this.queueNr.getValueFactory();
        queueValueFactory.setMin(1);
        queueValueFactory.setMax(getData().getEvents().size() + (this.editingNewEvent? 1 : 0));
        if (this.editingNewEvent) {
            queueValueFactory.setValue(getData().getEvents().size()+1);
        } else {
            queueValueFactory.setValue(this.eventToEdit.getQueueNr());
        }
        label.setText(this.eventToEdit.getLabel());
        if (!editorInitialized) {
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
                foreground.setVisible(false);
                foreground.setManaged(false);
                Node background = htmlEditor.lookup(".html-editor-background");
                background.setVisible(false);
                background.setManaged(false);

            });
            new Thread(sleeper).start();
        }
    }

    @FXML
    private void save() {
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
}
