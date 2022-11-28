package javabackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private final String driverName = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/drug_database";
    private final String username = "java";
    private final String password = "kiwiWebDev";

    private Connection con = null;

    public ConnectionManager() {
        // If needed, explicitly load driver here
        System.out.println("Loading driver...");
        try {
            Class.forName(driverName);
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }
    }

    public Connection createConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public void closeConnection() {
        try {
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
