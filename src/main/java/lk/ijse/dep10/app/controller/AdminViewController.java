package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class AdminViewController {

    @FXML
    private Button btnAttendance;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnPayroll;

    @FXML
    void btnAttendanceOnAction(ActionEvent event) {

    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {

    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) throws IOException {
        Stage stage=(Stage) btnLeave.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("/view/AdminLeaveView.fxml");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("Admin Leave");
        stage.setMaximized(true);
        stage.show();

    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) throws IOException {

    }

}
