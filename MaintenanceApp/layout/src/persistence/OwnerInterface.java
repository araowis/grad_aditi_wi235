package persistence;

import model.site.*;
import model.user.*;

public interface OwnerInterface extends UserInterface {

    Owner viewOwnerDetails(int ownerId);

    void updateOwnerDetails(Owner owner);

    OwnedSite viewOwnSite(int ownerId);

    void requestSiteUpdate(Site site);

    void payMaintenance(OwnedSite site, double amount);
    void updatePassword(String username, String newHash);
    Owner viewOwnerDetailsByUsername(String username);
}
