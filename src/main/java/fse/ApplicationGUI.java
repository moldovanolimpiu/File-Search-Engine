package fse;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class ApplicationGUI extends Application {

    FileRepository fileRepo = new FileRepository();

    public ApplicationGUI() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox mainLayout = new VBox(10);
        mainLayout.setStyle("-fx-padding: 20;");
        mainLayout.setAlignment(Pos.TOP_CENTER);



        TextField searchBar = new TextField();
        searchBar.setMaxWidth(400);
        searchBar.setPromptText("Search filenames...");

        VBox resultsContainer = new VBox(5);
        ToggleGroup selectionGroup = new ToggleGroup();



        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {

            Task<List<FileMetadata>> searchTask = new Task<>() {
                @Override
                protected List<FileMetadata> call() throws Exception {
                    return fileRepo.searchFilename(newVal);
                }
            };


            searchTask.setOnSucceeded(e -> {
                List<FileMetadata> results = searchTask.getValue();

                resultsContainer.getChildren().clear();

                for(FileMetadata file : results) {
                    ToggleButton tb = new ToggleButton(file.getFileName());
                    tb.setToggleGroup(selectionGroup);
                    tb.setMaxWidth(700);
                    tb.getStyleClass().add("label-button");

                    resultsContainer.getChildren().add(tb);
                }

            });

            new Thread(searchTask).start();
        });
        ScrollPane scrollPane = new ScrollPane(resultsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setMaxHeight(200);

        mainLayout.getChildren().addAll(searchBar,scrollPane);

        Scene mainScene = new Scene(mainLayout, 700, 800);
        mainScene.getStylesheets().add(getClass().getResource("/fse/style.css").toExternalForm());

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
