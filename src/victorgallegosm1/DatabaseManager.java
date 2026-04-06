/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author 12546
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/vgallegossp252338?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "sesame80";

    private static Connection connection;
    private static boolean isConnected = false;

    private static final List<ConnectionListener> listeners = new ArrayList<>();

    static {
        connect(); // try to connect on startup
        startConnectionChecker(); 
    }

    public static synchronized Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(2)) {
                connect();  // notify listeners if status changes
            }
        } catch (SQLException e) {
            System.err.println("Error checking DB connection: " + e.getMessage());
            connect(); // Fallback
        }
        return connection;
    }

    private static synchronized void connect() {
        try {
            Connection newConnection = DriverManager.getConnection(URL, USER, PASSWORD);
            boolean statusChanged = !isConnected;  // only print/log if connection was previously down
            connection = newConnection;
            isConnected = true;

            if (statusChanged) {
                System.out.println("Connected to database.");
                notifyConnectionStatusChanged(true);
            }

        } catch (SQLException e) {
            if (isConnected) {
                System.err.println("Database connection lost: " + e.getMessage());
            }
            isConnected = false;
            notifyConnectionStatusChanged(false);
        }
    }

    public static boolean isConnected() {
        return isConnected;
    }

    private static void startConnectionChecker() {
        Thread checker = new Thread(() -> {
            boolean lastStatus = isConnected;

            while (true) {
                try {
                    Thread.sleep(4000);

                    boolean valid = (connection != null && connection.isValid(2));
                    if (valid != lastStatus) {
                        // Only print if the connection state changes
                        System.out.println(valid ? "Database reconnected." : "Connection lost, attempting to reconnect...");
                        notifyConnectionStatusChanged(valid);
                        lastStatus = valid;
                    }

                    if (!valid) {
                        connect(); // Attempt reconnect
                    }

                } catch (InterruptedException e) {
                    System.out.println("Connection checker interrupted.");
                    break;  // exit thread if interrupted
                } catch (SQLException e) {
                    isConnected = false;
                    notifyConnectionStatusChanged(false);
                    System.err.println("Error checking DB connection: " + e.getMessage());
                }
            }
        });

        checker.setDaemon(true);
        checker.start();
    }

    // Listener interface
    public interface ConnectionListener {

        void onStatusChanged(boolean connected);
    }

    public static void addConnectionListener(ConnectionListener listener) {
        listeners.add(listener);
    }

    private static void notifyConnectionStatusChanged(boolean connected) {
        for (ConnectionListener listener : listeners) {
            listener.onStatusChanged(connected);
        }
    }
}
