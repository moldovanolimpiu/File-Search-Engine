package fse;

public class CrawlerReport {
    private int filesFound;
    private int fileInsertions;
    private int fileDeletions;
    private int fileUpdates;

    public CrawlerReport(int filesFound, int fileInsertions, int fileDeletions, int fileUpdates) {

        this.filesFound = filesFound;
        this.fileInsertions = fileInsertions;
        this.fileDeletions = fileDeletions;
        this.fileUpdates = fileUpdates;
    }
}
