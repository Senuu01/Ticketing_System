package users;

import ticket.TicketPool;
import logger.Logger;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final int customerId;

    public Customer(TicketPool ticketPool, int retrievalRate, int customerId) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (ticketPool) {
                while (ticketPool.getTicketCount() - retrievalRate < ticketPool.getMinThreshold()) {
                    try {
                        Logger.log(Thread.currentThread().getName() + ": Ticket pool is too low. Customer ID" + customerId + " is waiting for tickets.");
                        ticketPool.wait(); // Customer waits until there are enough tickets to keep the minimum threshold
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        Logger.log(Thread.currentThread().getName() + ": Customer ID" + customerId + " thread interrupted.");
                        return;
                    }
                }

                // Remove tickets once they are available
                ticketPool.removeTickets(retrievalRate);
                Logger.log(Thread.currentThread().getName() + ": Customer ID" + customerId + " purchased " + retrievalRate + " tickets. Remaining tickets: " + ticketPool.getTicketCount());
                ticketPool.notify(); // Notify a single thread that tickets have been removed
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.log(Thread.currentThread().getName() + ": Customer ID" + customerId + " thread interrupted.");
            }
        }
    }
}