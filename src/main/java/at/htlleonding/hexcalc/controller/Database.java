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

    public static int addSave(SaveData saveData) {
        String sql = "INSERT INTO saves (points, upgradeAmount, hasWon) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, saveData.points);
            pstmt.setInt(2, saveData.upgradeAmount);
            pstmt.setBoolean(3, saveData.hasWon);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteSave(int id) {
        String sql = "DELETE FROM saves WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<SaveData> getAllSaves() {
        String sql = "SELECT id, points, upgradeAmount, hasWon FROM saves";
        try (Statement stmt = dbConnection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<SaveData> saves = new ArrayList<>();
            while (rs.next()) {
                SaveData saveData = new SaveData();
                saveData.id = rs.getInt("id");
                saveData.points = rs.getLong("points");
                saveData.upgradeAmount = rs.getInt("upgradeAmount");
                saveData.hasWon = rs.getBoolean("hasWon");
                saves.add(saveData);
            }
            return saves;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void updateSave(SaveData saveData) {
        String sql = "UPDATE saves SET points = ?, upgradeAmount = ?, hasWon = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(sql)) {
            pstmt.setLong(1, saveData.points);
            pstmt.setInt(2, saveData.upgradeAmount);
            pstmt.setBoolean(3, saveData.hasWon);
            pstmt.setInt(4, saveData.id);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
