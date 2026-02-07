package context;

import java.sql.Connection;

import factory.ServiceFactory;
import persistence.operations.PostgresDatabaseConnector;
import persistence.postgres.AdminDAO;
import persistence.postgres.MaintenanceDAO;
import persistence.postgres.OwnerDAO;
import persistence.postgres.SiteDAO;

public class ApplicationContext {
    public static ServiceFactory createServiceFactory() throws Exception {
        Connection conn = PostgresDatabaseConnector.createConnection();
        return new ServiceFactory(
            new MaintenanceDAO(conn),
            new SiteDAO(conn),
            new OwnerDAO(conn),
            new AdminDAO(conn)
        );
    }
}