package fse;

public class Suggestion {
    private String name;
    private String timestamp;

    public Suggestion(String name, String timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
