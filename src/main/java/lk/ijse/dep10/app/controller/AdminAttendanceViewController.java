package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class AdminAttendanceViewController {

    public Button btnAttendanceList;
    public Button btnAttendanceReport;
    public Label lblTotalWorkers;

    public Label lblDate;
    public Label lblTime;
    public Label lblLeaves;
    public Label lblActive;
    private Scene adminView;

    @FXML
    private Button btnBack;

    public void initialize() {
        Thread clock = new Thread() {
            public void run() {
                for (; ; ) {
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    Calendar cal = Calendar.getInstance();

                    int second = cal.get(Calendar.SECOND);
                    int minute = cal.get(Calendar.MINUTE);
                    int hour = cal.get(Calendar.HOUR);
                    int i = cal.get(Calendar.AM_PM);

                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    //System.out.println(hour + ":" + (minute) + ":" + second);

                    Platform.runLater(() -> {
                        lblTime.setText(hour + ":" + (minute) + ":" + second + " " + ((i == 1) ? "PM" : "AM"));
                        lblDate.setText(year + "/" + month + "/" + day);

                    });


                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        return;
                    }
                }
            }
        };
        clock.start();

        Platform.runLater(() -> {
            lblTotalWorkers.setText(loadNumberOfActiveWorkers() + "");
            lblActive.setText(loadNumberOfAttendances() + "");
            lblLeaves.setText((loadNumberOfActiveWorkers() - loadNumberOfAttendances()) + "");

        });
    }


    /*Load number of Active workers for summary dashboard*/
    public int loadNumberOfActiveWorkers() {
        int numOfWorkers = 0;

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM Employee WHERE status=?");
            preparedStatement.setString(1, "ACTIVE");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numOfWorkers += 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return numOfWorkers;

    }

    /*Load number of attendance  on current day*/
    public int loadNumberOfAttendances() {
        int numOfAttendance = 0;

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT *FROM Attendance WHERE date=?");
            preparedStatement.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                numOfAttendance += 1;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numOfAttendance;
    }


    public void initData(Scene adminView) {
        this.adminView = adminView;
    }

    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.setScene(adminView);

    }

    public void btnAttendanceListOnAction(ActionEvent actionEvent) throws IOException {

       /*Load view attendance scene*/

        Stage stage = (Stage) btnAttendanceList.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AttendanceListView.fxml"));

        AnchorPane root = fxmlLoader.load();

        AttendanceListViewController ctrl = fxmlLoader.getController();
        ctrl.initData(btnAttendanceList.getScene());

        stage.setTitle("Admin Attendance List");
        stage.setScene(new Scene(root));
        stage.sizeToScene();

    }

    public void btnAttendanceReportOnAction(ActionEvent actionEvent) {

    }
}
