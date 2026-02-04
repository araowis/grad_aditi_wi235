package model.site.occupancy;

public enum OccupancyStatus {

    OPEN(6),
    OCCUPIED(9);

    private final int pricePerSquareFoot;

    OccupancyStatus(int pricePerSquareFoot) {
        this.pricePerSquareFoot = pricePerSquareFoot;
    }

    public int calculateMaintenance(int areaInSquareFeet) {
        return areaInSquareFeet * pricePerSquareFoot;
    }

    public int getPricePerSquareFoot() {
        return pricePerSquareFoot;
    }
}
