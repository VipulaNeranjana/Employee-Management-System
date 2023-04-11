package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.util.LeaveDay;
import lk.ijse.dep10.app.util.LeaveType;
import lk.ijse.dep10.app.util.Status;

import java.sql.*;
import java.time.LocalDate;

public class LeaveApplicationController {

    @FXML
    private ToggleGroup LeaveDuration;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnSubmit;

    @FXML
    private ComboBox<String> cmbLeaveType;

    @FXML
    private DatePicker dpStartDate;

    @FXML
    private RadioButton rdoFullDay;

    @FXML
    private RadioButton rdoHalfDay;

    @FXML
    private TextArea txtReason;

    //TODO Should get user id

    private int id = 123;

    public void initialize() {
        cmbLeaveType.getItems().addAll("Sick", "Other");
        cmbLeaveType.setValue("Sick");

        cmbLeaveType.getSelectionModel().selectedItemProperty().addListener((value, current, previous) -> {
            /*Reason section is disabled*/
            txtReason.setDisable(true);
            if (cmbLeaveType.getSelectionModel().getSelectedItem().equals("Other")) {
                txtReason.setDisable(false);
            }

        });

    }

    @FXML
    void btnCloseOnAction(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();

    }

    @FXML
    void btnSubmitOnAction(ActionEvent event) {

        if (!isValid()) {
            return;
        }


        Connection connection = DBConnection.getInstance().getConnection();

        try {

            /*Check whether the leave request is already sent for the selected date*/
            PreparedStatement stm2 = connection.prepareStatement("select * from Leaves where leave_date=?");
            stm2.setDate(1,Date.valueOf(dpStartDate.getValue()));
            ResultSet resultSet = stm2.executeQuery();

            if (resultSet.next()){
                new Alert(Alert.AlertType.INFORMATION,"Date is already taken, try another date!").showAndWait();
                return;
            }


            connection.setAutoCommit(false);
            String sql = "insert into Leaves values (?,?,?,?,?,?)";
            PreparedStatement stm = connection.prepareStatement(sql);

            stm.setInt(1, id);
            stm.setDate(2, Date.valueOf(dpStartDate.getValue()));
            stm.setDate(3, Date.valueOf(LocalDate.now()));
            stm.setString(4,
                    (rdoFullDay.isSelected() ? LeaveDay.FULL_DAY : LeaveDay.HALF_DAY).toString());
            stm.setString(5, Status.PENDING.toString());
            stm.setString(6,
                    (cmbLeaveType.getSelectionModel().getSelectedItem().equals("Sick") ? LeaveType.SICK : LeaveType.OTHER).toString());

            stm.executeUpdate();

            if (!txtReason.getText().isEmpty()) {
                sql = "insert into Leave_Description values (?,?,?)";
                PreparedStatement stm1 = connection.prepareStatement(sql);
                stm1.setInt(1, id);
                stm1.setDate(2, Date.valueOf(dpStartDate.getValue()));
                stm1.setString(3, txtReason.getText());

                stm1.executeUpdate();
            }

            connection.commit();


        } catch (Throwable e) {

            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to submit the Application!").show();
            throw new RuntimeException(e);

        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private boolean isValid() {
        boolean validData = true;

        if (cmbLeaveType.getSelectionModel().getSelectedItem().equals("Other") && txtReason.getText().isEmpty()) {
            validData = false;
            txtReason.requestFocus();
        }
        if (dpStartDate.getValue() == null || (dpStartDate.getValue().isBefore(LocalDate.now()))) {
            validData = false;
            dpStartDate.requestFocus();
        }
        if (rdoFullDay.getToggleGroup().getSelectedToggle() == null) {
            validData = false;
            rdoFullDay.requestFocus();
        }
        if (cmbLeaveType.getSelectionModel().getSelectedItem() == null) {
            validData = false;
            cmbLeaveType.requestFocus();
        }
        return validData;
    }

}
