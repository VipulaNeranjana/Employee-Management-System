package lk.ijse.dep10.app.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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


    private int id;

    public void initialize() {

        /*Column Mapping*/
        tblActiveLeave.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        tblActiveLeave.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
        tblActiveLeave.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
        tblActiveLeave.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("leaveDuration"));
        tblActiveLeave.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));


        /*set leaveApplication window as a first window*/
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);

        loadActiveLeaveTable();

        tblActiveLeave.getSelectionModel().selectedItemProperty().addListener((value,previous,current) ->{
            btnRemove.setDisable(false);
            if (current == null) {
                btnRemove.setDisable(true);
            }
        });

    }

    public void init(SimpleStringProperty input){
            id = Integer.parseInt(input.getValue());
    }


    private void loadActiveLeaveTable() {
        Connection connection = DBConnection.getInstance().getConnection();
        Date sqlDate = Date.valueOf(LocalDate.now());
        try {

            PreparedStatement stm = connection.prepareStatement("select * from Leaves where leave_date >= ? ");
            stm.setDate(1, sqlDate);
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                String leaveType = resultSet.getString("leave_type");
                Date applyDate = resultSet.getDate("apply_date");
                Date leaveDate = resultSet.getDate("leave_date");
                String leaveDuration = resultSet.getString("leave_duration");
                String status = resultSet.getString("status");

                Leave leave = new Leave(leaveDate, applyDate, leaveType,leaveDuration, status);
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
        tblActiveLeave.getSelectionModel().clearSelection();

        loadLeaveSheet();

        txtSearch.textProperty().addListener((value,previous,current) -> {
            loadLeaveSheet();
        });


    }
    private void loadLeaveSheet(){

        tblLeaveSheet.getItems().clear();

        tblLeaveSheet.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("leaveType"));
        tblLeaveSheet.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("applyDate"));
        tblLeaveSheet.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("leaveDate"));
        tblLeaveSheet.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("leaveDuration"));
        tblLeaveSheet.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));

        Connection connection = DBConnection.getInstance().getConnection();
        try {

            PreparedStatement stm = connection.prepareStatement("select * from Leaves where (leave_type like ? or apply_date like ? or leave_date like ? or leave_duration like ?) and status != ? ");
            String query = "%" + txtSearch.getText() + "%";
            stm.setString(1, query);
            stm.setString(2, query);
            stm.setString(3, query);
            stm.setString(4, query);
            stm.setString(5, Status.PENDING.toString());
            ResultSet resultSet = stm.executeQuery();

            while (resultSet.next()) {
                String leaveType = resultSet.getString("leave_type");
                Date applyDate = resultSet.getDate("apply_date");
                Date leaveDate = resultSet.getDate("leave_date");
                String leaveDuration = resultSet.getString("leave_duration");
                String status = resultSet.getString("status");

                Leave leave = new Leave(leaveDate, applyDate, leaveType,leaveDuration, status);
                tblLeaveSheet.getItems().add(leave);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        Leave selectedItem = tblActiveLeave.getSelectionModel().getSelectedItem();
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            connection.setAutoCommit(false);

            PreparedStatement stm = connection.prepareStatement("delete from Leaves where id = ? and leave_date = ?");
            PreparedStatement stm1 = connection.prepareStatement("delete from Leave_Description where id = ? and leave_date = ?");

            if (selectedItem.getLeaveType().equals("OTHER")){
                stm1.setInt(1,id);
                stm1.setDate(2,selectedItem.getLeaveDate());
                stm1.executeUpdate();
            }
            stm.setInt(1,id);
            stm.setDate(2,selectedItem.getLeaveDate());
            stm.executeUpdate();

            connection.commit();

            tblActiveLeave.getItems().remove(selectedItem);
            tblActiveLeave.refresh();




        } catch (Throwable e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to remove the request!").showAndWait();

            throw new RuntimeException(e);
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
    public void btnBackOnAction(ActionEvent actionEvent) {
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);


    }


    public void btnPaySheetOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnRemove.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/PaySheetView.fxml"));
        AnchorPane root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Leave Window");
        stage.show();
        stage.centerOnScreen();

    }
}
