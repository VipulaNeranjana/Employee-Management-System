package lk.ijse.dep10.app.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;
import lk.ijse.dep10.app.enumaration.Designation;
import lk.ijse.dep10.app.enumaration.Status;
import lk.ijse.dep10.app.model.Employee;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UpdateEmployeeViewController {

    @FXML
    private Button btnAddNewAgreementLetter;
    @FXML
    private Button btnAddNewOfferLetter;
    @FXML
    private Button btnAddNewBirthCertificate;
    @FXML
    private Button btnAddNewCv;
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
    private Button btnAddNewPic;


    @FXML
    private Button btnAttendance;



    @FXML
    private Button btnEmployee;

    @FXML
    private Button btnLeave;


    @FXML
    private Button btnPayroll;

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
    private RadioButton rdoFemale;

    @FXML
    private TextField txtAccountNo;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtAgreementLetterPath;

    @FXML
    private TextField txtBankName;

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
    private File cv;
    private byte[] oldCv;
    private SerialBlob cvBlob;
    private File birthCertificate;
    private byte[] oldBirthCertificate;
    private SerialBlob birthCertificateBlob;
    private File agreementLetter;
    private byte[] oldAgreementLetter;
    private SerialBlob agreementLetterBlob;
    private File offerLetter;
    private byte[] oldOfferLetter;
    private SerialBlob offerLetterBlob;
    private boolean isValid = true;
    private int employeeId;
    public void getEmployeeId(int id){
        employeeId = id;
    }
    public void initialize(){
        Platform.runLater(() -> {
            Connection connection = DBConnection.getInstance().getConnection();
            try {
                PreparedStatement stm1 = connection.prepareStatement("SELECT * FROM Employee WHERE id = ?");
                stm1.setInt(1, employeeId);
                ResultSet rst1 = stm1.executeQuery();
                if (rst1.next()){
                    txtId.setText(String.valueOf(rst1.getInt("id")));
                    txtFullName.setText(rst1.getString("name"));
                    txtNicNo.setText(rst1.getString("nic"));
                    calDob.setValue(rst1.getDate("dob").toLocalDate());
                    txtContact.setText(rst1.getString("contact"));
                    Gender.selectToggle(rst1.getString("gender").equals("MALE") ? rdoMale : rdoFemale);
                    imgProfile.setImage(new Image(rst1.getBlob("profile_pic").getBinaryStream(), 175, 150, true, true));
                    MaritalStatus.selectToggle(rst1.getString("marital_status").equals("MARRIED") ? rdoMarried : rdoUnMarried);
                    txtAddress.setText(rst1.getString("address"));
                    UserType.selectToggle(rst1.getString("user_type").equals("ADMIN") ? rdoAdmin : rdoUser);
                    txtNationality.setText(rst1.getString("nationality"));

                    txtUserName.setText(rst1.getString("user_name"));
                    txtPassword.setText(rst1.getString("password"));
                    Designation.selectToggle(rst1.getString("designation").equals("EXECUTIVE") ? rdoExecutive : rdoNonExecutive);
                    calJoinedDate.setValue(rst1.getDate("joined_date").toLocalDate());
                    chbUnionMember.setSelected(rst1.getInt("union_member") == 1);
                    Status.selectToggle(rst1.getString("status").equals("ACTIVE") ? rdoActive : rdoInActive);

                    txtBasicSalary.setText(String.valueOf(rst1.getInt("basic_salary")));
                    txtBankName.setText(rst1.getString("bank_name"));
                    txtAccountNo.setText(String.valueOf(rst1.getInt("account_no")));
                    txtBranchName.setText(rst1.getString("branch_name"));

                    txtCVPath.setText(rst1.getBlob("cv").getBinaryStream().readAllBytes().toString().isEmpty() ? "Empty" : "E-" + (rst1.getDate("joined_date").toLocalDate().getYear()) + "-" + (employeeId) + "-cv.file");
                    oldCv = rst1.getBlob("cv").getBinaryStream().readAllBytes();

                    txtBirthCertificatePath.setText(rst1.getBlob("birth_certificate").getBinaryStream().readAllBytes().toString().isEmpty() ? "Empty" : "E-" + (rst1.getDate("joined_date").toLocalDate().getYear()) + "-" + (employeeId) + "-birth-certificate.file");
                    oldBirthCertificate = rst1.getBlob("birth_certificate").getBinaryStream().readAllBytes();

                    txtOfferLetterPath.setText(rst1.getBlob("offer_letter").getBinaryStream().readAllBytes().toString().isEmpty() ? "Empty" : "E-" + (rst1.getDate("joined_date").toLocalDate().getYear()) + "-" + (employeeId) + "-offer-letter.file");
                    oldOfferLetter = rst1.getBlob("offer_letter").getBinaryStream().readAllBytes();

                    txtAgreementLetterPath.setText(rst1.getBlob("agreement_letter").getBinaryStream().readAllBytes().toString().isEmpty() ? "Empty" : "E-" + (rst1.getDate("joined_date").toLocalDate().getYear()) + "-" + (employeeId) + "-agreement-letter.file");
                    oldAgreementLetter = rst1.getBlob("agreement_letter").getBinaryStream().readAllBytes();
                }
            } catch (SQLException | IOException e) {
                new Alert(Alert.AlertType.ERROR, "Unable to load employee details. Try again...!").showAndWait();
                throw new RuntimeException(e);
            }

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
                isValid = false;
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
        txtBankName.textProperty().addListener((value, previous, current) -> {
            txtBankName.getStyleClass().remove("invalid");
            if (!txtBankName.getText().matches("[A-Za-z]{3,}")) {
                txtBankName.getStyleClass().add("invalid");
                txtBankName.requestFocus();
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
    @FXML
    void btnAddNewCvOnAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a new CV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.pdf","*.txt"));
        File file = fileChooser.showOpenDialog(btnAddNewCv.getScene().getWindow());
        cv = file;
        if (file != null){
            txtCVPath.setText(file.getAbsolutePath());
        }
    }
    @FXML
    void btnAddNewBirthCertificateOnAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a new birth certificate");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.pdf","*.txt"));
        File file = fileChooser.showOpenDialog(btnAddNewBirthCertificate.getScene().getWindow());
        birthCertificate = file;
        if (file != null){
            txtBirthCertificatePath.setText(file.getAbsolutePath());
        }
    }
    @FXML
    void btnAddNewOfferLetterOnAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a new offer letter");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.pdf","*.txt"));
        File file = fileChooser.showOpenDialog(btnAddNewOfferLetter.getScene().getWindow());
        offerLetter = file;
        if (file != null){
            txtOfferLetterPath.setText(file.getAbsolutePath());
        }
    }
    @FXML
    void btnAddNewAgreementLetterOnAction(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a new agreement letter");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.pdf","*.txt"));
        File file = fileChooser.showOpenDialog(btnAddNewAgreementLetter.getScene().getWindow());
        agreementLetter = file;
        if (file != null){
            txtAgreementLetterPath.setText(file.getAbsolutePath());
        }
    }
    @FXML
    void btnAddNewPicOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a new profile picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg","*.jpeg","*.png"));
        File file = fileChooser.showOpenDialog(btnAddNewPic.getScene().getWindow());
        if (file != null){
            Image image = null;
            try {
                image = new Image(file.toURI().toURL().toString(), 175, 150, true, true);
            } catch (MalformedURLException e) {
                new Alert(Alert.AlertType.ERROR,"Failed to load profile picture, try again").showAndWait();
                throw new RuntimeException(e);
            }
            imgProfile.setImage(image);
        }
    }

    @FXML
    void btnAttendanceOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnEmployee.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminAttendanceView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Admin Attendance View");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnEmployee.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/EmployeeTableView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Employee Window");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnLeaveOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnEmployee.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminLeaveView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Admin Leave");
        stage.show();
        stage.centerOnScreen();
    }


    @FXML
    void btnPayrollOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnEmployee.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminPayrollView.fxml"));
        AnchorPane root  =fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("Admin Payroll Management");
        stage.show();
        stage.centerOnScreen();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            if (!isValidData()){
                new Alert(Alert.AlertType.ERROR, "Invalid Data. Try again...!").showAndWait();
                return;
            }

            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imgProfile.getImage(), null);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            SerialBlob imageBlob = new SerialBlob(bytes);

            if (txtCVPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(cv.file)")){
                cvBlob = new SerialBlob(oldCv);
            }
            if (!txtCVPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(cv.file)")){
                FileInputStream fis = new FileInputStream(cv);
                cvBlob = new SerialBlob(fis.readAllBytes());
            }

            if (txtBirthCertificatePath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(birth-certificate.file)")){
                birthCertificateBlob = new SerialBlob(oldBirthCertificate);
            }
            if (!txtBirthCertificatePath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(birth-certificate.file)")){
                FileInputStream fis = new FileInputStream(birthCertificate);
                birthCertificateBlob = new SerialBlob(fis.readAllBytes());
            }

            if (txtOfferLetterPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(offer-letter.file)")){
                offerLetterBlob = new SerialBlob(oldOfferLetter);
            }
            if (!txtOfferLetterPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(offer-letter.file)")){
                FileInputStream fis = new FileInputStream(offerLetter);
                offerLetterBlob = new SerialBlob(fis.readAllBytes());
            }

            if (txtAgreementLetterPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(agreement-letter.file)")){
                agreementLetterBlob = new SerialBlob(oldAgreementLetter);
            }
            if (!txtAgreementLetterPath.getText().matches("(E-)(\\d{4})(-)(\\d{1,})(-)(agreement-letter.file)")){
                FileInputStream fis = new FileInputStream(agreementLetter);
                agreementLetterBlob = new SerialBlob(fis.readAllBytes());
            }

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
                    rdoExecutive.getToggleGroup().getSelectedToggle().equals(rdoExecutive)? lk.ijse.dep10.app.enumaration.Designation.EXECUTIVE : lk.ijse.dep10.app.enumaration.Designation.NON_EXECUTIVE,
                    Date.valueOf(calJoinedDate.getValue()),
                    chbUnionMember.isSelected(),
                    rdoActive.getToggleGroup().getSelectedToggle().equals(rdoActive)? lk.ijse.dep10.app.enumaration.Status.ACTIVE : lk.ijse.dep10.app.enumaration.Status.INACTIVE,
                    Integer.parseInt(txtBasicSalary.getText()),
                    txtBankName.getText(),
                    Integer.parseInt(txtAccountNo.getText()),
                    txtBranchName.getText(),
                    cvBlob,
                    birthCertificateBlob,
                    offerLetterBlob,
                    agreementLetterBlob);

            PreparedStatement stm2 = connection.prepareStatement("UPDATE Employee SET nic = ? , name = ?, dob = ?, contact = ?, gender = ?, profile_pic = ?, marital_status = ?, address = ?, user_type = ?, nationality = ?, user_name = ?, password = ?, designation = ?, joined_date = ?, union_member = ?, status = ?, basic_salary = ?, bank_name = ?, account_no = ?, branch_name = ?, cv = ?, birth_certificate = ?, offer_letter = ?, agreement_letter = ? WHERE id = ? ");
            stm2.setString(1,employee.getNic());
            stm2.setString(2,employee.getName());
            stm2.setDate(3,employee.getDate());
            stm2.setString(4,employee.getContact());
            stm2.setString(5,employee.getGender().toString());
            stm2.setBlob(6,employee.getProfilePic());
            stm2.setString(7,employee.getMaritalStatus().toString());
            stm2.setString(8,employee.getAddress());
            stm2.setString(9,employee.getUserType().toString());
            stm2.setString(10,employee.getNationality());
            stm2.setString(11,employee.getUserName());
            stm2.setString(12,employee.getPassword());
            stm2.setString(13,employee.getDesignation().name());
            stm2.setDate(14,employee.getJoinedDate());
            stm2.setBoolean(15,employee.isUnionMember());
            stm2.setString(16,employee.getStatus().toString());
            stm2.setInt(17,employee.getBasic_salary());
            stm2.setString(18,employee.getBankName());
            stm2.setInt(19,employee.getAccountNo());
            stm2.setString(20,employee.getBranchName());
            stm2.setBlob(21,employee.getCv());
            stm2.setBlob(22,employee.getBirthCertificate());
            stm2.setBlob(23,employee.getOfferLatter());
            stm2.setBlob(24,employee.getAgreementLatter());
            stm2.setInt(25,employeeId);

            stm2.executeUpdate();
            connection.commit();
        } catch (Throwable e) {
            try {
                connection.rollback();
            }
            catch (SQLException ex){
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
        finally {
            try {
                connection.setAutoCommit(true);
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
    private boolean isValidData(){
        boolean isValid = true;
        if (txtFullName.getStyleClass().contains("invalid") || txtNicNo.getStyleClass().contains("invalid") || calDob.getStyleClass().contains("invalid") || txtContact.getStyleClass().contains("invalid") || txtAddress.getStyleClass().contains("invalid") || txtNationality.getStyleClass().contains("invalid") || txtUserName.getStyleClass().contains("invalid") || txtPassword.getStyleClass().contains("invalid") || calJoinedDate.getStyleClass().contains("invalid") || txtBasicSalary.getStyleClass().contains("invalid") || txtBankName.getStyleClass().contains("invalid") || txtAccountNo.getStyleClass().contains("invalid") || txtBranchName.getStyleClass().contains("invalid")){
            isValid = false;
        }
        return isValid;
    }

}

