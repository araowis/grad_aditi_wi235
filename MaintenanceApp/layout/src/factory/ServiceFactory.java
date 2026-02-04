package factory;

import service.*;
import service.users.*;
import persistence.*;
import context.*;

public class ServiceFactory {

    private final MaintenanceRepository maintenanceDao;
    private final SiteRepository siteDao;
    private final OwnerRepository ownerDao;
    private final AdminRepository adminDao;

    public ServiceFactory(MaintenanceRepository maintenanceDao, SiteRepository siteDao, OwnerRepository ownerDao, AdminRepository adminDao) {
        this.maintenanceDao = maintenanceDao;
        this.siteDao = siteDao;
        this.ownerDao = ownerDao;
        this.adminDao = adminDao;
    }

    public Service createService(Mode mode) {
        return switch (mode) {
            case ADMIN -> createAdminService();
            case OWNER -> createOwnerService();
            case GENERAL -> createUserService();
        };
    }

    private Service createAdminService() {
        return new AdminService(maintenanceDao, siteDao, adminDao, ownerDao);
    }

    private Service createOwnerService() {
        return new OwnerService(ownerDao, maintenanceDao);
    }

    private Service createUserService() {
        return new UserService();
    }
}
