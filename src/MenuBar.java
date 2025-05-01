import database.functions.ClearCanvas;
import database.state.*;
import database.state.Renderer;
import menu.ExportToBmp;
import menu.ExportToPng;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuBar extends JMenuBar {
    public MenuBar() {
        // File menu
        JMenu fileMenu = new JMenu("File");

        // Add a new menu for color selection
        JMenu colorMenu = new JMenu("Color");

// Create a menu item that opens a color chooser dialog
        JMenuItem chooseColorItem = new JMenuItem(new AbstractAction("Choose Color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(null, "Choose a Color", new Color(CurrentColor.getRed(), CurrentColor.getGreen(), CurrentColor.getBlue()));
                if (selectedColor != null) {
                    // Handle the selected color here (e.g., set it in your drawing tool)
                    CurrentColor.setColor(selectedColor.getRed(), selectedColor.getGreen(), selectedColor.getBlue());
                }
            }
        });

// Add the menu item to the color menu
        colorMenu.add(chooseColorItem);


        // Clear canvas menu item
        JMenuItem clearCanvasItem = new JMenuItem(new AbstractAction("New Canvas") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showClearCanvasDialog();
            }
        });


        fileMenu.add(clearCanvasItem);
        this.add(fileMenu);

        // Current tool
        // Current tool
        JMenu toolsMenu = new JMenu("Tool");
// Create a button group to ensure only one tool can be selected
        ButtonGroup toolGroup = new ButtonGroup();

// Create radio button menu items
        JRadioButtonMenuItem lineItem = new JRadioButtonMenuItem(new AbstractAction("Line") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("line");
            }
        });

        JRadioButtonMenuItem fillItem = new JRadioButtonMenuItem(new AbstractAction("Fill") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("fill");
            }
        });

        JRadioButtonMenuItem drawItem = new JRadioButtonMenuItem(new AbstractAction("Draw") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("draw");
            }
        });

        JRadioButtonMenuItem textItem = new JRadioButtonMenuItem(new AbstractAction("Text (uses a third-party library)") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("text");
            }
        });

        JRadioButtonMenuItem moveItem = new JRadioButtonMenuItem(new AbstractAction("Move") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("move");
            }
        });
        JRadioButtonMenuItem circleItem = new JRadioButtonMenuItem(new AbstractAction("Circle") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("circle");
            }
        });

        JRadioButtonMenuItem rectangleItem = new JRadioButtonMenuItem(new AbstractAction("Rectangle") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("rectangle");
            }
        });

        JRadioButtonMenuItem squareItem = new JRadioButtonMenuItem(new AbstractAction("Square") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("square");
            }
        });

        JRadioButtonMenuItem eraserItem = new JRadioButtonMenuItem(new AbstractAction("Eraser") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("eraser");
            }
        });

        JRadioButtonMenuItem polygonItem = new JRadioButtonMenuItem(new AbstractAction("Polygon") {
            @Override
            public void actionPerformed(ActionEvent e) {
                CurrentTool.setCurrentTool("polygon");
            }
        });


// Add items to button group
        toolGroup.add(lineItem);
        toolGroup.add(fillItem);
        toolGroup.add(drawItem);
        toolGroup.add(textItem);
        // Add new items to the tool group
        toolGroup.add(circleItem);
        toolGroup.add(rectangleItem);
        toolGroup.add(squareItem);
        toolGroup.add(eraserItem);
        toolGroup.add(polygonItem);
        toolGroup.add(moveItem);

// Add new items to the tools menu
        toolsMenu.add(circleItem);
        toolsMenu.add(rectangleItem);
        toolsMenu.add(squareItem);
        toolsMenu.add(eraserItem);
        toolsMenu.add(polygonItem);

// Set default selection
        // Update the switch statement to handle new default selections
        switch (CurrentTool.getCurrentTool()) {
            case "line" -> lineItem.setSelected(true);
            case "fill" -> fillItem.setSelected(true);
            case "draw" -> drawItem.setSelected(true);
            case "text" -> textItem.setSelected(true);
            case "move" -> moveItem.setSelected(true);
            case "eraser" -> eraserItem.setSelected(true);
            case "polygon" -> polygonItem.setSelected(true);
            case "circle" -> circleItem.setSelected(true);
            case "rectangle" -> rectangleItem.setSelected(true);
            case "square" -> squareItem.setSelected(true);
            default -> {
                lineItem.setSelected(true);
                CurrentTool.setCurrentTool("line");
            }
        }

// Add items to menu
        toolsMenu.add(fillItem);
        toolsMenu.add(lineItem);
        toolsMenu.add(drawItem);
        toolsMenu.add(textItem);
        toolsMenu.add(moveItem);


