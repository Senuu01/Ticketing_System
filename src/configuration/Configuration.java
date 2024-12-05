
package configuration;

public class Configuration {
    private final int maxTickets;
    private final int releaseRate;
    private final int retrievalRate;

    public Configuration(int maxTickets, int releaseRate, int retrievalRate) {
        this.maxTickets = maxTickets;
        this.releaseRate = releaseRate;
        this.retrievalRate = retrievalRate;
    }

    public int getMaxTickets() {
        return maxTickets;
    }

    public int getReleaseRate() {
        return releaseRate;
    }

    public int getRetrievalRate() {
        return retrievalRate;
    }
}