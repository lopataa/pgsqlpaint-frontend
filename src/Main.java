import database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        // Set macOS-specific properties before any UI initialization
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.application.name", "pgsqlPaint"); // For modern macOS versions

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            //..
        }

        Connection conn = DatabaseManager.getConnection();



        MainFrame frame = new MainFrame();
        MenuBar menuBar = new MenuBar();
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
}
