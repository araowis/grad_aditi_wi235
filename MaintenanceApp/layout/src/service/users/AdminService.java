package service.users;

import service.Service;
import factory.LoginFactory;
import service.auth.LoginHandler;
import utils.PasswordHashing;
import persistence.*;
import model.site.OwnedSite;
import model.site.Site;
import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;
import model.user.Owner;

import java.util.List;
import java.util.Scanner;

public class AdminService implements Service {

    private final MaintenanceRepository maintenanceRepo;
    private final SiteRepository siteRepo;
    private final AdminRepository adminRepo;
    private final OwnerRepository ownerRepo;

    public AdminService(MaintenanceRepository maintenanceRepo, SiteRepository siteRepo, AdminRepository adminRepo,
            OwnerRepository ownerRepo) {
        this.maintenanceRepo = maintenanceRepo;
        this.siteRepo = siteRepo;
        this.adminRepo = adminRepo;
        this.ownerRepo = ownerRepo;
    }

    @Override
    public void run() throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.print("Choose option: ");
            int option = Integer.parseInt(sc.nextLine());

            if (option == 2) {
                signup(sc);
                return;
            }

            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();

            LoginHandler handler = LoginFactory.create("-a");

            if (handler.login(username, password, adminRepo)) {
                System.out.println("Welcome Admin");
                adminMenu(sc);
            } else {
                System.out.println("Invalid admin credentials");
            }
        }
    }

    private void signup(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        String hash = PasswordHashing.hashPassword(password);
        adminRepo.createAdmin(username, hash);

        System.out.println("Admin account created");
    }

    private void adminMenu(Scanner sc) throws Exception {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addOwner(sc);
                case 2 -> updateOwner(sc);
                case 3 -> removeOwner(sc);
                case 4 -> addSite();
                case 5 -> updateSite(sc);
                case 6 -> removeSite(sc);
                case 7 -> collectMaintenance(sc);
                case 8 -> approveMaintenance(sc);
                case 9 -> viewPendingMaintenance();
                case 10 -> generateMonthlyMaintenance();
                case 11 -> assignSiteToOwner(sc);
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void printMenu() {
        System.out.println("================================");
        System.out.println("--- Admin Menu ----");
        System.out.println("1. Add Owner");
        System.out.println("2. Update Owner");
        System.out.println("3. Remove Owner");
        System.out.println("4. Add Site");
        System.out.println("5. Update Site");
        System.out.println("6. Remove Site");
        System.out.println("7. Collect Maintenance");
        System.out.println("8. Approve Maintenance Payment");
        System.out.println("9. View Pending Maintenance");
        System.out.println("10. Generate Monthly Maintenance");
        System.out.println("11. Assign Site To Owner");
        System.out.println("0. Logout");
        System.out.println("================================");
    }

    private void addOwner(Scanner sc) throws Exception {
        System.out.print("Owner username: ");
        String username = sc.nextLine();

        String defaultPassword = "changethisplease";
        String hash = PasswordHashing.hashPassword(defaultPassword);

        ownerRepo.createOwner(username, hash);
        System.out.println("Owner created (default password: changethisplease)");
    }

    private void updateOwner(Scanner sc) {
        System.out.print("Owner ID: ");
        int ownerId = Integer.parseInt(sc.nextLine());

        Owner owner = ownerRepo.getOwnerById(ownerId);
        if (owner == null) {
            System.out.println("Owner not found");
            return;
        }

        System.out.println("Current name: " + owner.getName());
        System.out.print("New name [press Enter to keep current]: ");
        String nameInput = sc.nextLine().trim();

        if (!nameInput.isEmpty()) {
            owner.setName(nameInput);
        }

        System.out.println("Maintenance paid: " + owner.isMaintenancePaid());
        System.out.print("Set maintenance paid? (true/false) [press Enter to keep]: ");
        String paidInput = sc.nextLine().trim();

        if (!paidInput.isEmpty()) {
            owner.setMaintenancePaid(Boolean.parseBoolean(paidInput));
        }

        ownerRepo.updateOwnerDetails(owner);
        System.out.println("Owner details updated successfully");
    }

    private void removeOwner(Scanner sc) {
        System.out.print("Owner ID: ");
        int ownerId = Integer.parseInt(sc.nextLine());

        ownerRepo.removeOwner(ownerId);
        System.out.println("Owner removed");
    }

    private void addSite() {
        int siteId = siteRepo.addSite();
        System.out.println("Site created with ID: " + siteId);
    }

    private void updateSite(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        Site site = siteRepo.getSiteById(siteId);
        if (site == null) {
            System.out.println("Site not found");
            return;
        }

        if (!(site instanceof OwnedSite)) {
            System.out.println("This is an open site.");
            System.out.println("Dimensions cannot be modified once created.");
            System.out.println("No updatable fields available.");
            return;
        }

        OwnedSite ownedSite = (OwnedSite) site;

        System.out.println("Current occupancy: " + ownedSite.getOccupancyStatus());
        System.out.print("New occupancy (OPEN/OCCUPIED): ");
        String occInput = sc.nextLine().trim();

        if (!occInput.isEmpty()) {
            ownedSite.setOccupancyStatus(OccupancyStatus.valueOf(occInput.toUpperCase()));
        }

        System.out.println("Current house type: " + ownedSite.getHouseType());
        System.out.print("New house type (VILLA/APARTMENT/INDEPENDENT_HOUSE): ");
        String houseInput = sc.nextLine().trim();

        if (!houseInput.isEmpty()) {
            ownedSite.setHouseType(HouseType.valueOf(houseInput.toUpperCase()));
        }

        siteRepo.updateOwnedSite(ownedSite);
        System.out.println("Owned site updated successfully");
    }

    private void assignSiteToOwner(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        Site site = siteRepo.getSiteById(siteId);
        if (site == null) {
            System.out.println("Site not found");
            return;
        }

        if (site instanceof OwnedSite) {
            System.out.println("Site is already owned");
            return;
        }

        System.out.print("Owner ID: ");
        int ownerId = Integer.parseInt(sc.nextLine());

        if (!siteRepo.ownerExists(ownerId)) {
            System.out.println("Owner does not exist");
            return;
        }

        System.out.println("Choose House Type (optional):");
        System.out.println("1. Villa");
        System.out.println("2. Apartment");
        System.out.println("3. Independent House");
        System.out.print("Press Enter to skip (open site): ");

        String input = sc.nextLine().trim();
        HouseType houseType = null;

        if (!input.isEmpty()) {
            switch (input) {
                case "1" -> houseType = HouseType.VILLA;
                case "2" -> houseType = HouseType.APARTMENT;
                case "3" -> houseType = HouseType.INDEPENDENT_HOUSE;
                default -> {
                    System.out.println("Invalid choice");
                    return;
                }
            }
        }

        siteRepo.assignOwnerToSite(
                siteId,
                ownerId,
                OccupancyStatus.OCCUPIED,
                houseType);

        System.out.println("Site successfully assigned to owner");
    }

    private void removeSite(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        siteRepo.removeSite(siteId);
        System.out.println("Site removed");
    }

    private void generateMonthlyMaintenance() {
        maintenanceRepo.generateMonthlyMaintenance();
        System.out.println("Monthly maintenance generated");
    }

    private void collectMaintenance(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        System.out.print("Amount paid: ");
        long amount = Long.parseLong(sc.nextLine());

        maintenanceRepo.payMaintenance(siteId, amount);
        System.out.println("Payment recorded (pending approval)");
    }

    private void approveMaintenance(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        maintenanceRepo.approveMaintenancePayment(siteId);
        System.out.println("Maintenance payment approved");
    }

    private void viewPendingMaintenance() {
        List<?> pending = maintenanceRepo.getPendingMaintenance();
        if (pending.isEmpty()) {
            System.out.println("No pending maintenance");
            return;
        }
        pending.forEach(System.out::println);
    }
}
