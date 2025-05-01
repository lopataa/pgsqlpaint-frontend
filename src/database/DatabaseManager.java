package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.sql.*;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    private static final ExecutorService repaintNotificationExecutor =
            Executors.newSingleThreadExecutor();
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(DatabaseManager.class);
    private static Connection repaintListenConnection; // Dedicated connection for notifications

    static {
        // ask for ip using swing prompt thingie
        String ip = IPAddressPrompt.promptForIPAddress();
        config.setJdbcUrl("jdbc:postgresql://" + ip +":5432/draw");
        config.setUsername("postgres");
        config.setPassword("postgres");
        // Optimal settings for PostgreSQL
        config.setMaximumPoolSize(10);            // Match DB max_connections / instance_count
        config.setConnectionTimeout(60000);        // 5 seconds (balance between UX and errors)
        config.setIdleTimeout(300000);            // 5 minutes
        config.setMaxLifetime(540000);            // 9 minutes (below PG's 10m default)
        config.setLeakDetectionThreshold(20000);  // 10 seconds to find leaks
        config.setValidationTimeout(2000);        // 2 seconds connection test
        ds = new HikariDataSource(config);

        initializeRepaintNotificationListener();
    }

    private static void initializeRepaintNotificationListener() {
        repaintNotificationExecutor.execute(() -> {
            try (Connection conn = ds.getConnection()) {
                repaintListenConnection = conn;
                Statement stmt = conn.createStatement();
                stmt.execute("LISTEN repaint");

                while (!Thread.currentThread().isInterrupted()) {
                    PGNotification[] notifications = (conn.unwrap(PGConnection.class)).getNotifications(5000);
                    if (notifications != null) {
                        for (PGNotification notification : notifications) {
                            handleRepaintNotification(notification);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private static final ConcurrentHashMap<String, Consumer<String>> repaintListeners = new ConcurrentHashMap<>();

    public static void addRepaintNotificationListener(String channel, Consumer<String> callback) {
        repaintListeners.put(channel, callback);
    }

    private static void handleRepaintNotification(PGNotification notification) {
        System.out.println(notification);
        Consumer<String> callback = repaintListeners.get(notification.getName());
        if (callback != null) {
            callback.accept(notification.getParameter());
        }
    }

    public static void sendNotification(String channel, String message) throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("NOTIFY " + channel + ", '" + message.replace("'", "''") + "'");
        }
    }

    public static int getActiveConnectionCount() {
        return ds.getHikariPoolMXBean().getActiveConnections();
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = ds.getConnection();
            conn.prepareStatement("SET statement_timeout = '60s';").executeUpdate();
            return conn;
        } catch (SQLException e) {
            // show a message box
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }
}
