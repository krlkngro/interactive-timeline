package ee.ut.dataObjects;

public class Event {
    private int queueNr;
    private String label;
    private String htmlContent;
    private Boolean packed;


    public Event() {
        //todo discuss reasonable default values, figure out a way to set defaults based on general settings
        this.htmlContent = "<p style=\"font-family: 'Rubik';\">Insert text here</p>";
        this.queueNr = 1;
        this.label = "";
        this.packed = false;
    }

    public Event(Boolean packed, int queueNr, String label, String htmlContent, int maxHeight, int width, Boolean placedLeft) {
        this.packed = packed;
        this.queueNr = queueNr;
        this.label = label;
        this.htmlContent = htmlContent;
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

    public String getText() {
        //todo figure out better replacements
        String strippedText = htmlContent.replaceAll("<.*?>", "\n")
                .replaceAll("\n\n+", "\n")
                .strip()
                .split("\n")[0];
        return strippedText.substring(0, Math.min(50, strippedText.length()));
    }

    public Boolean getPacked() { return packed; }

    public void setPacked(Boolean packed) { this.packed = packed; }
}
