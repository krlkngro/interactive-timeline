package ee.ut.logic;

import ee.ut.dataObjects.Data;
import ee.ut.dataObjects.Event;
import ee.ut.dataObjects.Type;
import org.junit.Test;

import java.util.Arrays;
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

    @Test
    public void savingSettingsSavesCorrectLabelType() {
        Data data = new Data();
        assertEquals(Type.LINE, data.getLabelType());
        Editor.saveSettings(
                data,
                "Punkt",
                "300",
                true
        );
        assertEquals(Type.DOT, data.getLabelType());
    }

    @Test
    public void savingSettingsSavesCorrectEventSpace() {
        Data data = new Data();
        assertEquals(200, data.getEventSpace());
        Editor.saveSettings(
                data,
                "Punkt",
                "300",
                true
        );
        assertEquals(300, data.getEventSpace());
    }

    @Test
    public void enablingEventsPackedChangesPackedValueForExistingEvents() {
        Event event = new Event();
        Event event1 = new Event();
        Data data = new Data(Type.TEXT, 200, Arrays.asList(event, event1));
        for (Event e : data.getEvents()) {
            assertEquals(false, e.getPacked());
        }
        Editor.saveSettings(
                data,
                "Punkt",
                "300",
                true
        );
        for (Event e : data.getEvents()) {
            assertEquals(true, e.getPacked());
        }
    }

    @Test
    public void disablingEventsPackedChangesPackedValueForExistingEvents() {
        Data data = new Data();
        Event event = new Event();
        event.setPacked(true);
        data.getEvents().add(event);
        Editor.saveSettings(
                data,
                "Joon",
                "200",
                false
        );
        for (Event e : data.getEvents()) {
            assertEquals(false, e.getPacked());
        }
    }

    @Test
    public void savingSettingsChangesPackedValueForNewEvents() {
        Data data = new Data();
        Editor.saveSettings(
                data,
                "Joon",
                "200",
                false
        );
        Event event = new Event();
        Editor.saveEvent(
                data,
                event,
                "testHtml",
                "testLabel",
                1,
                true
        );
        for (Event e : data.getEvents()) {
            assertEquals(false, e.getPacked());
        }
        Editor.saveSettings(
                data,
                "Punkt",
                "300",
                true
        );
        Event event1 = new Event();
        Editor.saveEvent(
                data,
                event1,
                "testHtml",
                "testLabel",
                1,
                true
        );
        for (Event e : data.getEvents()) {
            assertEquals(true, e.getPacked());
        }

    }

}
