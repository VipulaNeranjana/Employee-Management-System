package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    void btnAttendanceOnAction(ActionEvent event) throws IOException {
        /*Load attendance view for admin, with loading pass admin main view to attendance view to
        * come back when back button pressed*/

        Stage stage = (Stage) btnAttendance.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminAttendanceView.fxml"));

        AnchorPane root = fxmlLoader.load();

        AdminAttendanceViewController ctrl = fxmlLoader.getController();
        ctrl.initData(btnAttendance.getScene());

        stage.setTitle("Admin Attendance View");
        stage.setScene(new Scene(root));
        stage.sizeToScene();


    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) {

    }

}
