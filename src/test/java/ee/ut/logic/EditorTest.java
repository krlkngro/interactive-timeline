package ee.ut.logic;

import ee.ut.dataObjects.Data;
import ee.ut.dataObjects.Event;
import ee.ut.dataObjects.Type;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class EditorTest {
    @Test
    public void savingNewEventAddsEventToData() {
        Data data = new Data();
        Event event = new Event();
        assertEquals(0, data.getEvents().size());
        Editor.saveEvent(
                data,
                event,
                "testHtml",
                "testLabel",
                1,
                true
        );
        assertEquals(1, data.getEvents().size());
    }

    @Test
    public void savingExistingEventDoesNotAddEventToData() {
        Data data = new Data();
        Event event = new Event();
        assertEquals(0, data.getEvents().size());
        Editor.saveEvent(
                data,
                event,
                "testHtml",
                "testLabel",
                1,
                false
        );
        assertEquals(0, data.getEvents().size());
    }

    @Test
    public void savingNewEventSavesCorrectData() {
        Data data = new Data();
        Event event = new Event();
        Editor.saveEvent(
                data,
                event,
                """
                <html dir="ltr">
                <head>
                <style>* {color: red;}</style>
                </head>
                <body contenteditable="true">
                testHtml
                </body>
                </html>
                """,
                "testLabel",
                2,
                true
        );
        assertEquals("testHtml", event.getHtmlContent());
        assertEquals("testLabel", event.getLabel());
        assertEquals(2, event.getQueueNr());
        assertEquals(event, data.getEvents().get(0));
    }

    @Test
    public void savingExistingEventSavesCorrectData() {
        Event event = new Event(false, 99, "TestTest", "TestTestHtml", 200, 200, false);
        Data data = new Data(Type.TEXT, 200, Collections.singletonList(event));
        Editor.saveEvent(
                data,
                event,
                """
                <html dir="ltr">
                <head>
                <style>* {color: red;}</style>
                </head>
                <body contenteditable="true">
                testHtml
                </body>
                </html>
                """,
                "testLabel",
                2,
                false
        );
        assertEquals(1, data.getEvents().size());
        Event savedEvent = data.getEvents().get(0);
        assertEquals("testHtml", savedEvent.getHtmlContent());
        assertEquals("testLabel", savedEvent.getLabel());
        assertEquals(2, savedEvent.getQueueNr());
        assertEquals(event, savedEvent);
    }

}
