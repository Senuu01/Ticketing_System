package users;

import ticket.TicketPool;
import logger.Logger;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseRate;

    public Vendor(TicketPool ticketPool, int releaseRate) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (ticketPool) {
                if (ticketPool.getTicketCount() >= ticketPool.getMaxCapacity()) {
                    Logger.log(Thread.currentThread().getName() + ": Ticket pool reached maximum capacity. Vendor thread is ending.");
                    return;  // End the vendor thread when maximum capacity is reached
                }

                // Add tickets to the pool
                ticketPool.addTickets(releaseRate);
                Logger.log(Thread.currentThread().getName() + ": Vendor added " + releaseRate + " tickets. Total tickets: " + ticketPool.getTicketCount());

                ticketPool.notify(); // Notify a single waiting customer that tickets have been added
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.log(Thread.currentThread().getName() + ": Vendor thread interrupted.");
            }
        }
    }
}