package database;
import javax.swing.*;

public class IPAddressPrompt {

    /**
     * Prompts the user for an IP address with "localhost" as the default value.
     * This is a blocking function that returns the entered IP address as a string.
     *
     * @return The entered IP address or "localhost" if no input is provided.
     */
    public static String promptForIPAddress() {
        // Default value for the input field
        String defaultIP = "localhost";

        // Show input dialog with default placeholder
        String input = (String) JOptionPane.showInputDialog(
                null,
                "Please enter an IP address or domain name:",
                "IP Address Input",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                defaultIP // Default placeholder text
        );

        // Return the entered value or "localhost" if input is null or empty
        if (input == null || input.trim().isEmpty()) {
            return defaultIP;
        }
        return input.trim();
    }

    public static void main(String[] args) {
        // Call the blocking function and get the result
        String ipAddress = promptForIPAddress();

        // Print the result to console (or use it in your application)
        System.out.println("User entered IP address: " + ipAddress);
    }
}
