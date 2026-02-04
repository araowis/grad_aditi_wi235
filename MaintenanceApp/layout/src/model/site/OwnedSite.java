package model.site;

import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;

public class OwnedSite extends Site {

    private int ownerId;
    private OccupancyStatus occupancyStatus;
    private HouseType houseType;
    private boolean maintenancePaid;

    public OwnedSite(int lengthInFeet, int breadthInFeet, int ownerId, OccupancyStatus status) {
        super(lengthInFeet, breadthInFeet);
        this.ownerId = ownerId;
        this.occupancyStatus = status;
        this.maintenancePaid = false;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public OccupancyStatus getOccupancyStatus() {
        return occupancyStatus;
    }

    public void setOccupancyStatus(OccupancyStatus status) {
        this.occupancyStatus = status;
    }

    public HouseType getHouseType() {
        return houseType;
    }

    public void setHouseType(HouseType houseType) {
        this.houseType = houseType;
    }

    public boolean isMaintenancePaid() {
        return maintenancePaid;
    }

    public void setMaintenancePaid(boolean paid) {
        this.maintenancePaid = paid;
    }

    public int calculateMaintenanceAmount() {
        return occupancyStatus.calculateMaintenance(area());
    }
}
