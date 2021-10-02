package ee.ut;

import ee.ut.dataObjects.Event;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;

import static ee.ut.App.getData;

public class EventsController {
    private Event eventToEdit;
    private boolean editingNewEvent;

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
    private void newEvent() {
        toggleAll();
        this.eventToEdit = new Event();
        this.editingNewEvent = true;
        //todo this is a horrible hack that might not even always work and
        // thus should be replaced if at all possible
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> {
            ComboBox<String> fontSelection = (ComboBox<String>) htmlEditor.lookupAll(".font-menu-button").toArray(new Node[0])[1];
            fontSelection.setValue("Rubik");
            fontSelection.setItems(FXCollections.observableArrayList("Rubik"));
            this.htmlEditor.setHtmlText("""
                <html dir="ltr">
                <head>
                    <link rel="stylesheet" href="%s\\results\\style.css">
                </head>
                <body contentEditable="true">
                %s
                </body>
                """.formatted(System.getProperty("user.dir"),this.eventToEdit.getHtmlContent()));
            fontSelection.setVisible(false);
            fontSelection.setManaged(false);
        });
        new Thread(sleeper).start();
    }

    @FXML
    private void save() {
        this.eventToEdit.setHtmlContent(this.htmlEditor.getHtmlText().replaceAll("(<html.*<body.*?>)|(</body></html>)", ""));
        //todo implement setting other fields for event based on inputs
        if (this.editingNewEvent) {
            this.editingNewEvent = false;
            getData().getEvents().add(this.eventToEdit);
        }
        toggleAll();
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
