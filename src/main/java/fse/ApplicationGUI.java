package fse;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public class ApplicationGUI extends Application {

    FileRepository fileRepo = new FileRepository();
    FileCrawler fileCrawler = new FileCrawler();

    public ApplicationGUI() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        new Thread(() -> {
            try {
                fileCrawler.crawlFiles("C:\\Info\\J3S2\\SD\\testdir");
                System.out.println("Scan finished");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).start();

        VBox mainLayout = new VBox(10);
        mainLayout.setStyle("-fx-padding: 20;");
        mainLayout.setAlignment(Pos.TOP_CENTER);




        RadioButton nameRadio = new RadioButton("Search by Filename");
        RadioButton contentRadio = new RadioButton("Search by Content");

        ToggleGroup searchGroup = new ToggleGroup();
        nameRadio.setToggleGroup(searchGroup);
        contentRadio.setToggleGroup(searchGroup);

        nameRadio.setSelected(true);

        HBox radioBox = new HBox(15, nameRadio, contentRadio);
        radioBox.setAlignment(Pos.CENTER);

        TextField searchBar = new TextField();
        searchBar.setMaxWidth(400);
        searchBar.setPromptText("Search filenames...");

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



        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            RadioButton selectedRadio = (RadioButton) searchGroup.getSelectedToggle();
            String mode = selectedRadio.getText();
            boolean isContent = mode.contains("Content");
            Task<List<FileMetadata>> searchTask = new Task<>() {
                @Override
                protected List<FileMetadata> call() throws Exception {
                    if(isContent){
                        return fileRepo.searchContent(newVal);
                    }else{
                        return fileRepo.searchFilename(newVal);
                    }

                }
            };


            searchTask.setOnSucceeded(e -> {
                List<FileMetadata> results = searchTask.getValue();

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

        mainLayout.getChildren().addAll(radioBox, searchBar,scrollPane, resultBox);

        Scene mainScene = new Scene(mainLayout, 700, 800);
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
}
