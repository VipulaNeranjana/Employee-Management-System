package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Employee;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

public class LeaveReportViewController {

    public Button btnAttendance;
    public Button btnEmployee;
    public Button btnLeave;
    public Button btnPayroll;
    @FXML
    private Button btnCancel;

    @FXML
    private TableView<Employee> tblLeaveApprove;
    public void initialize(){
        tblLeaveApprove.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblLeaveApprove.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblLeaveApprove.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        tblLeaveApprove.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
        tblLeaveApprove.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
        tblLeaveApprove.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("leaveDuration"));
        tblLeaveApprove.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAllLeaveRequest();
        tblLeaveApprove.getColumns().get(2).getStyleClass().add("center");
        tblLeaveApprove.getColumns().get(3).getStyleClass().add("center");
        tblLeaveApprove.getColumns().get(4).getStyleClass().add("center");
        tblLeaveApprove.getColumns().get(5).getStyleClass().add("center");
        tblLeaveApprove.getColumns().get(6).getStyleClass().add("center");
    }

    private void loadAllLeaveRequest() {
        tblLeaveApprove.getItems().clear();
        try {
            Connection connection= DBConnection.getInstance().getConnection();
            String sql="select * from Leaves where leave_date<? ";
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rstLeaveList = stm.executeQuery();

            // retrieve name using id
            PreparedStatement stm2 = connection.prepareStatement("select * from Employee where id=?");
            while (rstLeaveList.next()){
                int id = rstLeaveList.getInt("id");
                LocalDate leaveDate = rstLeaveList.getDate("leave_date").toLocalDate();
                LocalDate applyDate = rstLeaveList.getDate("apply_date").toLocalDate();
                Employee.Status status = Employee.Status.valueOf(rstLeaveList.getString("status"));
                Employee.LeaveType leaveType = Employee.LeaveType.valueOf(rstLeaveList.getString("leave_type"));
                Employee.LeaveDuration leaveDuration=Employee.LeaveDuration.valueOf(rstLeaveList.getString("leave_duration"));

                Employee employee=new Employee(id,null,leaveType,applyDate,leaveDate,leaveDuration,status);
                stm2.setInt(1,id);
                ResultSet rstEmployeeList = stm2.executeQuery();
                while (rstEmployeeList.next()){
                    String name = rstEmployeeList.getString("name");
                    employee.setName(name);
                }
                tblLeaveApprove.getItems().add(employee);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Can not load leave history, please try again");
            e.printStackTrace();
        }
    }
    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) btnEmployee.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("#");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("#");
        stage.setMaximized(true);
        stage.show();
    }

    public void btnAttendanceOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) btnAttendance.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("#");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("#");
        stage.setMaximized(true);
        stage.show();
    }

    public void btnLeaveOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) btnLeave.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("#");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("#");
        stage.setMaximized(true);
        stage.show();
    }

    public void btnPayrollOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) btnPayroll.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("#");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("#");
        stage.setMaximized(true);
        stage.show();
    }
}
