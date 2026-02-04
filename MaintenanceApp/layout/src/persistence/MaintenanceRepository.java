package persistence;

import java.util.List;
import persistence.dto.*;

public interface MaintenanceRepository {
    void generateMonthlyMaintenance();
    void payMaintenance(int siteId, long amount);
    List<MaintenanceRecord> getPendingMaintenance();
    List<MaintenanceRecord> getPendingMaintenanceBySite(int siteId);
    void approveMaintenancePayment(int siteId);
}
