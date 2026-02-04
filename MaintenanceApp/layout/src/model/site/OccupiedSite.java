package model.site;

public class OccupiedSite extends OwnedSite {
    public static final int PRICE_PER_SQUARE_FOOT = 9;
    private int occupiedSiteNumber;      
    private OccupiedHouseType type;

    public OccupiedSite(int lengthInFeet, int breadthInFeet, int ownerId) {
        super(lengthInFeet, breadthInFeet, ownerId);
        this.ownershipStatus = true;
    }

    public void setOccupiedSiteType(String type) {
        this.type = OccupiedHouseType.toType(type);
    }

    public OccupiedHouseType getOccupiedHouseType() {
        return this.type;
    }

    public int getOccupiedSiteNumber() {
        return occupiedSiteNumber;
    }

    public void setOccupiedSiteNumber(int occupiedSiteNumber) {
        this.occupiedSiteNumber = occupiedSiteNumber;
    }

    @Override
    public int getMaintenancePrice() {
        return area() * PRICE_PER_SQUARE_FOOT;
    }

    public void setOccupied(boolean occupied) {
        this.ownershipStatus = occupied;
    }
}
