module fse.filesearchengine {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.tika.core;
    requires org.apache.poi.poi;


    opens fse to javafx.fxml;
    exports fse;
}