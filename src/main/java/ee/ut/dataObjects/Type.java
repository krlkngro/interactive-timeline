package ee.ut.dataObjects;

public enum Type {
    TEXT,
    LINE,
    DOT;

    @Override
    public String toString() {
        switch (this) {
            case TEXT:
                return "text";

            case LINE:
                return "line";

            case DOT:
                return "dot";

            default:
                return null;
        }
    }
}



