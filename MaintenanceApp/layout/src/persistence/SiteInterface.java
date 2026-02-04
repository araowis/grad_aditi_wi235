package persistence;

import model.site.OwnedSite;
import model.site.Site;

public interface SiteInterface {
    int addSite();
    void updateSite(Site site);
    void updateOwnedSite(OwnedSite site);
    void removeSite(int siteId);
    void updateOwnershipStatus(int siteId, boolean isOccupied);
    int getOccupancyCount(int siteId);
    boolean isMaintenancePaid(int siteId);
    Site getSiteById(int siteId);
    OwnedSite getOwnedSiteById(int siteId);
    boolean ownerExists(int ownerId);
}
