package panels.editor.textdialog;

import javax.swing.*;
import java.awt.*;

public class TextToolDialog {

    public static TextProperties collectTextProperties() {
        // Create a panel to hold all input components
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns with spacing

        // Input field for text
        JLabel textLabel = new JLabel("Enter text:");
        JTextField textField = new JTextField();

        // Input field for font size
        JLabel fontSizeLabel = new JLabel("Font size:");
        JTextField fontSizeField = new JTextField("12"); // Default font size

        // Dropdown for font type
        JLabel fontTypeLabel = new JLabel("Font type:");
        String[] fonts = {"Comic Sans MS", "American Typewriter", "Arial"};
        JComboBox<String> fontTypeComboBox = new JComboBox<>(fonts);

        // Dropdown for font style
        JLabel fontStyleLabel = new JLabel("Font style:");
        String[] styles = {"Regular", "Bold", "Italic"};
        JComboBox<String> fontStyleComboBox = new JComboBox<>(styles);

        // Add components to the panel
        panel.add(textLabel);
        panel.add(textField);
        panel.add(fontSizeLabel);
        panel.add(fontSizeField);
        panel.add(fontTypeLabel);
        panel.add(fontTypeComboBox);
        panel.add(fontStyleLabel);
        panel.add(fontStyleComboBox);

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Text Properties",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve input values
            String text = textField.getText();
            if (text == null || text.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Text cannot be empty!");
                return null;
            }

            int fontSize;
            try {
                fontSize = Integer.parseInt(fontSizeField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid font size!");
                return null;
            }

            String fontType = (String) fontTypeComboBox.getSelectedItem();
            String fontStyleStr = (String) fontStyleComboBox.getSelectedItem();

            return new TextProperties(text, fontSize, fontType, fontStyleStr);
        }

        return null; // Return null if the user cancels
    }
}
