package ee.ut;

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

    public void initialize() {
        //Allow only numbers in eventSpace textfield
        eventSpace.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,5}")) {
                    eventSpace.setText(oldValue);
                }
            }
        });
    }

    public void save(ActionEvent actionEvent) {
        saveSettings(
                App.getData(),
                this.labelType.getValue(),
                this.eventSpace.getText(),
                this.eventsPacked.isSelected()
        );
        App.updatePreview();
    }
}
