package ee.ut;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Assert;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;

public class EventsControllerTest extends ApplicationTest {
    private final App app = new App();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("events.fxml"))));
        stage.show();
        stage.toFront();
    }

    @Test
    public void HTMLEditorStartsInvisible() {
        Assert.assertFalse(lookup("#htmlEditor").query().isVisible());
        Assert.assertFalse(lookup("#htmlEditor").query().isManaged());
    }

    @Test
    public void HTMLEditorBecomesVisibleOnceNewEventButtonClicked() {
        lookup("#newEventButton").queryButton().fire();
        Assert.assertTrue(lookup("#htmlEditor").query().isVisible());
        Assert.assertTrue(lookup("#htmlEditor").query().isManaged());
    }

}
