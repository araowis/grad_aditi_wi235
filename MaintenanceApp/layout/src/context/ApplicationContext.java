package context;

import java.sql.Connection;

import factory.ServiceFactory;
import persistence.operations.AdminDAO;
import persistence.operations.DatabaseConnector;
import persistence.operations.MaintenanceDAO;
import persistence.operations.OwnerDAO;
import persistence.operations.SiteDAO;

public class ApplicationContext {
    public static ServiceFactory createServiceFactory() throws Exception {
        Connection conn = DatabaseConnector.createConnection();
        return new ServiceFactory(
            new MaintenanceDAO(conn),
            new SiteDAO(conn),
            new OwnerDAO(conn),
            new AdminDAO(conn)
        );
    }
}