package ee.ut.dataObjects;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private Type labelType;
    private int eventSpace; // space between two events
    private List<Event> events;

    public Data() {
        // todo discuss reasonable default values
        this.labelType = Type.TEXT;
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

    public void setLabelType(String labelType) {
        switch(labelType){
            case "Tekst":
                this.labelType = Type.TEXT;
                break;
            case "Joon":
                this.labelType = Type.LINE;
                break;
            case "Punkt":
                this.labelType = Type.DOT;
                break;
            default:
                this.labelType = Type.LINE;
                break;
        }
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
