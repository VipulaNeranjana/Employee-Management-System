package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.EmployeeAttendance;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.Date;

public class AttendanceListViewController {

    @FXML
    private Button btnAddAttendance;

    @FXML
    private Button btnBack;

    @FXML
    private DatePicker dtDatePicker;

    @FXML
    private TableView<EmployeeAttendance> tblEmployeeAttendance;

    @FXML
    private TextField txtSearch;

    private Scene adminAttendanceScene;

    public void initialize() {

        Platform.runLater(()->{
            btnAddAttendance.setDisable(true);

        });

        tblEmployeeAttendance.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployeeAttendance.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployeeAttendance.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblEmployeeAttendance.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("signInTime"));
        tblEmployeeAttendance.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("signOutTime"));
        tblEmployeeAttendance.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("workingTime"));

        loadAllAttendance();

        txtSearch.textProperty().addListener((ov,previous,current)->{
            if ((!current.matches("[0-9]+"))) {
                new Alert(Alert.AlertType.ERROR, "Enter Employee ID Only").showAndWait();
                return;
            }

            if (current.isEmpty() || current.isBlank()) {
                return;
            }
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                System.out.println(current);
                PreparedStatement stmStudent = connection.prepareStatement("SELECT *FROM Attendance WHERE id  =?");
                stmStudent.setInt(1, Integer.parseInt( current ));

                ResultSet resultSet = stmStudent.executeQuery();

                ObservableList<EmployeeAttendance> attendanceList = tblEmployeeAttendance.getItems();
                attendanceList.clear();

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Date date = resultSet.getDate(2);
                    Time signInTime = resultSet.getTime(3);
                    Time signOutTime = resultSet.getTime(4);

                    Long durationMillis = Math.abs(signOutTime.getTime() - signInTime.getTime());
                    long hours = durationMillis / (1000 * 60 * 60);
                    long mints = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

                    String duration = "H-"+hours + " : m" + mints;

                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM Employee WHERE id=?");
                    preparedStatement.setInt(1, id);
                    ResultSet rst = preparedStatement.executeQuery();
                    rst.next();
                    String name = rst.getString(3);


                    EmployeeAttendance employeeAttendance = new EmployeeAttendance(name, id, date, signInTime, signOutTime, duration);
                    attendanceList.add(employeeAttendance);


                }


            } catch (SQLException e) {
                e.printStackTrace();

            }
        });

        dtDatePicker.valueProperty().addListener((pv,value,current)->{

            ObservableList<EmployeeAttendance> attendanceList = tblEmployeeAttendance.getItems();
            attendanceList.clear();
            if(current==null) return;
            LocalDate today = LocalDate.now();

            if (current.isAfter(today)) {
                new Alert(Alert.AlertType.ERROR, "Enter Valid Date!").showAndWait();
                return;
            }

            try {
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("SELECT *FROM Attendance WHERE date=?");
                stm.setDate(1, java.sql.Date.valueOf(current));
                ResultSet resultSet = stm.executeQuery();

                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    Date date = resultSet.getDate(2);
                    Time signInTime = resultSet.getTime(3);
                    Time signOutTime = resultSet.getTime(4);

                    Long durationMillis = Math.abs(signOutTime.getTime() - signInTime.getTime());
                    long hours = durationMillis / (1000 * 60 * 60);
                    long mints = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

                    String duration = "H-"+hours + " : m" + mints;

                    PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM Employee WHERE id=?");
                    preparedStatement.setInt(1, id);
                    ResultSet rst = preparedStatement.executeQuery();
                    rst.next();
                    String name = rst.getString(3);


                    EmployeeAttendance employeeAttendance = new EmployeeAttendance(name, id, date, signInTime, signOutTime, duration);
                    attendanceList.add(employeeAttendance);

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        });

        tblEmployeeAttendance.getSelectionModel().selectedItemProperty().addListener((pv,value,current)->{
            if (current == null) {
                btnAddAttendance.setDisable(true);
                return;
            }
            btnAddAttendance.setDisable(false);

        });

    }

    private void loadAllAttendance() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM Attendance");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                Date date = resultSet.getDate(2);
                Time signInTime = resultSet.getTime(3);
                Time signOutTime = resultSet.getTime(4);

                Long durationMillis = Math.abs(signOutTime.getTime() - signInTime.getTime());
                long hours = durationMillis / (1000 * 60 * 60);
                long mints = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

                String duration = "H-"+hours + " : m" + mints;

                PreparedStatement stm = connection.prepareStatement("SELECT *FROM Employee WHERE id=?");
                stm.setInt(1, id);
                ResultSet rst = stm.executeQuery();
                rst.next();
                String name = rst.getString(3);

                EmployeeAttendance employeeAttendance = new EmployeeAttendance(name, id, date, signInTime, signOutTime, duration);
                tblEmployeeAttendance.getItems().add(employeeAttendance);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initData(Scene adminAttendanceScene) {
        this.adminAttendanceScene = adminAttendanceScene;

    }

    @FXML
    void btnAddAttendance(ActionEvent event) throws IOException {
        EmployeeAttendance selectedEmployee = tblEmployeeAttendance.getSelectionModel().getSelectedItem();

        Stage stage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/EditAttendanceView.fxml"));

        AnchorPane root = fxmlLoader.load();

        EditAttendanceViewController  ctrl = fxmlLoader.getController();
        ctrl.initData(selectedEmployee,tblEmployeeAttendance);

        stage.setTitle("Edit Attendance");
        stage.setScene(new Scene(root));
        stage.show();
        stage.sizeToScene();

    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) btnAddAttendance.getScene().getWindow();
        stage.setScene(adminAttendanceScene);

    }

}
