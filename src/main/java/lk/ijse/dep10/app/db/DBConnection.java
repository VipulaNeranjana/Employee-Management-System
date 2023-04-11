package lk.ijse.dep10.app.db;


import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private final Connection connection;


    private DBConnection() {

        try {
            File file = new File("application.properties");
            FileReader fileReader = new FileReader(file);
            Properties properties = new Properties();
            properties.load(fileReader);
            fileReader.close();

            String host = properties.getProperty("mysql.host", "localhost");
            String port = properties.getProperty("mysql.port", "3306");
            String database = properties.getProperty("mysql.database", "hrm_database");
            String username = properties.getProperty("mysql.username", "root");
            String password = properties.getProperty("mysql.password", "");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database +
                    "?createDatabaseIfNotExist=true&allowMultiQueries=true";
            connection = DriverManager.getConnection(url, username, password);


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }

    public static DBConnection getInstance() {
        return (instance == null) ? instance = new DBConnection() : instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
