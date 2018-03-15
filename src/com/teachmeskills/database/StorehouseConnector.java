package com.teachmeskills.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StorehouseConnector {
    private static final String URL = "jdbc:mysql://localhost/storehouse";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection conection;

    public static Connection getConection() {
        if (conection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conection = DriverManager.getConnection(URL, USER, PASSWORD);
                return conection;
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conection;
    }

    public static void close() {
        if (conection != null) {
            try {
                conection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
