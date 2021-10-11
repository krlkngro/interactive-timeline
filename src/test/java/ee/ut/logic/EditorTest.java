package ee.ut.logic;

import ee.ut.dataObjects.Data;
import ee.ut.dataObjects.Event;
import ee.ut.dataObjects.Type;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
    public void savingExistingEventDoesNotAddAdditionalEventToData() {
        Data data = new Data();
        Event event = new Event();
        data.getEvents().add(event);
        assertEquals(1, data.getEvents().size());
        Editor.saveEvent(
                data,
                event,
                "testHtml",
                "testLabel",
                1,
                false
        );
        assertEquals(1, data.getEvents().size());
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
                1,
                true
        );
        assertEquals("testHtml", event.getHtmlContent());
        assertEquals("testLabel", event.getLabel());
        assertEquals(1, event.getQueueNr());
        assertEquals(event, data.getEvents().get(0));
    }

    @Test
    public void savingExistingEventSavesCorrectData() {
        Event event = new Event(false, 99, "TestTest", "TestTestHtml", 200, 200, false);
        Data data = new Data(Type.TEXT, 200, new ArrayList<>(List.of(event)));
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
                1,
                false
        );
        assertEquals(1, data.getEvents().size());
        Event savedEvent = data.getEvents().get(0);
        assertEquals("testHtml", savedEvent.getHtmlContent());
        assertEquals("testLabel", savedEvent.getLabel());
        assertEquals(1, savedEvent.getQueueNr());
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
            assertFalse(e.getPacked());
        }
        Editor.saveSettings(
                data,
                "Punkt",
                "300",
                true
        );
        for (Event e : data.getEvents()) {
            assertTrue(e.getPacked());
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
            assertFalse(e.getPacked());
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
            assertFalse(e.getPacked());
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
            assertTrue(e.getPacked());
        }

    }

    @Test
    public void testDeletingEventRemovesItFromData() {
        Data data = new Data();
        Event event1 = new Event();
        Event event2 = new Event();
        data.setEvents(new ArrayList<>(Arrays.asList(event1, event2)));
        assertEquals(2, data.getEvents().size());
        Editor.deleteEvent(data, event1);
        assertEquals(1, data.getEvents().size());
        assertEquals(event2, data.getEvents().get(0));
        assertNotEquals(event1, data.getEvents().get(0));
    }



    @Test
    public void movingElementToTheEndOfQueueChangesEventsListCorrectly() {
        Data data = new Data();
        Event event1 = new Event();
        event1.setQueueNr(1);
        Event event2 = new Event();
        event1.setQueueNr(2);
        Event event3 = new Event();
        event1.setQueueNr(3);
        Event event4 = new Event();
        event1.setQueueNr(4);
        data.setEvents(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));
        Editor.saveEvent(data, event2, "", "", 4, false);
        assertEquals(Arrays.asList(event1, event3, event4, event2), data.getEvents());
        assertEquals(1, event1.getQueueNr());
        assertEquals(2, event3.getQueueNr());
        assertEquals(3, event4.getQueueNr());
        assertEquals(4, event2.getQueueNr());
    }


    @Test
    public void movingElementToTheStartOfQueueChangesEventsListCorrectly() {
        Data data = new Data();
        Event event1 = new Event();
        event1.setQueueNr(1);
        Event event2 = new Event();
        event1.setQueueNr(2);
        Event event3 = new Event();
        event1.setQueueNr(3);
        Event event4 = new Event();
        event1.setQueueNr(4);
        data.setEvents(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));
        Editor.saveEvent(data, event3, "", "", 1, false);
        assertEquals(Arrays.asList(event3, event1, event2, event4), data.getEvents());
        assertEquals(1, event3.getQueueNr());
        assertEquals(2, event1.getQueueNr());
        assertEquals(3, event2.getQueueNr());
        assertEquals(4, event4.getQueueNr());
    }


    @Test
    public void movingElementBackInQueueChangesEventsListCorrectly() {
        Data data = new Data();
        Event event1 = new Event();
        event1.setQueueNr(1);
        Event event2 = new Event();
        event1.setQueueNr(2);
        Event event3 = new Event();
        event1.setQueueNr(3);
        Event event4 = new Event();
        event1.setQueueNr(4);
        data.setEvents(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));
        Editor.saveEvent(data, event4, "", "", 2, false);
        assertEquals(Arrays.asList(event1, event4, event2, event3), data.getEvents());
        assertEquals(1, event1.getQueueNr());
        assertEquals(2, event4.getQueueNr());
        assertEquals(3, event2.getQueueNr());
        assertEquals(4, event3.getQueueNr());
    }


    @Test
    public void movingElementForwardInQueueChangesEventsListCorrectly() {
        Data data = new Data();
        Event event1 = new Event();
        event1.setQueueNr(1);
        Event event2 = new Event();
        event1.setQueueNr(2);
        Event event3 = new Event();
        event1.setQueueNr(3);
        Event event4 = new Event();
        event1.setQueueNr(4);
        data.setEvents(new ArrayList<>(Arrays.asList(event1, event2, event3, event4)));
        Editor.saveEvent(data, event1, "", "", 3, false);
        assertEquals(Arrays.asList(event2, event3, event1, event4), data.getEvents());
        assertEquals(1, event2.getQueueNr());
        assertEquals(2, event3.getQueueNr());
        assertEquals(3, event1.getQueueNr());
        assertEquals(4, event4.getQueueNr());
    }
}
