package lk.ijse.dep10.app.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.EmployeeAttendanceReport;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

public class AttendanceReportViewController {

    public Button btnBack;
    public Button btnGenerateReport;
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










        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement("SELECT *FROM Attendance WHERE id=?");
            stm.setInt(1,id);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()){

                Long durationMillis = Math.abs(resultSet.getTime("off_time").getTime() - resultSet.getTime("in_time").getTime());
                long hours = durationMillis / (1000 * 60 * 60);
                long mints = (durationMillis % (1000 * 60 * 60)) / (1000 * 60);

                String duration = "H-"+hours + " : m" + mints;

                LocalDate dataBaseDate = resultSet.getDate("date").toLocalDate();
                if (dataBaseDate.isEqual(to)) {
                    EmployeeAttendanceReport empReport = new EmployeeAttendanceReport(id, resultSet.getDate("date"), resultSet.getTime("in_time"), resultSet.getTime("off_time"), duration);

                    tblAttendance.getItems().add(empReport);

                }if(dataBaseDate.isAfter(from)&&dataBaseDate.isBefore(to)){
                    EmployeeAttendanceReport empReport = new EmployeeAttendanceReport(id, resultSet.getDate("date"), resultSet.getTime("in_time"), resultSet.getTime("off_time"), duration);

                    tblAttendance.getItems().add(empReport);

                }if(dataBaseDate.isEqual(to)){
                    EmployeeAttendanceReport empReport = new EmployeeAttendanceReport(id, resultSet.getDate("date"), resultSet.getTime("in_time"), resultSet.getTime("off_time"), duration);

                    tblAttendance.getItems().add(empReport);
                }

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

    public void btnGenerateReportOnAction(ActionEvent actionEvent) throws JRException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("DROP TABLE IF EXISTS Temp_Attendance_Report");
            preparedStatement1.execute();
            PreparedStatement stm = connection.prepareStatement("CREATE TABLE Temp_Attendance_Report(" +
                    "id INT NOT NULL ," +
                    "date DATE PRIMARY KEY ," +
                    "in_time TIME NOT NULL ," +
                    "out_time TIME NOT NULL," +
                    "duration VARCHAR(300) NOT NULL )");
            stm.execute();

            ObservableList<EmployeeAttendanceReport> reports = tblAttendance.getItems();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Temp_Attendance_Report (id,date,in_time,out_time,duration) VALUES (?,?,?,?,?)");

            for (EmployeeAttendanceReport employee : reports) {
                preparedStatement.setInt(1,employee.getId());
                preparedStatement.setDate(2,Date.valueOf(employee.getDate().toString()));
                preparedStatement.setTime(3,employee.getSignInTime());
                preparedStatement.setTime(4,employee.getSignOutTime());
                preparedStatement.setString(5,employee.getWorkingTime());
                preparedStatement.executeUpdate();
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JasperDesign design = JRXmlLoader.load(getClass().getResourceAsStream("/report/attendace_report.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(design);
        Connection dataSource = DBConnection.getInstance().getConnection();
        HashMap<String, Object> reportParams = new HashMap<>();

        reportParams.put("employee_name", lblName.getText());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource);
        JasperViewer.viewReport(jasperPrint,false);


    }
}
