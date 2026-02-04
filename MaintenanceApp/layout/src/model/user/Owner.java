package model.user;

public class Owner extends User {
    protected int ownerId;            
    protected boolean isMaintenancePaid;

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isMaintenancePaid() {
        return isMaintenancePaid;
    }

    public void setMaintenancePaid(boolean maintenancePaid) {
        this.isMaintenancePaid = maintenancePaid;
    }
}
