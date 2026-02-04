package model.site;

public abstract class OwnedSite extends Site {

    private Integer ownerId;

    public OwnedSite(int lengthInFeet, int breadthInFeet, int ownerId) {
        super(lengthInFeet, breadthInFeet);
        this.ownerId = ownerId;
    } 

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public abstract int getMaintenancePrice();
}
