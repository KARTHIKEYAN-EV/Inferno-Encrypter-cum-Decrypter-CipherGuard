package io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles reading and writing files for the encryption tool
 */
public class FileHandler {

    /**
     * Reads the entire content of a file as a string
     * 
     * @param filePath path to the input file
     * @return file content as a String
     * @throws IOException if the file cannot be read
     */
    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Writes a string to a file
     * 
     * @param filePath path to the output file
     * @param content  the string content to write
     * @throws IOException if the file cannot be written
     */
    public static void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    /**
     * Appends text to an existing file
     * 
     * @param filePath path to the file
     * @param content  text to append
     * @throws IOException if the file cannot be written
     */
    public static void appendToFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes(), java.nio.file.StandardOpenOption.APPEND,
                java.nio.file.StandardOpenOption.CREATE);
    }
}
