package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.enumaration.*;
import lk.ijse.dep10.app.model.Employee;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeViewController {

    public Label lblUserName;
    public RadioButton rdoExecutive;
    public RadioButton rdoNonExecutive;
    @FXML
    private ToggleGroup Gender;

    @FXML
    private ToggleGroup MaritalStatus;

    @FXML
    private ToggleGroup UserType;

    @FXML
    private Button btnAddCv;

    @FXML
    private Button btnAddPic;

    @FXML
    private Button btnAgreementLatterAdd;

    @FXML
    private Button btnAttendance;

    @FXML
    private Button btnBirthCertificateAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnNewEmployee;

    @FXML
    private Button btnOfferLatterAdd;

    @FXML
    private Button btnPayroll;

    @FXML
    private Button btnRemovePic;

    @FXML
    private Button btnSave;

    @FXML
    private DatePicker calDob;

    @FXML
    private DatePicker calJoinedDate;

    @FXML
    private CheckBox chbUnionMember;

    @FXML
    private ImageView imgProfile;

    @FXML
    private HBox rdo;

    @FXML
    private RadioButton rdoActive;

    @FXML
    private RadioButton rdoAdmin;

    @FXML
    private RadioButton rdoInActive;

    @FXML
    private RadioButton rdoMale;

    @FXML
    private RadioButton rdoMarried;

    @FXML
    private RadioButton rdoUnMarried;

    @FXML
    private RadioButton rdoUser;

    @FXML
    private RadioButton tdoFemale;

    @FXML
    private PasswordField txtAccountNo;

    @FXML
    private TextField txtAddress;

    @FXML
    private PasswordField txtAgreementLatterPath;

    @FXML
    private PasswordField txtBankAccount;

    @FXML
    private PasswordField txtBasicSalary;

    @FXML
    private PasswordField txtBirthCertificatePath;

    @FXML
    private PasswordField txtBranchName;

    @FXML
    private PasswordField txtCVPath;

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
    private PasswordField txtOfferLatterPath;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    boolean isValid = true;
    private File offerLatter;
    private File agreementLatter;
    private File birthCertificate;
    private File cv;
    private String userName;

    public void initialize() {

        Platform.runLater(() -> {
            lblUserName.setText(userName);
        });
        txtFullName.textProperty().addListener((value, previous, current) -> {
            txtFullName.getStyleClass().remove("invalid");
            if (!txtFullName.getText().matches("[A-Za-z ]{3,}")) {
                txtFullName.getStyleClass().add("invalid");
                txtFullName.requestFocus();
                isValid = false;
            }
        });
        txtNicNo.textProperty().addListener((value, previous, current) -> {
            txtNicNo.getStyleClass().remove("invalid");
            if (!txtNicNo.getText().matches("\\d{9}[Vv]")) {
                txtNicNo.getStyleClass().add("invalid");
                txtNicNo.requestFocus();
                isValid = false;
            }
        });
        calDob.valueProperty().addListener((value, previous, current) -> {
            calDob.getStyleClass().remove("invalid");
            if (calDob.getValue() == null || calDob.getValue().isAfter(LocalDate.now().minusYears(18))) {
                calDob.getStyleClass().add("invalid");
                calDob.requestFocus();
                isValid = false;
            }
        });
        txtContact.textProperty().addListener((value, previous, current) -> {
            txtContact.getStyleClass().remove("invalid");
            if (!txtContact.getText().matches("\\d{3}-\\d{7}")) {
                txtContact.getStyleClass().add("invalid");
                txtContact.requestFocus();
            }
        });
        txtAddress.textProperty().addListener((value, previous, current) -> {
            txtAddress.getStyleClass().remove("invalid");
            if (!txtAddress.getText().matches("[A-Za-z 0-9-/]{3,}")) {
                txtAddress.getStyleClass().add("invalid");
                txtAddress.requestFocus();
                isValid = false;
            }
        });
        txtNationality.textProperty().addListener((value, previous, current) -> {
            txtNationality.getStyleClass().remove("invalid");
            if (!txtNationality.getText().matches("[A-Za-z]{3,}")) {
                txtNationality.getStyleClass().add("invalid");
                txtNationality.requestFocus();
                isValid = false;
            }
        });
        txtUserName.textProperty().addListener((value, previous, current) -> {
            txtUserName.getStyleClass().remove("invalid");
            if (!txtUserName.getText().matches("[A-Za-z 0-9]{3,}")) {
                txtUserName.getStyleClass().add("invalid");
                txtUserName.requestFocus();
                isValid = false;
            }
        });
        txtPassword.textProperty().addListener((value, previous, current) -> {
            txtPassword.getStyleClass().remove("invalid");
            if (!txtPassword.getText().matches("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[-/~!@#$%^&*+-])(?=\\S+$).{8,}")) {
                txtPassword.getStyleClass().add("invalid");
                txtPassword.requestFocus();
                isValid = false;
            }
        });
        calJoinedDate.valueProperty().addListener((value, previous, current) -> {
            calJoinedDate.getStyleClass().remove("invalid");
            if (calJoinedDate.getValue() == null || calJoinedDate.getValue().isBefore(LocalDate.now().minusDays(30))) {
                calJoinedDate.getStyleClass().add("invalid");
                calJoinedDate.requestFocus();
                isValid = false;
            }
        });
        txtBasicSalary.textProperty().addListener((value, previous, current) -> {
            txtBasicSalary.getStyleClass().remove("invalid");
            if (!txtBasicSalary.getText().matches("\\d{4,}")) {
                txtBasicSalary.getStyleClass().add("invalid");
                txtBasicSalary.requestFocus();
                isValid = false;
            }
        });
        txtBankAccount.textProperty().addListener((value, previous, current) -> {
            txtBankAccount.getStyleClass().remove("invalid");
            if (!txtBankAccount.getText().matches("[A-Za-z]{3,}")) {
                txtBankAccount.getStyleClass().add("invalid");
                txtBankAccount.requestFocus();
                isValid = false;
            }
        });
        txtAccountNo.textProperty().addListener((value, previous, current) -> {
            txtAccountNo.getStyleClass().remove("invalid");
            if (!txtAccountNo.getText().matches("[0-9]+")) {
                txtAccountNo.getStyleClass().add("invalid");
                txtAccountNo.requestFocus();
                isValid = false;
            }
        });
        txtBranchName.textProperty().addListener((value, previous, current) -> {
            txtBranchName.getStyleClass().remove("invalid");
            if (!txtBranchName.getText().matches("[A-Za-z]{3,}")) {
                txtBranchName.getStyleClass().add("invalid");
                txtBranchName.requestFocus();
                isValid = false;
            }
        });

    }

    public void initData(String userName){
        this.userName = userName;
    }


    private File getFile(Button button) {
        FileChooser fileChooser = new FileChooser();
        System.out.println(System.getProperties());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", "*.pdf", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(button.getScene().getWindow());
        return file;
    }

    @FXML
    void BtnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddCvOnAction(ActionEvent event) {
        File file = getFile(btnAddCv);

        if(!file.exists()) {
            txtCVPath.getStyleClass().add("invalid");
            return;
        };

        txtCVPath.setText(file.getAbsolutePath());
        cv = file;
    }

    @FXML
    void btnAddPicOnAction(ActionEvent event) throws URISyntaxException, MalformedURLException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("img","*.png","*.jpg","*.jpeg"));
        File file = fileChooser.showOpenDialog(btnAddCv.getScene().getWindow());

        if(!file.exists()) {
            imgProfile.getStyleClass().add("invalid");
            return;
        }

        imgProfile.setImage(new Image(file.toURI().toURL().toString()));

    }

    @FXML
    void btnAgreementLatterAddOnAction(ActionEvent event) {
        File file = getFile(btnAgreementLatterAdd);

        if(!file.exists()) {
            txtAgreementLatterPath.getStyleClass().add("invalid");
            return;
        };

        txtAgreementLatterPath.setText(file.getAbsolutePath());
        agreementLatter = file;
    }

    @FXML
    void btnAttendanceOnAction(ActionEvent event) {

    }

    @FXML
    void btnBirthCertificateAddOnAction(ActionEvent event) {
        File file = getFile(btnBirthCertificateAdd);

        if(!file.exists()) {
            txtBirthCertificatePath.getStyleClass().add("invalid");
            return;
        };

        txtBirthCertificatePath.setText(file.getAbsolutePath());
        birthCertificate = file;
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        btnNewEmployee.fire();
    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) {

    }

    @FXML
    void btnNewEmployeeOnAction(ActionEvent event) throws URISyntaxException, MalformedURLException {
        List<Node> nodes = Arrays.asList(txtAccountNo,txtAddress,txtBankAccount,
                txtId,txtContact,txtAgreementLatterPath,txtBasicSalary,
                txtBirthCertificatePath,txtBranchName,txtCVPath,
                txtBankAccount,txtFullName,txtNationality,txtNicNo,txtOfferLatterPath,
                txtPassword,txtUserName,calDob,calJoinedDate);
        TextField textField = null;
        DatePicker datePicker = null;
        for (Node node : nodes) {
            if(node instanceof TextField) {
                textField = (TextField) node;
                textField.clear();
                textField.getStyleClass().remove("invalid");
            } else if (node instanceof DatePicker) {
                datePicker = (DatePicker) node;
                datePicker.setValue(null);
                datePicker.getStyleClass().remove("invalid");
            }
        }
        rdoExecutive.setSelected(false);
        rdoNonExecutive.setSelected(false);
        rdoActive.setSelected(false);
        rdoInActive.setSelected(false);
        rdoAdmin.setSelected(false);
        rdoUser.setSelected(false);
        rdoMale.setSelected(false);
        tdoFemale.setSelected(false);
        rdoMarried.setSelected(false);
        rdoUnMarried.setSelected(false);
        chbUnionMember.setSelected(false);
        imgProfile.setImage(new Image(getClass().getResource("/img/profile-pic.jpg").toURI().toURL().toString()));

        cv = null;
        birthCertificate = null;
        offerLatter = null;
        agreementLatter = null;

        txtFullName.requestFocus();
    }

    @FXML
    void btnOfferLatterAddOnAction(ActionEvent event) {
        File file = getFile(btnOfferLatterAdd);

        if(!file.exists()) {
            txtOfferLatterPath.getStyleClass().add("invalid");
            return;
        };

        txtOfferLatterPath.setText(file.getAbsolutePath());
        offerLatter = file;
    }

    @FXML
    void btnPayrollOnAction(ActionEvent event) {

    }

    @FXML
    void btnRemovePicOnAction(ActionEvent event) throws URISyntaxException, MalformedURLException {
        imgProfile.setImage(new Image(getClass().getResource("/img/profile-pic.jpg").toURI().toURL().toString()));
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            if(!checkValidity()) return;

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imgProfile.getImage(), null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            SerialBlob imageBlob = new SerialBlob(bytes);

            FileInputStream fileInputStream = new FileInputStream(cv);
            byte[] cvBytes = fileInputStream.readAllBytes();
            SerialBlob cvBlob = new SerialBlob(cvBytes);

            fileInputStream = new FileInputStream(birthCertificate);
            byte[] birthBytes = fileInputStream.readAllBytes();
            SerialBlob birthBlob = new SerialBlob(birthBytes);

            fileInputStream = new FileInputStream(offerLatter);
            byte[] offerBytes = fileInputStream.readAllBytes();
            SerialBlob offerBlob = new SerialBlob(offerBytes);

            fileInputStream = new FileInputStream(agreementLatter);
            byte[] agreementBytes = fileInputStream.readAllBytes();
            SerialBlob agreementBlob = new SerialBlob(agreementBytes);

            Employee employee = new Employee(
                    txtNicNo.getText(),
                    txtFullName.getText(),
                    Date.valueOf(calDob.getValue()),
                    txtContact.getText(),
                    rdoMale.getToggleGroup().getSelectedToggle().equals(rdoMale)? lk.ijse.dep10.app.enumaration.Gender.MALE : lk.ijse.dep10.app.enumaration.Gender.FEMALE,
                    imageBlob,
                    rdoMarried.getToggleGroup().getSelectedToggle().equals(rdoMarried)? lk.ijse.dep10.app.enumaration.MaritalStatus.MARRIED : lk.ijse.dep10.app.enumaration.MaritalStatus.UNMARRIED,
                    txtAddress.getText(),
                    rdoUser.getToggleGroup().getSelectedToggle().equals(rdoAdmin)? lk.ijse.dep10.app.enumaration.UserType.ADMIN : lk.ijse.dep10.app.enumaration.UserType.USER,
                    txtNationality.getText(),
                    txtUserName.getText(),
                    txtPassword.getText(),
                    rdoExecutive.getToggleGroup().getSelectedToggle().equals(rdoExecutive)? Designation.EXECUTIVE : Designation.NON_EXECUTIVE,
                    Date.valueOf(calJoinedDate.getValue()),
                    chbUnionMember.isSelected(),
                    rdoActive.getToggleGroup().getSelectedToggle().equals(rdoActive)? Status.ACTIVE : Status.INACTIVE,
                    Integer.parseInt(txtBasicSalary.getText()),
                    txtBankAccount.getText(),
                    Integer.parseInt(txtAccountNo.getText()),
                    txtBranchName.getText(),
                    cvBlob,
                    birthBlob,
                    offerBlob,
                    agreementBlob);



            PreparedStatement statement = connection.prepareStatement("INSERT INTO Employee (nic , name, dob, contact, gender, profile_pic, marital_status, address, user_type, nationality, user_name, password, designation, joined_date, union_member, status, basic_salary, bank_name, account_no, branch_name, cv, birth_certificate, offer_letter, agreement_letter) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            statement.setString(1,employee.getNic());
            statement.setString(2,employee.getName());
            statement.setDate(3,employee.getDate());
            statement.setString(4,employee.getContact());
            statement.setString(5,employee.getGender().toString());
            statement.setBlob(6,employee.getProfilePic());
            statement.setString(7,employee.getMaritalStatus().toString());
            statement.setString(8,employee.getAddress());
            statement.setString(9,employee.getUserType().toString());
            statement.setString(10,employee.getNationality());
            statement.setString(11,employee.getUserName());
            statement.setString(12,employee.getPassword());
            statement.setString(13,employee.getDesignation().name());
            statement.setDate(14,employee.getJoinedDate());
            statement.setBoolean(15,employee.isUnionMember());
            statement.setString(16,employee.getStatus().toString());
            statement.setInt(17,employee.getBasic_salary());
            statement.setString(18,employee.getBankName());
            statement.setInt(19,employee.getAccountNo());
            statement.setString(20,employee.getBranchName());
            statement.setBlob(21,employee.getCv());
            statement.setBlob(22,employee.getBirthCertificate());
            statement.setBlob(23,employee.getOfferLatter());
            statement.setBlob(24,employee.getAgreementLatter());

            statement.executeUpdate();

            connection.commit();
            btnNewEmployee.fire();


        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                e.printStackTrace();
            }
            new Alert(Alert.AlertType.ERROR,"Failed to insert entries!, try again.").show();
        }

    }

    private boolean checkValidity() throws URISyntaxException, MalformedURLException {
        List<RadioButton> radioButtons = Arrays.asList(rdoActive, rdoAdmin,rdoMarried, rdoMale,rdoExecutive);
        for (RadioButton radioButton : radioButtons) {
            if(!radioButton.getToggleGroup().getSelectedToggle().isSelected()){
                radioButton.requestFocus();
                isValid = false;
            }
            isValid = true;
        }
        if (!chbUnionMember.isSelected()) {
            chbUnionMember.requestFocus();
            isValid = false;
        }
        if(imgProfile.getImage().getUrl().equals(getClass().getResource("/img/profile-pic.jpg").toURI().toURL().toString())){
            imgProfile.requestFocus();
            isValid = false;
        }
        return isValid;
    }

}
