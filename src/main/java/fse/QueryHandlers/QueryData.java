package fse.QueryHandlers;

public class QueryData {
    private String path;
    private String content;

    public QueryData(String path, String content) {
        this.path = path;
        this.content = content;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
