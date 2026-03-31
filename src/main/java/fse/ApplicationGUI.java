package fse;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

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
        mainLayout.setAlignment(Pos.CENTER);



        TextField searchBar = new TextField();
        searchBar.setMaxWidth(150);
        searchBar.setPromptText("Search filenames...");

        Label first = new Label("");
        Label second = new Label("");
        Label third = new Label("");

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {

            Task<List<String>> searchTask = new Task<>() {
                @Override
                protected List<String> call() throws Exception {
                    return fileRepo.searchFilename(newVal);
                }
            };


            searchTask.setOnSucceeded(e -> {
                //updateResultsTable(searchTask.getValue());
                List<String> results = searchTask.getValue();
                if(!results.isEmpty()){
                    first.setText(results.getFirst());
                }else{
                    first.setText("");
                }
                if (results.size() > 1) {
                    second.setText(results.get(1));
                }else{
                    second.setText("");
                }
                if (results.size() > 2) {
                    third.setText(results.get(2));
                }else{
                    third.setText("");
                }
            });

            new Thread(searchTask).start();
        });

        mainLayout.getChildren().addAll(searchBar,first,second,third);

        Scene mainScene = new Scene(mainLayout, 700, 800);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
