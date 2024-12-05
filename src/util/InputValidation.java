package util;

import java.util.Scanner;

public class InputValidation {

    // Method to validate a positive integer input
    public static int getPositiveInt(Scanner scanner, String prompt) {
        int value;
        while (true) {
            System.out.println(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
                if (value > 0) {
                    return value; // Return the valid input
                } else {
                    System.out.println("Error: Please enter a positive integer.");
                }
            } else {
                System.out.println("Error: Invalid input. Please enter a positive integer.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    // Method to validate a command from a list of valid commands
    public static String getValidCommand(Scanner scanner, String prompt, String[] validCommands) {
        while (true) {
            System.out.println(prompt);
            String input = scanner.next().toLowerCase().trim();
            for (String command : validCommands) {
                if (input.equals(command)) {
                    return input; // Return valid command
                }
            }
            System.out.println("Error: Invalid command. Valid commands are: " + String.join(", ", validCommands));
        }
    }
}
