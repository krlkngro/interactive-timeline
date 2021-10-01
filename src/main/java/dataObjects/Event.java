package dataObjects;

public class Event {
    private Boolean hidden;
    private int queueNr;
    private String label;
    private String htmlContent;
    private int maxHeight;
    private int width;
    private Boolean placedLeft;

    public Event(Boolean hidden, int queueNr, String label, String htmlContent, int maxHeight, int width, Boolean placedLeft) {
        this.hidden = hidden;
        this.queueNr = queueNr;
        this.label = label;
        this.htmlContent = htmlContent;
        this.maxHeight = maxHeight;
        this.width = width;
        this.placedLeft = placedLeft;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
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
}
