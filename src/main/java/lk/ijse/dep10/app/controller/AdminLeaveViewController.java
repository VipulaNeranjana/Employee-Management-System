package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import java.sql.*;
import java.time.LocalDate;

public class AdminLeaveViewController {

    public Button btnViewHistory;
    @FXML
    private Button btnApprove;

    @FXML
    private Button btnReject;

    @FXML
    private TableView<Employee> tblLeaveApprove;


    public  void initialize(){
      tblLeaveApprove.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
      tblLeaveApprove.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
      tblLeaveApprove.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
      tblLeaveApprove.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
      tblLeaveApprove.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
      tblLeaveApprove.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("status"));

      loadAllLeaveRequest();

      tblLeaveApprove.getColumns().get(2).getStyleClass().add("center");
    }

    private void loadAllLeaveRequest(){
        tblLeaveApprove.getItems().clear();
        try {
            Connection connection= DBConnection.getInstance().getConnection();
            String sql="select * from Leaves where leave_date>=? ";
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

                Employee employee=new Employee(id,null,leaveType,applyDate,leaveDate,status);
                stm2.setInt(1,id);
                ResultSet rstEmployeeList = stm2.executeQuery();
                while (rstEmployeeList.next()){
                    String name = rstEmployeeList.getString("name");
                    employee.setName(name);
                }
                tblLeaveApprove.getItems().add(employee);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Can not load leave requests, please try again");
            e.printStackTrace();
        }


    }

    @FXML
    void btnApproveOnAction(ActionEvent event) {
        Employee selectedLeave = tblLeaveApprove.getSelectionModel().getSelectedItem();
        selectedLeave.setStatus(Employee.Status.APPROVED);
        tblLeaveApprove.refresh();
        try {
            Connection connection=DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql="update Leaves set status='APPROVED' where leave_date=? and id=?" ;
            PreparedStatement stm = connection.prepareStatement(sql);
            Date leaveDate = Date.valueOf(selectedLeave.getLeaveDate());
            int id = selectedLeave.getId();
            stm.setDate(1,leaveDate);
            stm.setInt(2,id);
            stm.executeUpdate();
            connection.commit();

        } catch (Throwable e) {
            try {
                DBConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnRejectOnAction(ActionEvent event) {
        Employee selectedLeave = tblLeaveApprove.getSelectionModel().getSelectedItem();
        selectedLeave.setStatus(Employee.Status.REJECTED);
        tblLeaveApprove.refresh();
        try {

            Connection connection=DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql="update Leaves set status='REJECTED' where leave_date=? and id=?" ;
            PreparedStatement stm = connection.prepareStatement(sql);
            Date leaveDate = Date.valueOf(selectedLeave.getLeaveDate());
            int id = selectedLeave.getId();
            stm.setDate(1,leaveDate);
            stm.setInt(2,id);
            stm.executeUpdate();
            connection.commit();
        } catch (Throwable e) {
            try {
                DBConnection.getInstance().getConnection().rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }finally {
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnViewHistoryOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=new Stage();
        FXMLLoader fxmlLoader=new FXMLLoader(this.getClass().getResource("/view/LeaveReportView.fxml"));
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.setTitle("View History");
        stage.centerOnScreen();
        stage.show();
    }
}
