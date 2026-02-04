package model.site;

public class SiteFactory {

    private static final int FIRST_SITES_MAX  = 10; 
    private static final int SECOND_SITES_MAX = 10; 
    private static final int THIRD_SITES_MAX  = 15; 

    private int createdCount = 0;

    private static final SiteFactory INSTANCE = new SiteFactory();

    private SiteFactory() {}

    public static SiteFactory getInstance() {
        return INSTANCE;
    }

    public synchronized Site createSite() {
        if (createdCount < FIRST_SITES_MAX) {
            createdCount++;
            return new Site(40, 60);
        } 
        else if (createdCount < FIRST_SITES_MAX + SECOND_SITES_MAX) {
            createdCount++;
            return new Site(30, 50);
        } 
        else if (createdCount < FIRST_SITES_MAX + SECOND_SITES_MAX + THIRD_SITES_MAX) {
            createdCount++;
            return new Site(30, 40);
        } 
        else {
            throw new IllegalStateException("Cannot create more than " + (FIRST_SITES_MAX + SECOND_SITES_MAX + THIRD_SITES_MAX) + " sites in this application.");
        }
    }

    public Site createTemporarySite(int length, int breadth) {
        return new Site(length, breadth);
    }

    public OpenSite createTemporaryOwnedSite(int length, int breadth, int ownerId) {
        return new OpenSite(length, breadth, ownerId);
    }

    public synchronized int getCreatedCount() {
        return createdCount;
    }
}