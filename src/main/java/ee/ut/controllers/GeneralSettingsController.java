package ee.ut.controllers;

import ee.ut.App;
import ee.ut.dataObjects.Data;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import static ee.ut.logic.Editor.saveSettings;

public class GeneralSettingsController {
    @FXML
    private Button saveButton1;

    @FXML
    private CheckBox eventsPacked;

    @FXML
    private ComboBox<String> labelType;

    @FXML
    private TextField eventSpace;

    @FXML
    private TextField readMore;

    public void initialize() {
        //Allow only numbers in eventSpace textfield
        eventSpace.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,5}")) {
                eventSpace.setText(oldValue);
            }
        });
        Data data = App.getData();
        this.labelType.setValue(data.getLabelType().toString());
        this.readMore.setText(data.getReadMore());
        this.eventSpace.setText(String.valueOf(data.getEventSpace()));
    }

    public void save(ActionEvent actionEvent) {
        saveSettings(
                App.getData(),
                this.labelType.getValue(),
                this.eventSpace.getText(),
                this.eventsPacked.isSelected(),
                this.readMore.getText()
        );
        App.updatePreview();
    }
}
