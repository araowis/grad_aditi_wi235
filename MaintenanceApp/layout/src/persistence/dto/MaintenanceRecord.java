package persistence.dto;

public class MaintenanceRecord {
    public final int siteId;
    public final long amount;
    public final String month;

    public MaintenanceRecord(int siteId, long amount, String month) {
        this.siteId = siteId;
        this.amount = amount;
        this.month = month;
    }

    @Override
    public String toString() {
        return String.format("Site ID: %d | Amount Due: INR %d | Month: %s", siteId, amount, month);
    }
}
