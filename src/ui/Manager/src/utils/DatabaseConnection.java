package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "res\\config.properties";

    public static Connection getConnection() {
        Properties properties = new Properties();

        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            // Load properties from the file
            properties.load(input);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            // Return a connection
            return DriverManager.getConnection(url, user, password);

        } catch (IOException e) {
            System.out.println("Error reading configuration file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        return null;
    }
}
