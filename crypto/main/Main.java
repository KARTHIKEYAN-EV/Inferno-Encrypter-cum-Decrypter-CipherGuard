package main;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        // If command line arguments provided, use CLI
        if (args.length > 0) {
            if (args[0].toLowerCase().equals("cli")) {
                MainApp.main(args);
                return;
            }
            else if (args[0].toLowerCase().equals("gui")) {
                MainAppUI.main(args);
                return;
            }
        }

        // Otherwise, ask user which version to use
        String[] options = {"GUI Version", "CLI Version"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Choose application mode:",
            "Encryption Tool Launcher",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 0) {
            // GUI Version
            MainAppUI.main(args);
        } else if (choice == 1) {
            // CLI Version
            MainApp.main(args);
        }
    }
}