// Add tools menu to menu bar
        this.add(toolsMenu);
        // Add the color menu to the menu bar
        this.add(colorMenu);

        // === Renderer Selection Menu ===
        JMenu rendererMenu = new JMenu("Renderer");
        ButtonGroup rendererGroup = new ButtonGroup();

        JRadioButtonMenuItem pngItem = new JRadioButtonMenuItem("PNG (renders 2x faster, uses less bandwidth, but uses a third-party library in plpython3u)");
        JRadioButtonMenuItem bmpItem = new JRadioButtonMenuItem("BMP (renders slower, uses much more bandwidth, but is fully written in sql)");

        // Set initial selection based on CurrentRenderer
        if (CurrentRenderer.getRenderer() == Renderer.PNG) {
            pngItem.setSelected(true);
        } else {
            bmpItem.setSelected(true);
        }

        // Action listeners to update renderer state
        pngItem.addActionListener(e -> CurrentRenderer.setColor(Renderer.PNG));
        bmpItem.addActionListener(e -> CurrentRenderer.setColor(Renderer.BMP));

        rendererGroup.add(pngItem);
        rendererGroup.add(bmpItem);

        rendererMenu.add(pngItem);
        rendererMenu.add(bmpItem);

        // === Line Style Selection Menu ===
        JMenu lineStyleMenu = new JMenu("Line Style");
        ButtonGroup lineStyleGroup = new ButtonGroup();

        JRadioButtonMenuItem solidItem = new JRadioButtonMenuItem("Solid");
        JRadioButtonMenuItem dashedItem = new JRadioButtonMenuItem("Dashed");
        JRadioButtonMenuItem dottedItem = new JRadioButtonMenuItem("Dotted");

// Set initial selection based on CurrentLineStyle
        switch (CurrentLineStyle.getStroke()) {
            case SOLID -> solidItem.setSelected(true);
            case DASHED -> dashedItem.setSelected(true);
            case DOTTED -> dottedItem.setSelected(true);
        }

// Action listeners to update line style state
        solidItem.addActionListener(e -> CurrentLineStyle.setStroke(LineStyle.SOLID));
        dashedItem.addActionListener(e -> CurrentLineStyle.setStroke(LineStyle.DASHED));
        dottedItem.addActionListener(e -> CurrentLineStyle.setStroke(LineStyle.DOTTED));

        lineStyleGroup.add(solidItem);
        lineStyleGroup.add(dashedItem);
        lineStyleGroup.add(dottedItem);

        lineStyleMenu.add(solidItem);
        lineStyleMenu.add(dashedItem);
        lineStyleMenu.add(dottedItem);

// Add line style menu to menu bar
        this.add(lineStyleMenu);

        // === Stroke Width Menu ===
        JMenu strokeWidthMenu = new JMenu("Stroke Width");

// Menu item to open the stroke width dialog
        JMenuItem setStrokeWidthItem = new JMenuItem(new AbstractAction("Set Stroke Width...") {
            @Override
            public void actionPerformed(ActionEvent e) {
                showStrokeWidthDialog();
            }
        });

        strokeWidthMenu.add(setStrokeWidthItem);
        this.add(strokeWidthMenu);


        // Add renderer menu to menu bar
        this.add(rendererMenu);

        fileMenu.add(ExportToPng.createMenuItem());
        fileMenu.add(ExportToBmp.createMenuItem());

    }

    private void showClearCanvasDialog() {
        // Create the input fields
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(800, 100, 5000, 10));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(600, 100, 5000, 10));

        // Create the panel
        JPanel panel = new JPanel();
        panel.add(new JLabel("Width:"));
        panel.add(widthSpinner);
        panel.add(Box.createHorizontalStrut(15)); // spacing
        panel.add(new JLabel("Height:"));
        panel.add(heightSpinner);

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(null, panel, "Create New Canvas", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        // Process the result
        if (result == JOptionPane.OK_OPTION) {
            int width = (Integer) widthSpinner.getValue();
            int height = (Integer) heightSpinner.getValue();
            ClearCanvas.clearCanvas(width, height);
        }
    }

    private void showStrokeWidthDialog() {
        // The spinner shows the value + 1, so user sees 1, 2, 3, ... but we store 0, 1, 2, ...
        int currentValue = CurrentStroke.getStroke() + 1;
        JSpinner strokeSpinner = new JSpinner(new SpinnerNumberModel(currentValue, 1, 50, 1));

        JPanel panel = new JPanel();
        panel.add(new JLabel("Stroke Width:"));
        panel.add(strokeSpinner);

        int result = JOptionPane.showConfirmDialog(null, panel, "Set Stroke Width", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int userValue = (Integer) strokeSpinner.getValue();
            CurrentStroke.setStroke(userValue - 1); // Subtract one as requested
        }
    }

}
