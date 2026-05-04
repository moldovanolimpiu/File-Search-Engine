package fse;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class ApplicationGUI extends Application {

    FileRepository fileRepo = new FileRepository();
    FileCrawler fileCrawler = new FileCrawler();
    CrawlerReport report;
    public ApplicationGUI() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Label filesCrawlerReport = new Label("File Crawler Report:");
        Label filesFoundLabel = new Label("Files Found: ");
        Label filesInsertedLabel = new Label("Files Inserted: ");
        Label filesDeletedLabel = new Label("Files Deleted: ");
        Label filesUpdatedLabel = new Label("Files Updated: ");
        Label indexingStatusLabel = new Label("Indexing C:\\Info\\J3S2, Standby...");

        VBox reportBox = new VBox(10);
        reportBox.setAlignment(Pos.CENTER_LEFT);
        reportBox.getChildren().addAll(filesCrawlerReport, filesFoundLabel, filesInsertedLabel, filesDeletedLabel, filesUpdatedLabel, indexingStatusLabel);

        Task<CrawlerReport> crawlTask = new Task<>() {
            @Override
            protected CrawlerReport call() throws Exception {
                return fileCrawler.crawlFiles("C:\\Info\\J3S2");
            }
        };

        crawlTask.setOnSucceeded(e -> {
            CrawlerReport report = crawlTask.getValue();
            filesFoundLabel.setText("Files Found: " + report.getFilesFound());
            filesInsertedLabel.setText("Files Inserted: " + report.getFileInsertions());
            filesDeletedLabel.setText("Files Deleted: " + report.getFileDeletions());
            filesUpdatedLabel.setText("Files Updated: " + report.getFileUpdates());
            indexingStatusLabel.setText("Finished indexing");
        });

        new Thread(crawlTask).start();

        VBox mainLayout = new VBox(10);
        mainLayout.setStyle("-fx-padding: 20;");
        mainLayout.setAlignment(Pos.TOP_CENTER);


        Label directoryLabel = new Label("Directory:");
        Button indexButton = new Button("Index");
        TextField directoryBar = new TextField();
        directoryBar.setMaxWidth(500);
        directoryBar.setPromptText("Input Directory...");
        HBox.setHgrow(directoryBar, Priority.ALWAYS);
        HBox directoryBox = new HBox(10);
        directoryBox.setAlignment(Pos.CENTER);
        directoryBox.getChildren().addAll(directoryLabel, directoryBar, indexButton);


        indexButton.setOnAction(e -> {

            filesInsertedLabel.setText("Files Inserted: ");
            filesFoundLabel.setText("Files Found: ");
            filesDeletedLabel.setText("Files Deleted: ");
            filesUpdatedLabel.setText("Files Updated: ");

            System.out.println(directoryBar.getText());
            if(isValidPath(directoryBar.getText())) {
                indexingStatusLabel.setText("Indexing " + directoryBar.getText() + ", Standby...");
                Task<CrawlerReport> crawlLiveUpdate = new Task<>() {
                    @Override
                    protected CrawlerReport call() throws Exception {
                        return fileCrawler.crawlFiles(directoryBar.getText());
                    }
                };


                crawlLiveUpdate.setOnSucceeded(clu -> {
                    CrawlerReport report = crawlLiveUpdate.getValue();
                    filesFoundLabel.setText("Files Found: " + report.getFilesFound());
                    filesInsertedLabel.setText("Files Inserted: " + report.getFileInsertions());
                    filesDeletedLabel.setText("Files Deleted: " + report.getFileDeletions());
                    filesUpdatedLabel.setText("Files Updated: " + report.getFileUpdates());
                    indexingStatusLabel.setText("Finished indexing");
                });
                new Thread(crawlLiveUpdate).start();
            }else{
                System.out.println("Invalid Path");
                indexingStatusLabel.setText("Invalid Path");
            }


        });




        TextField searchBar = new TextField();
        searchBar.setMaxWidth(500);
        searchBar.setPromptText("Search...");

        Label searchLabel = new Label("Search:");

        ComboBox<String> rankingBox = new ComboBox<>();
        rankingBox.getItems().addAll(
                "Path Depth",
                "Alphabetical",
                "Date accessed"
        );

        rankingBox.setValue("Path Depth");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(searchBar, Priority.ALWAYS);
        searchBox.getChildren().addAll(searchLabel, searchBar,rankingBox);

        Label fileNameLabel = new Label("");
        Label filePathLabel = new Label("");
        Label fileExtensionLabel = new Label("");
        Label fileSizeLabel = new Label("");
        Label fileHashLabel = new Label("");
        Label fileContentLabel = new Label("");
        fileContentLabel.setMaxWidth(700);
        fileContentLabel.setWrapText(true);
        Label fileDateCreatedLabel = new Label("");
        Label fileDateModifiedLabel = new Label("");

        VBox resultBox = new VBox(10);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        resultBox.getChildren().addAll(fileNameLabel, filePathLabel,fileExtensionLabel,fileSizeLabel,fileHashLabel,fileDateCreatedLabel,fileDateModifiedLabel,fileContentLabel);

        VBox resultsContainer = new VBox(5);
        ToggleGroup selectionGroup = new ToggleGroup();

        selectionGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null){
                FileMetadata data = (FileMetadata) newValue.getUserData();

                fileNameLabel.setText("File name: " + data.getFileName());
                filePathLabel.setText("Path: " + data.getPath());
                fileExtensionLabel.setText("Extension: " + data.getFileExtension());
                fileSizeLabel.setText("Size: " + sizeFormat(data.getFileSize()));
                fileHashLabel.setText("Hash: " + data.getHash());
                fileDateCreatedLabel.setText("Date created: " + data.getDatecreated());
                fileDateModifiedLabel.setText("Date modified: " + data.getDatemodified());
                fileContentLabel.setText("Content:\n " + contentPreview(data.getContent()));


            }
        });

        rankingBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            searchBar.setText(searchBar.getText());
        });



        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {

            Task<List<FileMetadata>> searchTask = new Task<>() {
                @Override
                protected List<FileMetadata> call() throws Exception {
                    return fileRepo.searchCompound(newVal);

                }
            };


            searchTask.setOnSucceeded(e -> {
                List<FileMetadata> results = searchTask.getValue();

                String selected = rankingBox.getValue();

                switch(selected){
                    case "Path Depth":
                        results.sort(Comparator.comparingInt(FileMetadata::getRank));
                        break;
                    case "Alphabetical":
                        results.sort(Comparator.comparing(FileMetadata::getFileName));
                        break;
                    case "Date accessed":
                        results.sort(Comparator.comparing(FileMetadata::getDateccessed).reversed());
                        break;
                }

                resultsContainer.getChildren().clear();

                for(FileMetadata file : results) {
                    ToggleButton tb = new ToggleButton(file.getPath());
                    tb.setToggleGroup(selectionGroup);
                    tb.setMaxWidth(700);
                    tb.getStyleClass().add("label-button");
                    tb.setUserData(file);

                    resultsContainer.getChildren().add(tb);
                }

            });

            new Thread(searchTask).start();
        });
        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setMaxHeight(200);

        mainLayout.getChildren().addAll(reportBox, directoryBox, searchBox,scrollPane, resultBox);

        Scene mainScene = new Scene(mainLayout, 900, 800);
        mainScene.getStylesheets().add(getClass().getResource("/fse/style.css").toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private String sizeFormat(long size) {

        //long var = 0;
        String appendage;
        int count = 0;
        while(size > 1023){
            size =size/1024;
            count++;
        }
        if(count == 0){
            appendage = size + " B";
        }else if(count == 1){
            appendage = size + " KB";
        }else if(count == 2){
            appendage = size + " MB";
        }else if(count == 3){
            appendage = size + " GB";
        }else{
            appendage = size + " TB";
        }
        return appendage;

    }

    private StringBuilder contentPreview(String content){
        StringBuilder sb = new StringBuilder();
        String[] contentArray = content.split("\n");
        int size = Math.min(contentArray.length, 5);
        for(int i = 0; i < size; i++){
            sb.append(contentArray[i]);
            sb.append("\n");
        }

        return sb;
    }

    private static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }
}
