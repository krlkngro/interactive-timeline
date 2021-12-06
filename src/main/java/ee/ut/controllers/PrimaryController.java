package ee.ut.controllers;

import javafx.fxml.FXML;

import java.io.IOException;

import static ee.ut.App.startTimeline;

public class PrimaryController {

    @FXML
    private void startNewTimeline() throws IOException {
        startTimeline(true);
    }

    @FXML
    private void editPreviousTimeline() throws IOException{
        startTimeline(false);
    }
}
