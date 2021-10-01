package dataObjects;

import java.util.List;

public class Data {
    enum labelType {
        TEXT,
        LINE,
        DOT,
    }
    int eventSpace; // space between two events
    List<Event> events;
}
