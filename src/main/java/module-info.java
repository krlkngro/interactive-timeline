module ee.ut {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;

    opens ee.ut to javafx.fxml;
    opens ee.ut.dataObjects to javafx.base, com.fasterxml.jackson.databind;
    exports ee.ut.dataObjects to com.fasterxml.jackson.databind;
    exports ee.ut;
    exports ee.ut.controllers;
    opens ee.ut.controllers to javafx.fxml;
}
