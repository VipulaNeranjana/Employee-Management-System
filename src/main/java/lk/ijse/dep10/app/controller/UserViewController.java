package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.IOException;
import java.sql.*;

public class UserViewController {

    public Label lblRemainingLeaves;
    public Label lblPendingLeaves;
    public Label lblTakenLeaves;
    public Label lblUnionMember;
    public Label lblBasicSalary;
    public Label lblDesignation;
    public Label lblJoinedDate;
    public Label lblNationality;
    public Label lblMaritalStatus;
    public Label lblGender;
    public Label lblContact;
    public Label lblAddress;
    public Label lblDOB;
    public Label lblNIC;
    public Label lblName;
    public Label lblID;
    public Button btnBack;
    public ImageView imgProfilePicture;
    public AnchorPane panePanel;
    public AnchorPane paneRoot;
    public Label lblUserName;
    @FXML
    private Button btnLeave;

    @FXML
    private Button btnPaySheet;
    private int employeeId;
    private String joinedYear;
    public void getEmployeeId(int id){
        employeeId = id;
    }
    public void initialize() throws IOException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement stm1 = connection.prepareStatement("SELECT * FROM Employee WHERE id = ?");
            stm1.setInt(1,employeeId);
            ResultSet rst1 = stm1.executeQuery();
            if (rst1.next()){
                joinedYear = Integer.toString(rst1.getDate("joined_date").toLocalDate().getYear());

                lblUserName.setText(rst1.getString("user_name"));
                imgProfilePicture.setImage(new Image(rst1.getBlob("profile_pic").getBinaryStream(),150,150,true,true));
                lblID.setText(": " + "E-" + joinedYear + "-" + employeeId);
                lblName.setText(": " + rst1.getString("name"));
                lblNIC.setText(": " + rst1.getString("nic"));
                lblDOB.setText(": " + rst1.getDate("dob").toLocalDate().toString());
                lblAddress.setText(": " + rst1.getString("address"));
                lblContact.setText(": " + rst1.getString("contact"));
                lblGender.setText(": " + (rst1.getString("gender").equals("MALE")?"Male":"Female"));
                lblMaritalStatus.setText(": " + (rst1.getString("marital_status").equals("MARRIED")?"Married":"Unmarried"));
                lblNationality.setText(": " + rst1.getString("nationality"));
                lblJoinedDate.setText(": " + rst1.getDate("joined_date").toLocalDate().toString());
                lblDesignation.setText(": " + (rst1.getString("designation").equals("EXECUTIVE")?"Executive":"Non-executive"));
                lblBasicSalary.setText(": Rs. " + rst1.getInt("basic_salary") + " /=");
                lblUnionMember.setText(": " + (rst1.getInt("union_member") == 1?"Yes":"No"));
            }
            PreparedStatement stm2 = connection.prepareStatement("SELECT * FROM Leaves WHERE id = ?");
            stm2.setInt(1, employeeId);
            ResultSet rst2 = stm2.executeQuery();
            int approvedLeaves = 0;
            int pendingLeaves = 0;
            while (rst2.next()){
                if (rst2.getString("status").equals("APPROVED")){
                    approvedLeaves += 1;
                }
                if (rst2.getString("status").equals("PENDING")){
                    pendingLeaves += 1;
                }
            }
            int remainingLeaves = 14 - approvedLeaves;
            lblTakenLeaves.setText(approvedLeaves + " leaves have been taken");
            lblPendingLeaves.setText(pendingLeaves + " pending leave requests");
            lblRemainingLeaves.setText(remainingLeaves + " leaves are remaining");
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Unable to get the employee. try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnPaySheetOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnPaySheet.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/PaySheetView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Pay Sheet Window");
        stage.show();
        stage.centerOnScreen();

        PaySheetViewController controller = fxmlLoader.getController();
        controller.getEmployeeId(employeeId);
        controller.getJoinedYear(joinedYear);
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/LoginView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Login View Window");
        stage.show();
        stage.centerOnScreen();
    }
}
