package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {

    @FXML
    private Button btnAdminLogIn;

    @FXML
    private Button btnEmployeeLogIn;

    @FXML
    private Button btnLogIn;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;

    private boolean adminLogIn;

    @FXML
    void btnAdminLogInOnAction(ActionEvent event) {
        adminLogIn = true;

    }

    @FXML
    void btnEmployeeLogInOnAction(ActionEvent event) {
        adminLogIn = false;

    }

    @FXML
    void btnLogInOnAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) btnLogIn.getScene().getWindow();
        if (adminLogIn) {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminView.fxml"));
            AnchorPane root  =fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("Admin Window");
            stage.show();
            stage.centerOnScreen();

        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/UserView.fxml"));
            AnchorPane root  =fxmlLoader.load();

            stage.setScene(new Scene(root));
            stage.setTitle("User Window");
            stage.show();
            stage.centerOnScreen();

            UserViewController controller = fxmlLoader.getController();
            String[] segments = txtUserName.getText().trim().split("-");
            int id = Integer.parseInt(segments[segments.length-1]);
            controller.getEmployeeId(id);
        }

    }

}
