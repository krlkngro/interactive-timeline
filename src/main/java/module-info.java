module ee.ut {
    requires javafx.controls;
    requires javafx.fxml;

    opens ee.ut to javafx.fxml;
    exports ee.ut;
}
