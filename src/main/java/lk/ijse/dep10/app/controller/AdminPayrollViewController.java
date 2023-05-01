package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.model.Payroll;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AdminPayrollViewController {
    public Button btnGenerate;
    public CheckBox chkNoLimit;
    public ComboBox<String> cmbGenerateMonth;
    public ComboBox<Integer> cmbGenerateYear;
    public ComboBox<String> cmbSearchMonth;
    public ComboBox<String> cmbSearchYear;
    public Label lblNoLimit;
    public TableView<Payroll> tblPayrolls;
    public TextField txtBonusPercentage;
    public TextField txtOtHours;
    public TextField txtSearch;
    public Label lblMessage;
    public Button btnClearFilters;
    public Button btnBack;
    public Button btnEmployee;
    public Button btnAttendance;
    public Button btnLeave;
    public Button btnPayroll;
    private Connection connection;

    public void initialize() {
        btnClearFilters.setDisable(true);
        btnGenerate.setDisable(true);

        connection = DBConnection.getInstance().getConnection();

        /*Column Mapping*/
        tblPayrolls.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("formattedId"));
        tblPayrolls.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblPayrolls.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("year"));
        tblPayrolls.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("month"));
        tblPayrolls.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("basicSalary"));
        tblPayrolls.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("overTimePayment"));
        tblPayrolls.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("bonus"));
        tblPayrolls.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("deductionsStr"));
        tblPayrolls.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("netPayStr"));

        loadData();

        addCmbItems();

        txtSearch.textProperty().addListener((value, previous, current) -> {
            filter();
        });



    }

    private void addCmbItems() {
        cmbSearchYear.getItems().add("Select Year: none");
        cmbSearchMonth.getItems().add("Select Month: none");

        for (int i = (new java.sql.Date(new Date().getTime())).toLocalDate().getYear(); i >= 2010 ; i--) {
            cmbGenerateYear.getItems().add(i);
            cmbSearchYear.getItems().add(Integer.toString(i));
        }

        List<String> months = Arrays.asList("JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER");
        cmbGenerateMonth.getItems().addAll(months);
        cmbSearchMonth.getItems().addAll(months);
    }

    private void filter() {
        try {
            if ((cmbSearchYear.getSelectionModel().isEmpty() || cmbSearchYear.getSelectionModel().getSelectedIndex() == 0) &&
                    (cmbSearchMonth.getSelectionModel().isEmpty() || cmbSearchMonth.getSelectionModel().getSelectedIndex() == 0) &&
                    (txtSearch.getText().equals("") || txtSearch.getText() == null)) {
                btnClearFilters.setDisable(true);
            } else {
                btnClearFilters.setDisable(false);
            }


            /*Search by only using the year*/
            if ((!cmbSearchYear.getSelectionModel().isEmpty() && !cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (cmbSearchMonth.getSelectionModel().isEmpty() || cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE date LIKE CONCAT(?, '%')");
                stm.setString(1, cmbSearchYear.getSelectionModel().getSelectedItem());
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by only using month*/
            } else if ((cmbSearchYear.getSelectionModel().isEmpty() || cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (!cmbSearchMonth.getSelectionModel().isEmpty() && !cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE date LIKE CONCAT('%-', ?, '-%')");
                stm.setString(1, String.format("%02d", cmbSearchMonth.getSelectionModel().getSelectedIndex()));
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by only using text*/
            } else if ((cmbSearchYear.getSelectionModel().isEmpty() || cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (cmbSearchMonth.getSelectionModel().isEmpty() || cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    !txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE (Payroll.id LIKE CONCAT('%', ?, '%')) OR (Payroll.name LIKE CONCAT('%', ?, '%'))");
                stm.setString(1, txtSearch.getText());
                stm.setString(2, txtSearch.getText());
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by using year and month*/
            } else if ((!cmbSearchYear.getSelectionModel().isEmpty() && !cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (!cmbSearchMonth.getSelectionModel().isEmpty() && !cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE date LIKE CONCAT(?, '%') AND (date LIKE CONCAT('%-', ?, '-%'))");
                stm.setString(1, cmbSearchYear.getSelectionModel().getSelectedItem());
                stm.setString(2, String.format("%02d", cmbSearchMonth.getSelectionModel().getSelectedIndex()));
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by only using year and text*/
            } else if ((!cmbSearchYear.getSelectionModel().isEmpty() && !cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (cmbSearchMonth.getSelectionModel().isEmpty() || cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    !txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE date LIKE CONCAT(?, '%') AND ((Payroll.id LIKE CONCAT('%', ?, '%')) OR (Payroll.name LIKE CONCAT('%', ?, '%')))");
                stm.setString(1, cmbSearchYear.getSelectionModel().getSelectedItem());
                stm.setString(2, txtSearch.getText());
                stm.setString(3, txtSearch.getText());
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by only using month and text*/
            } else if ((cmbSearchYear.getSelectionModel().isEmpty() || cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (!cmbSearchMonth.getSelectionModel().isEmpty() && !cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    !txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE (date LIKE CONCAT('%-', ?, '-%')) AND ((Payroll.id LIKE CONCAT('%', ?, '%')) OR (Payroll.name LIKE CONCAT('%', ?, '%')))");
                stm.setString(1, String.format("%02d", cmbSearchMonth.getSelectionModel().getSelectedIndex()));
                stm.setString(2, txtSearch.getText());
                stm.setString(3, txtSearch.getText());
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search by using year, month and text*/
            } else if ((!cmbSearchYear.getSelectionModel().isEmpty() && !cmbSearchYear.getSelectionModel().getSelectedItem().equals("Select Year: none")) &&
                    (!cmbSearchMonth.getSelectionModel().isEmpty() && !cmbSearchMonth.getSelectionModel().getSelectedItem().equals("Select Month: none")) &&
                    !txtSearch.getText().equals("")) {
                PreparedStatement stm = connection.prepareStatement("SELECT  * FROM Payroll WHERE (date LIKE CONCAT(?, '%')) AND (date LIKE CONCAT('%-', ?, '-%')) AND ((Payroll.id LIKE CONCAT('%', ?, '%')) OR (Payroll.name LIKE CONCAT('%', ?, '%')))");
                stm.setString(1, cmbSearchYear.getSelectionModel().getSelectedItem());
                stm.setString(2, String.format("%02d", cmbSearchMonth.getSelectionModel().getSelectedIndex()));
                stm.setString(3, txtSearch.getText());
                stm.setString(4, txtSearch.getText());
                ResultSet rst = stm.executeQuery();
                addResultSet(rst);
                /*Search all*/
            } else {
                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Payroll");
                addResultSet(rst);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SELECT * FROM Payroll");
            addResultSet(rst);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addResultSet (ResultSet rst) {
        tblPayrolls.getItems().clear();
        try {
            while (rst.next()) {
                int id = rst.getInt("id");
                java.sql.Date date = rst.getDate("date");
                String name = rst.getString("name");
                int basicSalary = rst.getInt("basic_salary");
                int overtimePayment = rst.getInt("overtime_payment");
                int bonus = rst.getInt("bonus");
                double tax = rst.getDouble("tax");
                double epf = rst.getDouble("epf");
                double etf = rst.getDouble("etf");
                double union_fee = rst.getDouble("union_fee");

                Payroll payroll = new Payroll(id, date, name, basicSalary, overtimePayment, bonus, tax, epf, etf, union_fee);
                tblPayrolls.getItems().add(payroll);
            }
        } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    }

    @FXML
    void btnGenerateOnAction(ActionEvent event) {
        boolean valid = isValid();
        if (!valid) return;

        try {
            File file = new File("application.properties");
            FileReader fileReader = new FileReader(file);
            Properties properties = new Properties();
            properties.load(fileReader);
            double otPercentage = Double.parseDouble(properties.getProperty("mysql.otPercentage"));
            double epfPercentage = Double.parseDouble(properties.getProperty("mysql.epfPercentage"));
            double etfPercentage = Double.parseDouble(properties.getProperty("mysql.etfPercentage"));
            double unionFee = Double.parseDouble(properties.getProperty("mysql.unionFee"));
            double bonusPercentage = Double.parseDouble(txtBonusPercentage.getText());
            int maxOT = 0;
            boolean noLimitOT = chkNoLimit.isSelected();
            if (!noLimitOT) maxOT = Integer.parseInt(txtOtHours.getText());
            int year = cmbGenerateYear.getSelectionModel().getSelectedItem();
            int month = cmbGenerateMonth.getSelectionModel().getSelectedIndex() + 1;
            int numberOfWorkdaysInTheMonth = numberOfWorkdays(year, month);

            Statement stm = connection.createStatement();
            ResultSet rstEmployee = stm.executeQuery("SELECT * FROM Employee WHERE status='ACTIVE'");
            while (rstEmployee.next()) {
                int id = rstEmployee.getInt("id");
                java.sql.Date date = java.sql.Date.valueOf(LocalDate.of(year, month, 1));
                String name = rstEmployee.getString("name");
                int basic = rstEmployee.getInt("basic_salary");
                double ot = otCalculation(id, otPercentage, year, month, numberOfWorkdaysInTheMonth, basic, noLimitOT, maxOT);
                int otInt = (int) ot;
                double noPay = noPayCalculation(id, year, month, numberOfWorkdaysInTheMonth, basic);
                double bonus = basic * (bonusPercentage / 100.0);
                int bonusInt = (int) bonus;
                double grossSalary = basic + ot + bonus - noPay;
                double tax = calculateTax(grossSalary);
                double epf = (grossSalary * epfPercentage / 100);
                double etf = (grossSalary * etfPercentage / 100);

                Statement stm2 = connection.createStatement();
                stm2.executeUpdate("INSERT INTO Payroll (id, date, name, basic_salary, overtime_payment, bonus, tax, epf, etf, union_fee) " +
                        "VALUES (" + id + ", '" + date + "', '" + name + "', " + (double) (Math.round((basic - noPay) * 100)) / 100 + ", " + otInt + ", " + bonusInt + ", " + (double) (Math.round((tax) * 100)) / 100 + ", " + (double) (Math.round((epf) * 100)) / 100 + ", " + (double) (Math.round((etf) * 100)) / 100 + ", " + unionFee + ")");

                tblPayrolls.getItems().add(new Payroll(id, date, name, basic, otInt, bonusInt, tax, epf, etf, unionFee));
                lblMessage.setText("The payroll for the year: " + cmbGenerateYear.getSelectionModel().getSelectedItem() + " and month: " + cmbGenerateMonth.getSelectionModel().getSelectedItem() + " is generated.");
                lblMessage.getStyleClass().add("green");

                Platform.runLater(() -> {
                    txtOtHours.clear();
                    txtBonusPercentage.setText("0");
                    cmbGenerateYear.getSelectionModel().clearSelection();
                    cmbGenerateMonth.getSelectionModel().clearSelection();
                    btnGenerate.setDisable(true);
                });

                tblPayrolls.getSelectionModel().clearSelection();
                cmbSearchMonth.getSelectionModel().clearSelection();
                cmbSearchYear.getSelectionModel().clearSelection();
                txtSearch.clear();
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private double calculateTax(double grossSalary) {
        double annualSalary = grossSalary * 12;
        double tax = 0;
        double amount = annualSalary - 1200000; /*Tax relief for first 1.2 Million*/

        if (amount <= 0) return tax;
        else if (amount <= 500000) {
            tax += (amount * 0.06 / 12);
            return tax;
        } else {
            tax += 2500; /*500000 * 0.06 / 12*/
            amount -= 500000;
        }

        if (amount <= 500000) {
            tax += (amount * 0.12 / 12);
            return tax;
        } else {
            tax += 5000; /*500000 * 0.12 / 12*/
            amount -= 500000;
        }

        if (amount <= 500000) {
            tax += (amount * 0.18 / 12);
            return tax;
        } else {
            tax += 7500; /*500000 * 0.18 / 12*/
            amount -= 500000;
        }

        if (amount <= 500000) {
            tax += (amount * 0.24 / 12);
            return tax;
        } else {
            tax += 10000; /*500000 * 0.24 / 12*/
            amount -= 500000;
        }

        if (amount <= 500000) {
            tax += (amount * 0.30 / 12);
            return tax;
        } else {
            tax += 12500; /*500000 * 0.30 / 12*/
            amount -= 500000;
        }

        tax += (amount * 0.36 / 12);
        return tax;
    }

    private double noPayCalculation(int id, int year, int month, int numberOfWorkDaysInTheMonth, int basic) {
        LocalDate date = LocalDate.of(year, month, 1);
        int lengthOfMonth = date.lengthOfMonth();
        int noPayDays = 0;
        for (int i = 1; i <= lengthOfMonth; i++) {
            if (!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
                try {
                    PreparedStatement stm1 = connection.prepareStatement("SELECT * FROM Attendance WHERE (id=?) AND (date LIKE CONCAT(?,'-',?,'-',?))");
                    stm1.setString(1, Integer.toString(id));
                    stm1.setString(2, Integer.toString(year));
                    stm1.setString(3, String.format("%02d", month));
                    stm1.setString(4, String.format("%02d", date.getDayOfMonth()));
                    ResultSet rst1 = stm1.executeQuery();
                    if (!rst1.next()) {
                        PreparedStatement stm2 = connection.prepareStatement("SELECT * FROM Leaves WHERE (id=?) AND (leave_date LIKE CONCAT(?,'-',?,'-',?)) AND status='APPROVED'");
                        stm2.setString(1, Integer.toString(id));
                        stm2.setString(2, Integer.toString(year));
                        stm2.setString(3, String.format("%02d", month));
                        stm2.setString(4, String.format("%02d", date.getDayOfMonth()));
                        ResultSet rst2 = stm2.executeQuery();
                        if (!rst2.next()) {
                            noPayDays++;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            date = date.plusDays(1);
        }
        return (((double) basic) / (double) numberOfWorkDaysInTheMonth) * noPayDays;
    }

    private double otCalculation(int id, double otPercentage, int year, int month, int numberOfWorkdaysInTheMonth, int basic, boolean noLimitOT, int maxOT) {
        double hourlyRateOfBasic = ((double) basic) / (numberOfWorkdaysInTheMonth * 8);
        double numberOfOTHours = numberOfOTHours(id, year, month, noLimitOT, maxOT);
        double ot = numberOfOTHours * hourlyRateOfBasic * otPercentage;
        return ot;
    }

    private double numberOfOTHours(int id, int year, int month, boolean noLimitOT, int maxOT) {
        try {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Attendance WHERE (id=?) AND (date LIKE CONCAT(?, '-', ?, '%'))");
            stm.setString(1, Integer.toString(id));
            stm.setString(2, Integer.toString(year));
            stm.setString(3, String.format("%02d", month));
            ResultSet rst = stm.executeQuery();
            double otHours = 0;
            while (rst.next()) {
                java.sql.Date date = rst.getDate("date");
                Time inTime = rst.getTime("in_time");
                Time offTime = rst.getTime("off_time");

                if (date.toLocalDate().getDayOfWeek().name().equals("SATURDAY") || date.toLocalDate().getDayOfWeek().name().equals("SUNDAY")) {
                    Duration duration = Duration.between(inTime.toLocalTime(), offTime.toLocalTime());
                    double hours = (((double) duration.toMinutes()) / (60.0)) - 1.0; /*Deduction for lunch hour*/
                    otHours += hours;
                } else {
                    if (inTime.toLocalTime().isBefore(LocalTime.of(8, 0, 0))) {
                        Duration duration1 = Duration.between(inTime.toLocalTime(), LocalTime.of(8, 0, 0));
                        double hours = ((double) duration1.toMinutes()) / (60.0);
                        otHours += hours;
                    }
                    if (offTime.toLocalTime().isAfter(LocalTime.of(17, 0, 0))) {
                        Duration duration2 = Duration.between(LocalTime.of(17, 0, 0), offTime.toLocalTime());
                        double hours = ((double) duration2.toMinutes()) / (60.0);
                        otHours += hours;
                    }
                }
            }
            if (!noLimitOT) {
                if (otHours > Integer.parseInt(txtOtHours.getText())) {
                    otHours = Integer.parseInt(txtOtHours.getText());
                }
            }
            return otHours;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int numberOfWorkdays(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        int lengthOfMonth = date.lengthOfMonth();
        int workDays = 0;
        for (int i = 1; i <= lengthOfMonth; i++) {
            if (!date.getDayOfWeek().name().equals("SATURDAY") && !date.getDayOfWeek().name().equals("SUNDAY")) {
                workDays++;
            }
            date = date.plusDays(1);
        }
        return workDays;
    }

    @FXML
    void chkNoLimitOnAction(ActionEvent event) {
        txtOtHours.clear();
        txtOtHours.setDisable(chkNoLimit.isSelected());
    }

    @FXML
    void cmbSearchMonthOnAction(ActionEvent event) {
        filter();
    }

    @FXML
    void cmbSearchYearOnAction(ActionEvent event) {
        filter();
    }

    @FXML
    void lblNoLimitOnAction(MouseEvent event) {
        txtOtHours.clear();
        chkNoLimit.setSelected(!chkNoLimit.isSelected());
        txtOtHours.setDisable(chkNoLimit.isSelected());
    }

    public void btnClearFiltersOnAction(ActionEvent actionEvent) {
        tblPayrolls.getSelectionModel().clearSelection();
        cmbSearchYear.getSelectionModel().select(0);
        cmbSearchMonth.getSelectionModel().select(0);
        txtSearch.clear();
        btnClearFilters.setDisable(true);
    }
    private boolean isValid() {
        cmbGenerateYear.getStyleClass().remove("invalid");
        cmbGenerateMonth.getStyleClass().remove("invalid");
        txtBonusPercentage.getStyleClass().remove("invalid");
        txtOtHours.getStyleClass().remove("invalid");
        lblMessage.setText("");
        lblMessage.getStyleClass().remove("red");
        lblMessage.getStyleClass().remove("green");
        try {
            /*Checking for previously generated payrolls*/
            boolean valid = true;
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM Payroll WHERE date LIKE CONCAT(?, '-', ?, '%')");
            stm.setString(1, Integer.toString(cmbGenerateYear.getSelectionModel().getSelectedItem()));
            stm.setString(2, String.format("%02d", (cmbGenerateMonth.getSelectionModel().getSelectedIndex() + 1)));
            ResultSet rst = stm.executeQuery();
            boolean isNext = rst.next();
            if (isNext) {
                valid = false;
                cmbGenerateYear.getStyleClass().add("invalid");
                cmbGenerateMonth.getStyleClass().add("invalid");
                lblMessage.setText("The payroll for the year: " + cmbGenerateYear.getSelectionModel().getSelectedItem() + " and month: " + cmbGenerateMonth.getSelectionModel().getSelectedItem() + " has already been generated.");
                lblMessage.getStyleClass().add("red");
                cmbGenerateYear.requestFocus();
            }

            /*Validating bonus percentage*/
            if (txtBonusPercentage.getText().equals(null) || txtBonusPercentage.getText().equals("")) {
                valid = false;
                txtBonusPercentage.getStyleClass().add("invalid");
                lblMessage.setText("The \"Bonus Percentage\" value should be entered");
                lblMessage.getStyleClass().add("red");
                txtBonusPercentage.requestFocus();
            } else {
                String bonusText = txtBonusPercentage.getText();
                if (bonusText.substring(0,1).equals("-")) {
                    bonusText = bonusText.substring(1);
                    valid = false;
                    txtBonusPercentage.getStyleClass().add("invalid");
                    lblMessage.setText("The \"Bonus Percentage\" value cannot be a negative number");
                    lblMessage.getStyleClass().add("red");
                    txtBonusPercentage.requestFocus();
                    txtBonusPercentage.selectAll();
                }
                for (char c : bonusText.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        valid = false;
                        txtBonusPercentage.getStyleClass().add("invalid");
                        lblMessage.setText("The \"Bonus Percentage\" value should be a whole number");
                        lblMessage.getStyleClass().remove("red");
                        lblMessage.getStyleClass().add("red");
                        txtBonusPercentage.requestFocus();
                        txtBonusPercentage.selectAll();
                    }
                }
            }

            /*Validating Maximum Overtime Hours*/
            if (!chkNoLimit.isSelected()) {
                if (txtOtHours.getText().equals(null) || txtOtHours.getText().equals("")) {
                    valid = false;
                    txtOtHours.getStyleClass().add("invalid");
                    lblMessage.setText("The \"Max. OT hours\" value should be entered");
                    lblMessage.getStyleClass().add("red");
                    txtOtHours.requestFocus();
                } else {
                    String bonusText = txtOtHours.getText();
                    if (bonusText.substring(0,1).equals("-")) {
                        bonusText = bonusText.substring(1);
                        valid = false;
                        txtOtHours.getStyleClass().add("invalid");
                        lblMessage.setText("The \"Max. OT hours\" value cannot be a negative number");
                        lblMessage.getStyleClass().add("red");
                        txtOtHours.requestFocus();
                        txtOtHours.selectAll();
                    }
                    for (char c : bonusText.toCharArray()) {
                        if (!Character.isDigit(c)) {
                            valid = false;
                            txtOtHours.getStyleClass().add("invalid");
                            lblMessage.setText("The \"Max. OT hours\" value should be a whole number");
                            lblMessage.getStyleClass().remove("red");
                            lblMessage.getStyleClass().add("red");
                            txtOtHours.requestFocus();
                            txtOtHours.selectAll();
                        }
                    }
                }
            }
            return valid;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbGenerateMonthOnAction(ActionEvent actionEvent) {
        if (!cmbGenerateYear.getSelectionModel().isEmpty() && !cmbGenerateMonth.getSelectionModel().isEmpty()) {
            btnGenerate.setDisable(false);
        }
    }

    public void cmbGenerateYearOnAction(ActionEvent actionEvent) {
        if (!cmbGenerateYear.getSelectionModel().isEmpty() && !cmbGenerateMonth.getSelectionModel().isEmpty()) {
            btnGenerate.setDisable(false);
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setResizable(true);
            stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/AdminView.fxml"))));
            stage.setTitle("Admin Window");
            stage.sizeToScene();
            stage.centerOnScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnEmployee.getScene().getWindow();
        stage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/EmployeeView.fxml")).load()));
        stage.setTitle("Add Employee");
        stage.setMaximized(true);
    }

    public void btnAttendanceOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnAttendance.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminAttendanceView.fxml"));
        AnchorPane root = fxmlLoader.load();
        AdminAttendanceViewController ctrl = fxmlLoader.getController();
        ctrl.initData(btnAttendance.getScene());
        stage.setTitle("Admin Attendance View");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
    }

    public void btnLeaveOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage=(Stage) btnLeave.getScene().getWindow();
        URL fxmlFile=this.getClass().getResource("/view/AdminLeaveView.fxml");
        FXMLLoader fxmlLoader=new FXMLLoader(fxmlFile);
        AnchorPane root=fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.setTitle("Admin Leave Management");
        stage.setMaximized(true);
    }

    public void btnPayrollOnAction(ActionEvent actionEvent) {
    }
}