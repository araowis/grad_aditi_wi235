package model.site;

public class OpenSite extends OwnedSite {
    public static final int PRICE_PER_SQUARE_FOOT = 6;

    public OpenSite(int lengthInFeet, int breadthInFeet, int ownerId) {
        super(lengthInFeet, breadthInFeet, ownerId);
    }

    @Override
    public int getMaintenancePrice() {
        return area() * PRICE_PER_SQUARE_FOOT;
    }

}
