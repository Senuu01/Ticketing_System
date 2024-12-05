package configuration;

import java.io.*;

public class ConfigurationManager {
    private static final String CONFIG_FILE = "config.txt";

    public static Configuration loadConfiguration() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))) {
            int maxTickets = Integer.parseInt(reader.readLine());
            int releaseRate = Integer.parseInt(reader.readLine());
            int retrievalRate = Integer.parseInt(reader.readLine());
            return new Configuration(maxTickets, releaseRate, retrievalRate);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading configuration. Using default values.");
            return new Configuration(100, 10, 5); // Default values
        }
    }

    public static void saveConfiguration(Configuration config) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(config.getMaxTickets() + "\n");
            writer.write(config.getReleaseRate() + "\n");
            writer.write(config.getRetrievalRate() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving configuration.");
        }
    }
}
