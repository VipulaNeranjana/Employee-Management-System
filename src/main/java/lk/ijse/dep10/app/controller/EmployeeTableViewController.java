package lk.ijse.dep10.app.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.model.EmployeeTable;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeTableViewController {

    public TextField txtSearch;
    @FXML
    private Button btnAttendance;

    @FXML
    private Button btnChange;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnRemove;

    @FXML
    private Label lblUserName;

    @FXML
    private TableView<EmployeeTable> tblEmployees;
    private ArrayList<EmployeeTable> employeeList;

    public void initialize(){
        tblEmployees.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tblEmployees.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblEmployees.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("contact"));

        loadTable();

        tblEmployees.getSelectionModel().selectedItemProperty().addListener((value, previous, current) -> {
            btnRemove.setDisable(current == null);
            btnChange.setDisable(current == null);

        });

        txtSearch.textProperty().addListener((value, previous, current) -> {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM Employee WHERE name LIKE CONCAT('%', ?, '%') OR id LIKE CONCAT('%', ?, '%')");
                statement.setString(1, current);
                statement.setString(2, current);
                ResultSet resultSet = statement.executeQuery();

                ObservableList<EmployeeTable> searchEmployeeList = tblEmployees.getItems();
                searchEmployeeList.clear();

                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String contact = resultSet.getString("contact");
                    EmployeeTable employee = new EmployeeTable(id, name, contact);
                    searchEmployeeList.add(employee);
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadTable() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Employee");

            employeeList = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contact = resultSet.getString("contact");

                EmployeeTable employee = new EmployeeTable(id, name, contact);
                employeeList.add(employee);
            }
            tblEmployees.getItems().addAll(employeeList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "couldn't load the table").show();
        }
    }
    @FXML
    void btnAttendanceOnAction(ActionEvent event) throws IOException {

    }

    @FXML
    void btnChangeOnAction(ActionEvent event) {

    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnNew.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/EmployeeView.fxml")).load()));
    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Employee WHERE id=?");
            EmployeeTable employee = tblEmployees.getSelectionModel().getSelectedItem();
            statement.setInt(1,employee.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"failed to delete employer, please try again");
            throw new RuntimeException(e);
        }
        tblEmployees.getItems().remove(tblEmployees.getSelectionModel().getSelectedItem());
    }

}
