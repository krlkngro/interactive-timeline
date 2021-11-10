package ee.ut.logic;

import ee.ut.App;
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
        htmlText = htmlText.substring(Math.max(0, htmlText.indexOf("<body")), Math.max(0, htmlText.indexOf("</body>")));
        htmlText = htmlText.replaceAll("(<body.*?>)|(\n)", "");
        int scriptStart = htmlText.indexOf("<script");
        if (scriptStart >= 0) {
            int scriptEnd  = htmlText.indexOf("</script>");
            htmlText = htmlText.substring(0, scriptStart) + htmlText.substring(scriptEnd+9);
        }
        event.setHtmlContent(htmlText);
        List<Event> events = data.getEvents();
        if (!isNew) {
            events.remove(event);
        }
        event.setLabel(label);
        event.setPacked(allEventsPacked);
        events.add(queueNr-1, event);
        for (int i = 1; i <= events.size(); i++) {
            events.get(i-1).setQueueNr(i);
        }
        App.updatePreview();
    }

    public static void deleteEvent(Data data, Event event) {
        data.getEvents().remove(event);
        App.updatePreview();
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
