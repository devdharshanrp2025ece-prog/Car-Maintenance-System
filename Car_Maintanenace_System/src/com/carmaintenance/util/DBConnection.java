package com.carmaintenance.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.carmaintenance.exception.DatabaseException;

public final class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_maintenance?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "SECE";

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseException("Unable to connect to MySQL. Check server, database, driver, username and password.", e);
        }
    }
}
