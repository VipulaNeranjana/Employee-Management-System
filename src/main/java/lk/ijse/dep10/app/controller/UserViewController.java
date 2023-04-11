package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserViewController {

    @FXML
    private Button btnLeave;

    @FXML
    private Button btnPaySheet;

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
    }

}
