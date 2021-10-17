module ee.ut {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.io;

    opens ee.ut to javafx.fxml;
    opens ee.ut.dataObjects to javafx.base;
    exports ee.ut.dataObjects to com.fasterxml.jackson.databind;
    exports ee.ut;
}
