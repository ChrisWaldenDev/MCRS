package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.MCRS;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static Connection connection;
    private static final String CREATE_PLAYER_SKILLS_TABLE = "CREATE TABLE IF NOT EXISTS player_skills (" +
            "uuid TEXT, skill TEXT, experience REAL, PRIMARY KEY(uuid, skill))";

    public static void connect() {

        try {
            File folder = MCRS.getInstance().getDataFolder();

            if (!folder.exists()) {
                boolean created = folder.mkdirs();
                if (!created) {
                    Bukkit.getLogger().severe("Couldn't create folder " + folder.getAbsolutePath());
                }
            }

            File dbFile = new File(folder, "data.db");
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            if (connection == null || connection.isClosed()) connection = DriverManager.getConnection(url);

            try (Statement statement = connection.createStatement()) {
                statement.execute(CREATE_PLAYER_SKILLS_TABLE);
            }

        } catch (SQLException e) {
            Bukkit.getLogger().severe("Database connection or table creation failed");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
