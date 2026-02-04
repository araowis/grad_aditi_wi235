package persistence;

import model.site.*;
import model.user.*;

public interface OwnerRepository extends UserRepository {
    void createOwner(String username, String passwordHash);
    void removeOwner(int ownerId);
    
    Owner getOwnerById(int ownerId);
    Owner getOwnerByUsername(String username);
    void updateOwnerDetails(Owner owner);
    
    void updatePassword(String username, String newHash);

    OwnedSite getOwnedSiteByOwnerId(int ownerId);
    void requestSiteUpdate(Site site);
    
}
