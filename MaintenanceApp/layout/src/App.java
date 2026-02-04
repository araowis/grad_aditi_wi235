import service.*;
import service.factory.*;

import java.sql.Connection;

import persistence.*;
import persistence.operations.*;

public class App {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a mode:");
            System.out.println("-a (admin), -o (owner), -g (general)");
            return;
        }

        try {
            Connection conn = DatabaseConnector.createConnection();
            
            MaintenanceInterface maintenanceDao = new MaintenanceDAO(conn);
            SiteInterface siteDao = new SiteDAO(conn);
            OwnerInterface ownerDao = new OwnerDAO(conn);
            AdminInterface adminDao = new AdminDAO(conn);

            AssignmentFactory factory = new AssignmentFactory(maintenanceDao, siteDao, ownerDao, adminDao);

            Service service = factory.createService(args[0]);
            service.run();

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
