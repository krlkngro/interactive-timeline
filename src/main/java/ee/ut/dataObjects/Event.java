package ee.ut.dataObjects;

public class Event {
    private int queueNr;
    private String label;
    private String htmlContent;
    private int maxHeight;
    private int width;
    private Boolean placedLeft;
    private Boolean packed;


    public Event() {
        //todo discuss reasonable default values, figure out a way to set defaults based on general settings
        this.htmlContent = "<p style=\"font-family: 'Rubik';\">Insert text here</p>";
        this.queueNr = 1;
        this.label = "";
        this.maxHeight = 200;
        this.width = 200;
        this.placedLeft = true;
        this.packed = false;
    }

    public Event(Boolean packed, int queueNr, String label, String htmlContent, int maxHeight, int width, Boolean placedLeft) {
        this.packed = packed;
        this.queueNr = queueNr;
        this.label = label;
        this.htmlContent = htmlContent;
        this.maxHeight = maxHeight;
        this.width = width;
        this.placedLeft = placedLeft;
    }


    public int getQueueNr() {
        return queueNr;
    }

    public void setQueueNr(int queueNr) {
        this.queueNr = queueNr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Boolean getPlacedLeft() {
        return placedLeft;
    }

    public void setPlacedLeft(Boolean placedLeft) {
        this.placedLeft = placedLeft;
    }

    public Boolean getPacked() { return packed; }

    public void setPacked(Boolean packed) { this.packed = packed; }
}
