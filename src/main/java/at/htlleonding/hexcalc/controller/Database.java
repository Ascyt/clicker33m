package at.htlleonding.hexcalc.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private static Connection dbConnection;

    public static void initialize() {
        System.out.println("= Opening connection. =");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:derby:db");
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbConnection = connection;
    }

    public static void close() {
        System.out.println("= Closing connection. =");
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        }
        catch (Exception ex) {

        }
    }
}
