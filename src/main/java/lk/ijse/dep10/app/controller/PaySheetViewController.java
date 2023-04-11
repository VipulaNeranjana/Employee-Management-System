package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

public class PaySheetViewController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPrint;

    @FXML
    private DatePicker txtSelectedDate;
    private int employeeId;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/UserView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("User Window");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) throws JRException {
        employeeId = 1;
        if (txtSelectedDate.getValue() == null){
            new Alert(Alert.AlertType.ERROR,"Please select a date. try again...!").showAndWait();
            return;
        }
        LocalDate localDate = txtSelectedDate.getValue();
        int year = localDate.getYear();
        Month month = localDate.getMonth();
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Payroll WHERE id = ?");
            stm.setInt(1,employeeId);
            ResultSet rst = stm.executeQuery();
            if (rst.next()){
                Date date = rst.getDate("date");
                LocalDate localDateDatabase = date.toLocalDate();
                int yearDatabase = localDateDatabase.getYear();
                Month monthDatabase = localDateDatabase.getMonth();
                if (year != yearDatabase || month != monthDatabase){
                    new Alert(Alert.AlertType.ERROR,"There is no such pay sheet which matches to selected year and month. try again...!").showAndWait();
                }
                else if (year == yearDatabase && month == monthDatabase){
                    String name = rst.getString("name");
                    int basicSalary = rst.getInt("basic_salary");
                    int overtimePayment = rst.getInt("overtime_payment");
                    int bonus = rst.getInt("bonus");
                    double tax = rst.getDouble("tax");
                    double epf = rst.getDouble("epf");
                    double etf = rst.getDouble("etf");
                    double unionFee = rst.getDouble("union_fee");

                    JasperDesign jasperDesign = JRXmlLoader.load(getClass().getResourceAsStream("/report/employee-pay-sheet-params.jrxml"));
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

                    HashMap<String, Object> reportParams = new HashMap<>();

                    reportParams.put("year", yearDatabase);
                    reportParams.put("month", monthDatabase.toString());
                    reportParams.put("id", Integer.toString(employeeId));
                    reportParams.put("name",name);
                    reportParams.put("basic_salary",String.format("%,.2f",(double) basicSalary));
                    reportParams.put("overtime_payment", String.format("%,.2f", (double)overtimePayment));
                    reportParams.put("bonus",String.format("%,.2f", (double)bonus));
                    reportParams.put("gross_pay",String.format("%,.2f", (double)(basicSalary + overtimePayment + bonus)));
                    reportParams.put("tax",String.format("%,.2f", tax));
                    reportParams.put("epf",String.format("%,.2f", epf));
                    reportParams.put("etf",String.format("%,.2f", etf));
                    reportParams.put("union_fee",String.format("%,.2f", unionFee));
                    reportParams.put("total_deductions",String.format("%,.2f", (tax + epf + etf + unionFee)));
                    reportParams.put("net_pay",String.format("%,.2f", (basicSalary + overtimePayment + bonus - tax - epf - etf - unionFee)));

                    JREmptyDataSource dataSource = new JREmptyDataSource(1);

                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, dataSource);
                    JasperViewer.viewReport(jasperPrint,false);
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to find employee, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
}

