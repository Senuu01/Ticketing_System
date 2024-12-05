import configuration.*;
import logger.Logger;
import ticket.TicketPool;
import users.Customer;
import users.Vendor;
import util.InputValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input for Total Number of Tickets
        int totalTickets = InputValidation.getPositiveInt(scanner, "Enter the total number of tickets: ");
        System.out.println("Entered total number of " + totalTickets + " tickets successfully.\n");

        // Input for Ticket Release Rate
        int ticketReleaseRate = InputValidation.getPositiveInt(scanner, "Enter ticket release rate: ");
        System.out.println("Ticket Release rate is " + ticketReleaseRate + "%\n");

        // Input for Customer Retrieval Rate
        int customerRetrievalRate = InputValidation.getPositiveInt(scanner, "Enter customer retrieve rate: ");
        System.out.println("Customer retrieval rate is " + customerRetrievalRate + "%\n");

        // Input for Maximum Ticket Capacity
        int maxTicketCapacity = InputValidation.getPositiveInt(scanner, "Enter maximum ticket capacity: ");
        System.out.println("Maximum ticket capacity is " + maxTicketCapacity + "\n");

        // Input for Minimum Ticket Threshold
        int minTicketThreshold = InputValidation.getPositiveInt(scanner, "Enter minimum ticket threshold: ");
        System.out.println("Minimum ticket threshold is " + minTicketThreshold + "\n");

        // Create TicketPool with the user-defined maximum capacity and minimum threshold
        TicketPool ticketPool = new TicketPool(maxTicketCapacity, minTicketThreshold);

        // Lists to manage vendor and customer threads
        List<Thread> vendors = new ArrayList<>();
        List<Thread> customers = new ArrayList<>();

        while (true) {
            // User command input to control system operations
            String command = InputValidation.getValidCommand(scanner, "Enter command (start/stop/exit):", new String[]{"start", "stop", "exit"});

            switch (command) {
                case "start" -> {
                    // Start vendor threads
                    for (int i = 0; i < 2; i++) {
                        Thread vendor = new Thread(new Vendor(ticketPool, ticketReleaseRate), "Vendor-" + (i + 1));
                        vendors.add(vendor);
                        vendor.start();
                    }

                    // Start customer threads
                    for (int i = 0; i < 3; i++) {
                        Thread customer = new Thread(new Customer(ticketPool, customerRetrievalRate, i + 1), "Customer-" + (i + 1));
                        customers.add(customer);
                        customer.start();
                    }
                    Logger.log("System started with maximum ticket capacity: " + maxTicketCapacity + ", minimum threshold: " + minTicketThreshold);
                }
                case "stop" -> {
                    // Stop all threads gracefully
                    vendors.forEach(Thread::interrupt);
                    customers.forEach(Thread::interrupt);
                    vendors.clear();
                    customers.clear();
                    Logger.log("System stopped.");
                }
                case "exit" -> {
                    // Exit the system
                    Logger.log("Exiting system...");
                    vendors.forEach(Thread::interrupt);
                    customers.forEach(Thread::interrupt);
                    try {
                        // Allow threads to complete any cleanup
                        for (Thread vendor : vendors) {
                            vendor.join();
                        }
                        for (Thread customer : customers) {
                            customer.join();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Logger.log("Main thread interrupted during shutdown.");
                    }
                    System.exit(0);
                }
            }
        }
    }
}
