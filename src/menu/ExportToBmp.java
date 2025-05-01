package menu;

import database.DatabaseManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

public class ExportToBmp implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save BMP File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("BMP Files", "bmp"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".bmp")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".bmp");
            }

            try (Connection conn = DatabaseManager.getConnection()) {
                assert conn != null;
                try (CallableStatement stmt = conn.prepareCall("{ ? = call render_canvas_to_bmp() }")) {

                    stmt.registerOutParameter(1, Types.BINARY);
                    stmt.execute();

                    byte[] pngData = stmt.getBytes(1);

                    try (FileOutputStream fos = new FileOutputStream(fileToSave)) {
                        fos.write(pngData);
                    }

                    JOptionPane.showMessageDialog(null,
                            "File saved successfully!",
                            "Export Complete",
                            JOptionPane.INFORMATION_MESSAGE);

                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Error exporting BMP: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    public static JMenuItem createMenuItem() {
        JMenuItem pngItem = new JMenuItem("Export to BMP");
        pngItem.addActionListener(new ExportToBmp());
        pngItem.setMnemonic('B');
        return pngItem;
    }
}
