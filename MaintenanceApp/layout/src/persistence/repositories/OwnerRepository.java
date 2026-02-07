package persistence.repositories;

import model.site.*;
import model.user.*;
import java.util.List;

public interface OwnerRepository extends UserRepository {
    void createOwner(String username, String passwordHash);
    void removeOwner(int ownerId);
    
    Owner getOwnerById(int ownerId);
    Owner getOwnerByUsername(String username);
    void updateOwnerDetails(Owner owner);
    
    void updatePassword(String username, String newHash);

    List<OwnedSite> getOwnedSiteByOwnerId(int ownerId);
    void requestSiteUpdate(Site site);
    List<Owner> getAllOwners();
    
}
