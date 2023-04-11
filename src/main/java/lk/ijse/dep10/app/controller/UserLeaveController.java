package lk.ijse.dep10.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLeaveController {

    public AnchorPane leaveSheet;
    public AnchorPane leaveApplication;
    public Button btnHome;
    public Button btnBack;
    @FXML
    private Button btnAddApplication;

    @FXML
    private Button btnLeaveSheet;

    @FXML
    private Button btnRemove;

    @FXML
    private TextField txtSearch;

    public void initialize() {
        /*set leaveApplication window as a first window*/
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);
    }

    @FXML
    void btnAddApplicationOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/LeaveApplicationView.fxml"));
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner((Stage) btnAddApplication.getScene().getWindow());
        stage.setTitle("Leave Application");
        stage.show();
        stage.centerOnScreen();


    }

    @FXML
    void btnLeaveSheetOnAction(ActionEvent event) {
        leaveApplication.setVisible(false);
        leaveSheet.setVisible(true);

    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {

    }

    public void btnHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) btnAddApplication.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/UserView.fxml"));
        AnchorPane root = fxmlLoader.load();

        stage.setScene(new Scene(root));
        stage.setTitle("User Window");
        stage.show();
        stage.centerOnScreen();

    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        leaveSheet.setVisible(false);
        leaveApplication.setVisible(true);


    }
}
