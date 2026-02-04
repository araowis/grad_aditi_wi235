package service.factory;

import service.*;
import service.users.*;
import persistence.*;

public class AssignmentFactory {

    private final MaintenanceInterface maintenanceDao;
    private final SiteInterface siteDao;
    private final OwnerInterface ownerDao;
    private final AdminInterface adminInterface;

    public AssignmentFactory(MaintenanceInterface maintenanceDao, SiteInterface siteDao, OwnerInterface ownerDao, AdminInterface adminInterface) {
        this.maintenanceDao = maintenanceDao;
        this.siteDao = siteDao;
        this.ownerDao = ownerDao;
        this.adminInterface = adminInterface;
    }

    public Service createService(String mode) {
        return switch (mode) {
            case "-a" -> new AdminService(maintenanceDao, siteDao, adminInterface);
            case "-o" -> new OwnerService(ownerDao);
            case "-g" -> new UserService();
            default -> throw new IllegalArgumentException("Unknown option: " + mode);
        };
    }
}
