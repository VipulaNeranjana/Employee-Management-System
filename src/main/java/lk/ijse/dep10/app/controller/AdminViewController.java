package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

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
    void btnEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) {
        Stage stage = (Stage) btnPayroll.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminPayrollView.fxml"))));
            stage.setTitle("Admin Payroll Management");
            stage.setMaximized(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
