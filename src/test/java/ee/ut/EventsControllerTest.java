package ee.ut;

import ee.ut.dataObjects.Data;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit.ApplicationTest;

import java.io.IOException;
import java.util.Objects;

public class EventsControllerTest extends ApplicationTest {
    private static MockedStatic<App> mockApp;

    @Before
    public void setUp() {
        mockApp = Mockito.mockStatic(App.class);
        mockApp.when(App::getData).thenReturn(new Data());
    }

    @Override
    public void start(Stage stage) throws IOException {
        mockApp = Mockito.mockStatic(App.class);
        mockApp.when(App::getData).thenReturn(new Data());
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(App.class.getResource("events.fxml")))));
        stage.show();
        stage.toFront();
        mockApp.close();
    }

    @Test
    public void eventEditorStartsInvisible() {
        Assert.assertFalse(lookup("#editor").query().isVisible());
        Assert.assertFalse(lookup("#editor").query().isManaged());
    }

    @Test
    public void HTMLEditorBecomesVisibleOnceNewEventButtonClicked() {
        lookup("#newEventButton").queryButton().fire();
        Assert.assertTrue(lookup("#editor").query().isVisible());
        Assert.assertTrue(lookup("#editor").query().isManaged());
    }



    @Test
    public void HTMLEditorBecomesInvisibleOnceCancelButtonClicked() {
        lookup("#newEventButton").queryButton().fire();
        lookup("#cancelButton").queryButton().fire();
        Assert.assertFalse(lookup("#editor").query().isVisible());
        Assert.assertFalse(lookup("#editor").query().isManaged());
    }

    @After
    public void tearDown() {
        mockApp.close();
    }


}
