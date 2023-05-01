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
import lk.ijse.dep10.app.db.DBConnection;

import java.io.IOException;
import java.sql.*;

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
        String userName = txtUserName.getText();
        String passWord = txtPassword.getText();
        Stage stage = (Stage) btnLogIn.getScene().getWindow();
        if (adminLogIn) {
            if (userName.equals("USERNAME")&& passWord.equals("PASSWORD")){
                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/AdminView.fxml"));
                AnchorPane root  =fxmlLoader.load();

                stage.setScene(new Scene(root));
                stage.setTitle("Admin Window");
                stage.show();
                stage.centerOnScreen();
            }
        } else {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                String sql="select * from Employee where user_name=? ";
                PreparedStatement stm=connection.prepareStatement(sql);
                stm.setString(1,userName);
                ResultSet rst = stm.executeQuery();
                if(!rst.next()){
                    txtUserName.getStyleClass().add("invalid");
                    txtPassword.getStyleClass().add("invalid");
                    txtUserName.requestFocus();
                    txtUserName.selectAll();
                }else{
                    if(!passWord.equals(rst.getString("password"))  ){
                        txtUserName.getStyleClass().add("invalid");
                        txtPassword.getStyleClass().add("invalid");
                        txtUserName.requestFocus();
                        txtUserName.selectAll();
                        return;
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/UserView.fxml"));
                    AnchorPane root  =fxmlLoader.load();
                    stage.setScene(new Scene(root));
                    stage.setTitle("User Window");
                    stage.show();
                    stage.setMaximized(true);
                    stage.centerOnScreen();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

}
