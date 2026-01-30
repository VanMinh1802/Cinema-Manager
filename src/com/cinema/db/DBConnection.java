package com.cinema.db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    public enum DBMode {
        LOCAL, CLOUD
    }

    // --- CONFIGURATION ---
    private static final String CONFIG_FILE = "config.properties";
    private static final String DB_PROPS_FILE = "database.properties";
    private static final String KEY_DB_MODE = "db.mode";
    private static DBMode currentMode = DBMode.CLOUD; // Default

    // --- CREDENTIALS (Loaded from file) ---
    private static String cloudHost;
    private static String cloudPort;
    private static String cloudDb;
    private static String cloudUser;
    private static String cloudPass;

    private static String localHost;
    private static String localPort;
    private static String localDb;
    private static String localUser;
    private static String localPass;

    private static Connection instance = null;

    static {
        loadConfig();
        loadDatabaseProps();
    }

    public static Connection getConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                return instance;
            }
        } catch (SQLException e) {
            // Check failed
        }

        return createConnection();
    }

    private static Connection createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url, user, pass;

            if (currentMode == DBMode.LOCAL) {
                // Local URL
                url = "jdbc:mysql://" + localHost + ":" + localPort + "/" + localDb
                        + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
                user = localUser;
                pass = localPass;
                System.out.println(">>> Connecting to LOCAL Database (" + localHost + ")...");
            } else {
                // Cloud URL
                url = "jdbc:mysql://" + cloudHost + ":" + cloudPort + "/" + cloudDb
                        + "?useUnicode=true&characterEncoding=UTF-8&sslMode=REQUIRED";
                user = cloudUser;
                pass = cloudPass;
                System.out.println(">>> Connecting to CLOUD Database (" + cloudHost + ")...");
            }

            instance = DriverManager.getConnection(url, user, pass);
            // System.out.println(">>> Connection Established Successfully!");

            checkSchema(instance); // Self-healing

        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL Driver not found!");
            throw new RuntimeException("MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error: Failed to connect to " + currentMode + " Database!");
            throw new RuntimeException("Connection failed (" + currentMode + "): " + e.getMessage());
        }
        return instance;
    }

    private static void checkSchema(Connection conn) {
        String createTable = "CREATE TABLE IF NOT EXISTS QuyDinhHoiVien (" +
                "config_key VARCHAR(50) PRIMARY KEY, " +
                "config_value VARCHAR(255))";

        String initData1 = "INSERT IGNORE INTO QuyDinhHoiVien (config_key, config_value) VALUES ('POINTS_PER_10K', '1')";
        String initData2 = "INSERT IGNORE INTO QuyDinhHoiVien (config_key, config_value) VALUES ('POINT_VALUE_VND', '1000')";

        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTable);
            stmt.executeUpdate(initData1);
            stmt.executeUpdate(initData2);
        } catch (SQLException e) {
            // Silently fail or log, usually permissive
            System.err.println("Schema check warning: " + e.getMessage());
        }
    }

    public static void switchMode(DBMode newMode) {
        if (currentMode != newMode) {
            System.out.println(">>> Switching Database Mode to: " + newMode);
            currentMode = newMode;
            // Close existing connection so next call creates a new one
            try {
                if (instance != null && !instance.isClosed()) {
                    instance.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            instance = null;
            saveConfig();
        }
    }

    public static DBMode getCurrentMode() {
        return currentMode;
    }

    private static void loadConfig() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
            String modeStr = props.getProperty(KEY_DB_MODE);
            if (modeStr != null) {
                currentMode = DBMode.valueOf(modeStr);
            }
        } catch (IllegalArgumentException | IOException e) {
            // Default to Cloud if file missing or invalid
            System.out.println(">>> No valid config found, using default: " + currentMode);
        }
    }

    private static void saveConfig() {
        Properties props = new Properties();
        props.setProperty(KEY_DB_MODE, currentMode.name());
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "CinemaManager Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDatabaseProps() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(DB_PROPS_FILE)) {
            props.load(in);
            cloudHost = props.getProperty("cloud.host");
            cloudPort = props.getProperty("cloud.port");
            cloudDb = props.getProperty("cloud.db");
            cloudUser = props.getProperty("cloud.user");
            cloudPass = props.getProperty("cloud.pass");
            localHost = props.getProperty("local.host");
            localPort = props.getProperty("local.port");
            localDb = props.getProperty("local.db");
            localUser = props.getProperty("local.user");
            localPass = props.getProperty("local.pass");
        } catch (IOException e) {
            System.err.println("Error loading database properties: " + e.getMessage());
            // Fallback hardcoded for dev env if file missing (optional, but unsafe for git)
        }
    }
}
