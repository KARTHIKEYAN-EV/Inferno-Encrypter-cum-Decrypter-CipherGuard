package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class to track encryption/decryption activity
 */
public class Logger {

    private static final String LOG_FILE = "activity.log"; // log file name

    /**
     * Logs a message with timestamp to the log file
     * 
     * @param message The message to log
     */
    public static void log(String message) {
        // Get current timestamp
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = "[" + timeStamp + "] " + message + "\n";

        try {
            // Append log entry to the log file
            Files.write(Paths.get(LOG_FILE), logEntry.getBytes(),
                    java.nio.file.StandardOpenOption.APPEND,
                    java.nio.file.StandardOpenOption.CREATE);
        } catch (IOException e) {
            // If logging fails, print error to console
            System.out.println("Logging failed: " + e.getMessage());
        }
    }
}
