package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        generateSchemaIfNotExist();

    }

    private void generateSchemaIfNotExist() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            Statement stm = connection.createStatement();
            ResultSet rst = stm.executeQuery("SHOW TABLES");

            HashSet<String> tableNameList = new HashSet<>();

            while (rst.next()){
                tableNameList.add(rst.getString(1));
            }

            boolean tableExists = tableNameList.containsAll(Set.of("Employee","Status","Attendance","Leaves","Leave_Description","Payroll"));
            if (!tableExists){
                stm.execute(readDBScript());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Cannot create tables, try again...!").showAndWait();
            throw new RuntimeException(e);
        }
    }
    private String readDBScript(){
        InputStream is = getClass().getResourceAsStream("/schema.sql");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){
            String line;
            StringBuilder dbScriptBuilder = new StringBuilder();
            while((line  = br.readLine()) != null){
                dbScriptBuilder.append(line);
            }
            return dbScriptBuilder.toString();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
