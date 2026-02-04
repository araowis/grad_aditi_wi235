package persistence;

import java.util.*;
import model.site.*;
import model.user.*;
import persistence.operations.MaintenanceDAO.MaintenanceRecord;

public interface MaintenanceInterface {
    public void calculateMonthlyMaintenance();
    public void payMaintenance(int siteId, double amount);

    void approveSiteUpdate(int siteId);
    void rejectSiteUpdate(int siteId, String reason);

    List<Site> getAllPendingSites();
    Site getPendingSiteById(int siteId);
    public List<MaintenanceRecord> getPendingMaintenance();
}
