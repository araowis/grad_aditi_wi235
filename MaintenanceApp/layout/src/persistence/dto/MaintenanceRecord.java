package persistence.dto;

public record MaintenanceRecord(int siteId, long amount, String month) {
    @Override
    public String toString() {
        return String.format(
            "Site ID: %d | Amount Due: INR %d | Month: %s",
            siteId, amount, month
        );
    }
}
