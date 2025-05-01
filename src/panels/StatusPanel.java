package panels;

import database.DatabaseManager;
import database.State;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private final JLabel connectionCountLabel;
    private final JLabel actionLabel;
    private final JLabel loadingLabel;
    private final Timer refreshTimer;

    public StatusPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createEtchedBorder());

        // Connection count components
        connectionCountLabel = new JLabel("Connections: 0");
        add(connectionCountLabel);

        // State components
        actionLabel = new JLabel("Action: None");
        add(actionLabel);

        loadingLabel = new JLabel("Loading: false");
        add(loadingLabel);

        // Refresh button (optional now, since we have auto-updates)
        JButton refreshButton = new JButton("↻");
        refreshButton.addActionListener(e -> updateStatus());
        add(refreshButton);

        // Create timer for automatic updates (every 500ms)
        refreshTimer = new Timer(500, e -> updateStatus());
        refreshTimer.start();
    }

    private void updateStatus() {
        SwingUtilities.invokeLater(() -> {
            // Update connection count
            int count = DatabaseManager.getActiveConnectionCount();
            connectionCountLabel.setText("Connections: " + count);

            // Update state information
            String currentAction = State.getAction();
            boolean isLoading = State.getIsLoading();
            actionLabel.setText("Action: " + currentAction);
            loadingLabel.setText("Loading: " + isLoading);

            // Visual feedback for loading state
            if (isLoading) {
                loadingLabel.setForeground(Color.ORANGE);
            } else {
                loadingLabel.setForeground(Color.BLACK);
            }
        });
    }

    // Clean up when panel is no longer needed
    @Override
    public void removeNotify() {
        super.removeNotify();
        refreshTimer.stop();
    }
}
