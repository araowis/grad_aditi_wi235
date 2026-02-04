package persistence;

import model.user.Owner;

public interface AdminInterface extends UserInterface {
    void addOwner(Owner owner);
    void updateOwner(Owner owner);
    void removeOwner(int ownerId);
    void approvePayment(int siteId);
    public void saveOwnerUserToDB(String username, String hash);    
    void saveUserToDB(String username, String hash);
}
