package persistence;

import java.sql.Date;
import java.util.List;
import persistence.dto.*;

public interface MaintenanceRepository {
    void generateMonthlyMaintenance();
    void payMaintenance(int siteId, long amount, Date paymentDate);
    List<MaintenanceRecord> getPendingMaintenance();
    List<MaintenanceRecord> getPendingMaintenanceBySite(int siteId);
    List<MaintenanceRecord> getMaintenancePaymentRequestList();
    void approveMaintenancePayment(int siteId);
}
