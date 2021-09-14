module com.example.loanserver2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.loanserver2 to javafx.fxml;
    exports com.example.loanserver2;
}