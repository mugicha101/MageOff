module com.example.mageoff {
    requires javafx.controls;
    requires javafx.fxml;


    opens game.mageoff to javafx.fxml;
    exports game.mageoff;
}