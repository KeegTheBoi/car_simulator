module com.carsim {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.carsim to javafx.fxml;
    exports com.carsim;
}
