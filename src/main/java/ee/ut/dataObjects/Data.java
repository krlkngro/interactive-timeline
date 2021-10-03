package ee.ut.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private Type labelType;
    private int eventSpace; // space between two events
    private List<Event> events;

    public Data() {
        // todo discuss reasonable default values
        this.labelType = Type.LINE;
        this.eventSpace = 200;
        this.events = new ArrayList<>();
    }

    public Data(Type labelType, int eventSpace, List<Event> events) {
        this.labelType = labelType;
        this.eventSpace = eventSpace;
        this.events = events;
    }

    public Type getLabelType() {
        return labelType;
    }

    public void setLabelType(Type labelType) {
        this.labelType = labelType;
    }

    public int getEventSpace() {
        return eventSpace;
    }

    public void setEventSpace(int eventSpace) {
        this.eventSpace = eventSpace;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    //Add a single event to events list
    public void addEvent(int index, Event event){
        events.add(index, event);
    }
}
