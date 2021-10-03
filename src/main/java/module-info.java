module ee.ut {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens ee.ut to javafx.fxml;
    exports ee.ut;
}
