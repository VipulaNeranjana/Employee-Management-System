package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.EmployeeAttendance;

import java.sql.*;

public class EditAttendanceViewController {
    public Button btnSave;
    EmployeeAttendance employeeAttendance;

    public void initData(EmployeeAttendance employeeAttendance, TableView<EmployeeAttendance>tblEmployeeAttendance) {
        this.employeeAttendance = employeeAttendance;
        this.tblEmployeeAttendance = tblEmployeeAttendance;

    }

    public void initialize() {
        Platform.runLater(()->{
            lblID.setText(employeeAttendance.getId() + "");
            lblName.setText(employeeAttendance.getName());
            lblDate.setText(employeeAttendance.getDate() + "");
            txtSignInTime.setText(employeeAttendance.getSignInTime() + "");
            txtSignOutTime.setText(employeeAttendance.getSignOutTime() + "");
        });
    }

    @FXML
    private Label lblDate;

    @FXML
    private Label lblID;

    @FXML
    private Label lblName;

    @FXML
    private TextField txtSignInTime;

    @FXML
    private TextField txtSignOutTime;

    private TableView<EmployeeAttendance> tblEmployeeAttendance;

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (txtSignOutTime.getText().isEmpty() || (!txtSignOutTime.getText().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}"))) {
            txtSignOutTime.requestFocus();
            return;
        }

        if (txtSignInTime.getText().isEmpty() || (!txtSignInTime.getText().matches("[0-9]{2}:[0-9]{2}:[0-9]{2}"))) {
            txtSignOutTime.requestFocus();
            return;
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Attendance SET in_time=?,off_time=? WHERE id=? AND date=?");

            preparedStatement.setInt(3, employeeAttendance.getId());
            preparedStatement.setDate(4, (Date) employeeAttendance.getDate());
            preparedStatement.setTime(1, Time.valueOf(txtSignInTime.getText()));
            preparedStatement.setTime(2, Time.valueOf(txtSignOutTime.getText()));
            preparedStatement.executeUpdate();

            System.out.println("Data Updated to database");

            tblEmployeeAttendance.refresh();

            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
