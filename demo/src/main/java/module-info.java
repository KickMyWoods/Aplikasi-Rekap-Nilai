module com.rekap.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.rekap.demo to javafx.fxml;
    exports com.rekap.demo;
}