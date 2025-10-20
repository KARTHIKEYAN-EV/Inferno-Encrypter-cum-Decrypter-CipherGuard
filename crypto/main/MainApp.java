package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.InputMismatchException;

import algorithms.*;
import exceptions.InvalidKeyException;
import io.FileHandler;
import util.Logger;

public class MainApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Cipher cipher = null;
        boolean running = true;

        while (running) {
            System.out.println("\n=== Encryption Tool ===");
            System.out.println("1. Caesar Cipher");
            System.out.println("2. XOR Cipher");
            System.out.println("3. Substitution Cipher");
            System.out.println("4. Exit");
            System.out.print("Select a cipher: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            // Choose cipher
            switch (choice) {
                case 1:
                    cipher = new CaesarCipher();
                    break;
                case 2:
                    cipher = new XORCipher();
                    break;
                case 3:
                    System.out.println("Enter 26-letter mapping (A-Z) in order:");
                    String mappingStr = sc.nextLine().toUpperCase();
                    cipher = new SubstitutionCipher(mappingStr);
                    break;
                case 4:
                    running = false;
                    continue;
                default:
                    System.out.println("Invalid choice. Try again.");
                    continue;
            }

            // Input type
            System.out.println("\nInput source:");
            System.out.println("1. File");
            System.out.println("2. Text");
            System.out.print("Select input type: ");
            int inputType = sc.nextInt();
            sc.nextLine(); // consume newline

            String input = "";
            String outputFile = "";
            if (inputType == 1) {
                System.out.print("Enter input file path: ");
                String inputFile = sc.nextLine();
                System.out.print("Enter output file path: ");
                outputFile = sc.nextLine();
                try {
                    input = FileHandler.readFile(inputFile);
                } catch (IOException e) {
                    System.out.println("File read error: " + e.getMessage());
                    continue;
                }
            } else if (inputType == 2) {
                System.out.print("Enter text: ");
                input = sc.nextLine();
            } else {
                System.out.println("Invalid input type.");
                continue;
            }

            // Key input (only for Caesar and XOR)
            int key = 0;
            if (!(cipher instanceof SubstitutionCipher)) {
                
                int k = 0;
                do{
                    try{
                        System.out.print("Enter key (integer): ");
                        key = sc.nextInt();
                        if (key<0) throw new InvalidKeyException ("Key must be positive for Caesar Cipher");
                        k=1;
                    }
                    catch(InputMismatchException e){
                        sc.nextLine();
                        System.out.println("Error: Enter an integer value");
                    }
                    catch(Exception e){
                        sc.nextLine();
                        System.out.println("Error: "+e.getMessage());
                    }
                }while(k==0);
                sc.nextLine(); // consume newline
            }

            // Action: Encrypt / Decrypt
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.print("Select action: ");
            int action = sc.nextInt();
            sc.nextLine(); // consume newline

            try {
                String result = "";
                if (action == 1) {
                    result = cipher.encrypt(input, key);
                } else if (action == 2) {
                    result = cipher.decrypt(input, key);
                } else {
                    System.out.println("Invalid action.");
                    continue;
                }

                // Show result
                System.out.println("\n=== Result ===\n" + result);

                // Save to file if file input
                if (inputType == 1) {
                    try {
                        FileHandler.writeFile(outputFile, result);
                        Logger.log(cipher.getName() + " " + (action == 1 ? "encrypted" : "decrypted") + " file to "
                                + outputFile);
                        System.out.println("File processed successfully!");
                    } catch (IOException e) {
                        System.out.println("File write error: " + e.getMessage());
                    }
                }

            } catch (InvalidKeyException e) {
                System.out.println("Key error: " + e.getMessage());
            }
        }

        System.out.println("Exiting program. Goodbye!");
        sc.close();
    }
}
