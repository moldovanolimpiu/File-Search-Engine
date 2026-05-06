module fse.filesearchengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.tika.core;
    requires org.apache.poi.poi;


    opens fse to javafx.fxml;
    exports fse.Repositories;
    opens fse.Repositories to javafx.fxml;
    exports fse.SuggestionObserverPattern;
    opens fse.SuggestionObserverPattern to javafx.fxml;
    exports fse.GUI;
    opens fse.GUI to javafx.fxml;
    exports fse.QueryHandlers;
    opens fse.QueryHandlers to javafx.fxml;
    exports fse.FileData;
    opens fse.FileData to javafx.fxml;
}