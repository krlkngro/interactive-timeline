package ee.ut.logic;

import ee.ut.dataObjects.Data;
import ee.ut.dataObjects.Event;

import java.util.List;

public class Editor {

    private static Boolean allEventsPacked = false;

    public static void saveEvent(
            Data data,
            Event event,
            String htmlText,
            String label,
            Integer queueNr,
            boolean isNew) {
        event.setHtmlContent(htmlText.replaceAll("(<html(.|\n)*<body.*?>)|(</body>\n*</html>)|(\n)", ""));
        List<Event> events = data.getEvents();
        if (isNew) {
            events.add(event);
        }
        event.setQueueNr(Math.max(1, queueNr));
        for (Event dataEvent: events) {
            if (dataEvent.getQueueNr() >= event.getQueueNr() && dataEvent != event) {
                dataEvent.setQueueNr(dataEvent.getQueueNr()+1);
            }
        }
        event.setLabel(label);
        event.setPacked(allEventsPacked);
    }

    public static void saveSettings(
            Data data,
            String labelType,
            String eventSpace,
            Boolean eventsPacked
    ){

        data.setEventSpace(Integer.valueOf(eventSpace));
        data.setLabelType(labelType);
        if(eventsPacked){
            allEventsPacked = true; //for future events
            for (Event event: data.getEvents()) { //change all existing events
                event.setPacked(true);
            }
        }else {
            allEventsPacked = false; //for future events
            for (Event event: data.getEvents()) { //change all existing events
                event.setPacked(false);
            }
        }

    }
}
