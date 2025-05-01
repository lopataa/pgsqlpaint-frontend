package panels;

import database.DatabaseManager;
import database.state.CurrentRenderer;
import database.state.CurrentTool;
import database.state.Renderer;
import panels.editor.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.time.chrono.Era;
import java.util.concurrent.ExecutionException;


public class EditorPanel extends JPanel {
    private BufferedImage currentImage;
    public JLabel imageLabel;
    private MoveMouseListener moveMouseListener;

    public EditorPanel() {
        setLayout(new BorderLayout());

        // Initialize image label
        imageLabel = new ImageLabel(this);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(imageLabel, BorderLayout.CENTER);

        imageLabel.addMouseListener(new LineMouseListener(this));
        imageLabel.addMouseListener(new FillMouseListener(this));
        imageLabel.addMouseListener(new PolygonMouseListener(this));
        imageLabel.addMouseListener(new SquareMouseListener(this));
        imageLabel.addMouseListener(new RectMouseListener(this));
        imageLabel.addMouseListener(new CircleMouseListener(this));
        DrawMouseListener drawMouseListener = new DrawMouseListener(this);
        imageLabel.addMouseListener(drawMouseListener);
        EraserMouseListener eraserMouseListener = new EraserMouseListener(this);
        imageLabel.addMouseListener(eraserMouseListener);
        imageLabel.addMouseMotionListener(eraserMouseListener);
        imageLabel.addMouseMotionListener(drawMouseListener);
        moveMouseListener = new MoveMouseListener(this);
        imageLabel.addMouseListener(moveMouseListener);
        imageLabel.addMouseMotionListener(moveMouseListener);
        imageLabel.addMouseListener(new TextMouseListener(this));

        setupNotificationListener();
        loadInitialImage();
    }


    private void setupNotificationListener() {
        DatabaseManager.addRepaintNotificationListener("repaint", payload -> {
            SwingUtilities.invokeLater(this::loadImageFromDatabase);
        });
    }

    private void loadInitialImage() {
        loadImageFromDatabase();
    }

    private volatile SwingWorker<BufferedImage, Void> currentWorker;

    private void loadImageFromDatabase() {
        // Cancel previous worker if exists
        if (currentWorker != null && !currentWorker.isDone()) {
            currentWorker.cancel(true); // true forces interruption
        }

        imageLabel.setText("");

        currentWorker = new SwingWorker<BufferedImage, Void>() {
            private Connection conn;
            private Statement stmt;
            private ResultSet rs;

            @Override
            protected BufferedImage doInBackground() throws Exception {
                System.out.println("Loading image...");
                try {
                    conn = DatabaseManager.getConnection();
                    stmt = conn.createStatement();

                    // Set timeout to prevent hung queries
                    stmt.execute("SET statement_timeout TO 3000"); // 3 seconds

                    if(CurrentRenderer.getRenderer() == Renderer.BMP) {
                        rs = stmt.executeQuery("SELECT * FROM public.render_canvas_to_bmp()");
                    } else {
                        rs = stmt.executeQuery("SELECT * FROM public.render_canvas_to_png()");
                    }

                    if (isCancelled()) return null;

                    if (rs.next()) {
                        byte[] imageData = rs.getBytes(1);
                        if (isCancelled()) return null;
                        return ImageIO.read(new ByteArrayInputStream(imageData));
                    }
                    return null;
                } finally {
                    // Ensure resources are closed on cancellation
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                }
            }

            @Override
            protected void done() {
                try {
                    if (!isCancelled()) {
                        currentImage = get();
                        if (currentImage != null) {
                            updateImageDisplay();
                        } else {
                            imageLabel.setText("No image data");
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    if (!isCancelled()) { // Only show errors if not canceled
                        imageLabel.setText("Error: " + e.getCause().getMessage());
                    }
                }
            }
        };
        currentWorker.execute();
    }

    public double getImageScale() {
        if (currentImage == null) return 1.0;
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = currentImage.getWidth();
        int imgHeight = currentImage.getHeight();
        return Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight);
    }

    public Point getImagePosition() {
        if (currentImage == null) return new Point(0, 0);
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = (int) (currentImage.getWidth() * getImageScale());
        int imgHeight = (int) (currentImage.getHeight() * getImageScale());
        return new Point((panelWidth - imgWidth) / 2, (panelHeight - imgHeight) / 2);
    }

    private void updateImageDisplay() {
        if (currentImage == null) return;

        // Calculate maximum dimensions while maintaining aspect ratio
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = currentImage.getWidth();
        int imgHeight = currentImage.getHeight();

        // Calculate scaling factors for both dimensions
        double widthRatio = (double) panelWidth / imgWidth;
        double heightRatio = (double) panelHeight / imgHeight;

        // Use the smaller scaling factor to maintain aspect ratio
        double scale = Math.min(widthRatio, heightRatio);

        // Calculate final dimensions
        int newWidth = (int) (imgWidth * scale);
        int newHeight = (int) (imgHeight * scale);

        // Scale the image using nearest-neighbor interpolation
        BufferedImage scaledImage = scaleImageNearestNeighbor(currentImage, newWidth, newHeight);

        // Create icon and set it to the label
        ImageIcon icon = new ImageIcon(scaledImage);
        imageLabel.setIcon(icon);

        // Center the image in the panel
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        revalidate();
        repaint();
    }

    private BufferedImage scaleImageNearestNeighbor(BufferedImage original, int newWidth, int newHeight) {
        BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, original.getType());

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                // Map the scaled pixel to the nearest neighbor in the original image
                int srcX = x * original.getWidth() / newWidth;
                int srcY = y * original.getHeight() / newHeight;
                int rgb = original.getRGB(srcX, srcY);
                scaledImage.setRGB(x, y, rgb);
            }
        }

        return scaledImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (CurrentTool.getCurrentTool().equals("move")) {
            Graphics2D g2d = (Graphics2D) g.create();
            try {
                moveMouseListener.drawPreview(g2d);
            } finally {
                g2d.dispose();
            }
        }
    }
}
