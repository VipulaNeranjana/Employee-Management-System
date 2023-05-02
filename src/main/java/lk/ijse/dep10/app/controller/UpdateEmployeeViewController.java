package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class UpdateEmployeeViewController {

    @FXML
    private ToggleGroup Designation;

    @FXML
    private ToggleGroup Gender;

    @FXML
    private ToggleGroup MaritalStatus;

    @FXML
    private ToggleGroup Status;

    @FXML
    private ToggleGroup UserType;

    @FXML
    private Button btnAddCv;

    @FXML
    private Button btnAddPic;

    @FXML
    private Button btnAgreementLetterAdd;

    @FXML
    private Button btnAttendance;

    @FXML
    private Button btnBirthCertificateAdd;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnOfferLetterAdd;

    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnRemovePic;

    @FXML
    private Button btnUpdate;

    @FXML
    private DatePicker calDob;

    @FXML
    private DatePicker calJoinedDate;

    @FXML
    private CheckBox chbUnionMember;

    @FXML
    private ImageView imgProfile;

    @FXML
    private Label lblUserName;

    @FXML
    private HBox rdo;

    @FXML
    private RadioButton rdoActive;

    @FXML
    private RadioButton rdoAdmin;

    @FXML
    private RadioButton rdoExecutive;

    @FXML
    private RadioButton rdoInActive;

    @FXML
    private RadioButton rdoMale;

    @FXML
    private RadioButton rdoMarried;

    @FXML
    private RadioButton rdoNonExecutive;

    @FXML
    private RadioButton rdoUnMarried;

    @FXML
    private RadioButton rdoUser;

    @FXML
    private RadioButton tdoFemale;

    @FXML
    private TextField txtAccountNo;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtAgreementLetterPath;

    @FXML
    private TextField txtBankAccount;

    @FXML
    private TextField txtBasicSalary;

    @FXML
    private TextField txtBirthCertificatePath;

    @FXML
    private TextField txtBranchName;

    @FXML
    private TextField txtCVPath;

    @FXML
    private TextField txtContact;

    @FXML
    private TextField txtFullName;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNationality;

    @FXML
    private TextField txtNicNo;

    @FXML
    private TextField txtOfferLetterPath;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    private int employeeId;
    public void getEmployeeId(int id){
        employeeId = id;
    }
    public void initialize(){
        Platform.runLater(() -> {
            
        });
    }

    @FXML
    void btnAddCvOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddPicOnAction(ActionEvent event) {

    }

    @FXML
    void btnAgreementLetterAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnAttendanceOnAction(ActionEvent event) {

    }

    @FXML
    void btnBirthCertificateAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {

    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnOfferLetterAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemovePicOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}

