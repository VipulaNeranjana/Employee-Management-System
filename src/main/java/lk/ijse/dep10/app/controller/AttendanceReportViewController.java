package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.EmployeeAttendanceReport;

import java.sql.*;
import java.time.LocalDate;

public class AttendanceReportViewController {

    public Button btnBack;
    @FXML
    private Button btnSubmit;

    @FXML
    private DatePicker dtFrom;

    @FXML
    private DatePicker dtTo;

    @FXML
    private Label lblName;

    @FXML
    private TableView<EmployeeAttendanceReport> tblAttendance;

    @FXML
    private TextField txtID;

    private Scene adminAttendanceScene;


    public void initData(Scene adminAttendanceScene) {
        this.adminAttendanceScene = adminAttendanceScene;

    }

    public void initialize() {
        tblAttendance.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblAttendance.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblAttendance.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("signInTime"));
        tblAttendance.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("signOutTime"));
        tblAttendance.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("workingTime"));

    }

    @FXML
    void btnSubmitOnAction(ActionEvent event) {
        if(!isEnteredDataValid())return;

        LocalDate from = dtFrom.getValue();
        LocalDate to = dtTo.getValue();
        int id = Integer.parseInt(txtID.getText());

        System.out.println("Data locad");

        loadDataToTable(from, to, id);

    }

    private void loadDataToTable(LocalDate from, LocalDate to,int id) {
        try {
            tblAttendance.getItems().clear();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm1 = connection.prepareStatement("SELECT *FROM Employee WHERE id=?");
            stm1.setInt(1, id);
            ResultSet resultSet = stm1.executeQuery();
            if (!resultSet.next()) {
                new Alert(Alert.AlertType.ERROR, "Invalid ID").showAndWait();
                return;
            }


            String name = resultSet.getString("name");
            lblName.setText(name);

            /*Load Data to Table*/

            for (LocalDate date=from; date.isBefore(to); date = date.plusDays(1)) {
                PreparedStatement stm2 = connection.prepareStatement("SELECT *FROM Attendance WHERE id=? AND date=?");
                stm2.setInt(1, id);
                stm2.setDate(2, Date.valueOf(date));
                ResultSet resultSet1 = stm2.executeQuery();
                if(!resultSet1.next())continue;

                int empId = resultSet1.getInt(1);
                Date attendanceDate = resultSet1.getDate(2);
                Time signInTime = resultSet1.getTime(3);
                Time signOutTime = resultSet1.getTime(4);

                Long durationMillis = Math.abs(signOutTime.getTime() - signInTime.getTime());
                long hours = durationMillis / (1000 * 60 * 60);
                long mints = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

                String duration = "H-"+hours + " : m" + mints;

                EmployeeAttendanceReport employeeAttendanceReport = new EmployeeAttendanceReport(empId, attendanceDate, signInTime, signOutTime, duration);
                tblAttendance.getItems().add(employeeAttendanceReport);

            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private boolean isEnteredDataValid() {
        boolean dataValid = true;

        if (dtFrom.getValue() == null) {
            dataValid = false;
        }
        if (dtTo.getValue() == null || (dtTo.getValue().isAfter(LocalDate.now()))) {
            dataValid = false;
        }
        if (txtID.getText().isEmpty() || txtID.getText().isBlank() || (!txtID.getText().matches("[0-9]+"))) {
            dataValid = false;
        }

        return dataValid;
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnBack.getScene().getWindow();

        stage.setScene(adminAttendanceScene);
        stage.show();

    }
}
