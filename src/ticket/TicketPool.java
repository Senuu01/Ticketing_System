package ticket;

import java.util.LinkedList;
import java.util.Queue;
import logger.Logger; // Import the Logger class

public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int maxCapacity;
    private final int minThreshold;  // Minimum number of tickets that must always be available

    public TicketPool(int maxCapacity, int minThreshold) {
        this.maxCapacity = maxCapacity;
        this.minThreshold = minThreshold;

        // Initialize the pool with tickets equal to the minimum threshold
        for (int i = 0; i < minThreshold; i++) {
            tickets.add(1);
        }
    }

    public synchronized void addTickets(int count) {
        while (tickets.size() + count > maxCapacity) {
            try {
                Logger.log(Thread.currentThread().getName() + ": Waiting to add tickets. Pool is full.");
                wait(); // Vendor waits until there is enough space
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            tickets.add(1); // Add tickets to the pool
        }
        Logger.log(Thread.currentThread().getName() + ": Added " + count + " tickets. Current count: " + tickets.size());
        notify(); // Notify a single waiting thread that tickets have been added
    }

    public synchronized void removeTickets(int count) {
        while (tickets.size() - count < minThreshold) {
            try {
                Logger.log(Thread.currentThread().getName() + ": Waiting to remove tickets. Pool is below minimum threshold.");
                wait(); // Customer waits until enough tickets are available, ensuring minimum threshold
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        for (int i = 0; i < count; i++) {
            tickets.poll(); // Remove tickets from the pool
        }
        Logger.log(Thread.currentThread().getName() + ": Removed " + count + " tickets. Current count: " + tickets.size());
        notify(); // Notify a single waiting thread that tickets have been removed
    }

    public synchronized int getTicketCount() {
        return tickets.size();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getMinThreshold() {
        return minThreshold;
    }
}
