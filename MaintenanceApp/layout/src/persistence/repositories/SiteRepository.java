package persistence.repositories;

import java.util.List;

import model.site.OwnedSite;
import model.site.Site;
import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;

public interface SiteRepository {
    int addSite();
    void updateSite(Site site);
    void updateOwnedSite(OwnedSite site);
    void removeSite(int siteId);

    // void updateOwnershipStatus(int siteId, OccupancyStatus status);
    public List<Site> getAllSites();

    void assignOwnerToSite(int siteId, int ownerId, OccupancyStatus status, HouseType houseType);

    int getOccupancyCount(int siteId);
    boolean isMaintenancePaid(int siteId);
    Site getSiteById(int siteId);
    boolean ownerExists(int ownerId);
}
