package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.Leave;
import lk.ijse.dep10.app.util.LeaveType;
import lk.ijse.dep10.app.util.Status;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class UserLeaveController {

    public AnchorPane leaveSheet;
    public AnchorPane leaveApplication;
    public Button btnHome;
    public Button btnBack;
    public TableView<Leave> tblLeaveSheet;
    public TableView<Leave> tblActiveLeave;
    @FXML
    private Button btnAddApplication;

    @FXML
    private Button btnLeaveSheet;

    @FXML
    private Button btnRemove;

    @FXML
    private TextField txtSearch;

    //TODO should add id here

    private int id=123;

    public void initialize() {

        /*Column Mapping*/
        tblActiveLeave.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        tblActiveLeave.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
        tblActiveLeave.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
        tblActiveLeave.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));






        /*set leaveApplication window as a first window*/
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);

        loadActiveLeaveTable();
    }

    private void loadActiveLeaveTable() {
        Connection connection = DBConnection.getInstance().getConnection();
        Date sqlDate = Date.valueOf(LocalDate.now());
        try {

            PreparedStatement stm = connection.prepareStatement("select * from Leaves where leave_date >= ? ");
            stm.setDate(1,sqlDate);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                String leaveType = resultSet.getString("leave_type");
                Date applyDate = resultSet.getDate("apply_date");
                Date leaveDate = resultSet.getDate("leave_date");
                String status = resultSet.getString("status");

                Leave leave = new Leave(leaveDate, applyDate, leaveType, status);
                tblActiveLeave.getItems().add(leave);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void btnAddApplicationOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/LeaveApplicationView.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) btnAddApplication.getScene().getWindow());
        stage.setTitle("Leave Application");
        stage.show();
        stage.centerOnScreen();


    }

    @FXML
    void btnLeaveSheetOnAction(ActionEvent event) {
        leaveApplication.setVisible(false);
        leaveSheet.setVisible(true);

        tblLeaveSheet.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        tblLeaveSheet.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
        tblLeaveSheet.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
        tblLeaveSheet.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("status"));

        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement stm = connection.prepareStatement("select * from Leaves where id = ? and status != ?");
            stm.setInt(1,id);
            stm.setString(2, Status.PENDING.toString());
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()){
                String leaveType = resultSet.getString("leave_type");
                Date applyDate = resultSet.getDate("apply_date");
                Date leaveDate = resultSet.getDate("leave_date");
                String status = resultSet.getString("status");

                Leave leave = new Leave(leaveDate, applyDate, leaveType, status);
                tblLeaveSheet.getItems().add(leave);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }




    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {

    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnAddApplication.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/UserView.fxml"));
        AnchorPane root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("User Window");
        stage.show();
        stage.centerOnScreen();

    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);


    }
}
