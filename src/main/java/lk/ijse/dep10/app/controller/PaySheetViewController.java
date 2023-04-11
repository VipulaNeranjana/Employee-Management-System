package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class PaySheetViewController {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnPrint;

    @FXML
    private Button btnView;

    @FXML
    private DatePicker txtSelectedDate;

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
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnViewOnAction(ActionEvent event) {

    }

}

