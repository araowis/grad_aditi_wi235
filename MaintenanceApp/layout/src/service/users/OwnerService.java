package service.users;

import service.Service;
import factory.LoginFactory;
import service.auth.LoginHandler;
import utils.PasswordHashing;

import java.util.Scanner;

import model.site.OwnedSite;
import model.site.occupancy.OccupancyStatus;
import model.site.type.HouseType;
import model.user.Owner;

import persistence.OwnerRepository;
import persistence.MaintenanceRepository;

public class OwnerService implements Service {

    private final OwnerRepository ownerRepo;
    private final MaintenanceRepository maintenanceRepo;

    private String loggedInUsername;
    private int loggedInOwnerId;

    public OwnerService(OwnerRepository ownerRepo, MaintenanceRepository maintenanceRepo) {
        this.ownerRepo = ownerRepo;
        this.maintenanceRepo = maintenanceRepo;
    }

    @Override
    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            if (login(sc)) {
                startOwnerOperations(sc);
            }
        } catch (Exception e) {
            System.out.println("Error during owner session");
            e.printStackTrace();
        }
    }


    private boolean login(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        LoginHandler handler = LoginFactory.create("-o");

        if (!handler.login(username, password, ownerRepo)) {
            System.out.println("Owner login failed");
            return false;
        }

        this.loggedInUsername = username;
        Owner owner = ownerRepo.getOwnerByUsername(username);
        this.loggedInOwnerId = owner.getOwnerId();

        System.out.println("Welcome, " + username);
        return true;
    }

    private void startOwnerOperations(Scanner sc) throws Exception {

        if (mustChangePassword()) {
            System.out.println("You must change your default password.");
            changePassword(sc);
        }

        while (true) {
            printMenu();
            int choice = readInt(sc);

            switch (choice) {
                case 1 -> viewMySite();
                case 2 -> requestSiteUpdate(sc);
                case 3 -> payMaintenance(sc);
                case 4 -> changePassword(sc);
                case 0 -> {
                    System.out.println("Owner logged out");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void printMenu() {
        System.out.println("================================");
        System.out.println("--- Owner Menu ----");
        System.out.println("1. View My Site");
        System.out.println("2. Raise update Site Request");
        System.out.println("3. Pay Maintenance");
        System.out.println("4. Change Password");
        System.out.println("0. Logout");
        System.out.println("================================");
        System.out.print("Choice: ");
    }

    private int readInt(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (Exception e) {
            return -1;
        }
    }

    private boolean mustChangePassword() throws Exception {
        String storedHash = ownerRepo.fetchHashFromDB(loggedInUsername);
        return PasswordHashing.verifyPassword("changeme", storedHash);
    }

    private void changePassword(Scanner sc) {
        System.out.print("New password: ");
        String pass1 = sc.nextLine();
        System.out.print("Confirm password: ");
        String pass2 = sc.nextLine();

        if (!pass1.equals(pass2)) {
            System.out.println("Passwords do not match");
            return;
        }

        try {
            String hash = PasswordHashing.hashPassword(pass1);
            ownerRepo.updatePassword(loggedInUsername, hash);
            System.out.println("Password updated successfully");
        } catch (Exception e) {
            System.out.println("Password update failed");
            e.printStackTrace();
        }
    }

    private void viewMySite() {
        OwnedSite site = ownerRepo.getOwnedSiteByOwnerId(loggedInOwnerId);

        if (site == null) {
            System.out.println("No site assigned to you");
            return;
        }

        System.out.println("----- My Site -----");
        System.out.println("Site ID: " + site.getId());
        System.out.println("Area: " + site.area() + " sq ft");
        System.out.println("Occupancy: " + site.getOccupancyStatus());
        System.out.println("House Type: " + site.getHouseType());
        System.out.println("Maintenance Paid: " + site.isMaintenancePaid());
    }

    private void requestSiteUpdate(Scanner sc) {
        OwnedSite site = ownerRepo.getOwnedSiteByOwnerId(loggedInOwnerId);

        if (site == null) {
            System.out.println("No site assigned to you");
            return;
        }

        System.out.println("Current occupancy: " + site.getOccupancyStatus());
        System.out.print("New occupancy (OPEN/OCCUPIED): ");
        String occ = sc.nextLine().trim();

        if (!occ.isEmpty()) {
            site.setOccupancyStatus(OccupancyStatus.valueOf(occ.toUpperCase()));
        }

        System.out.println("Current house type: " + site.getHouseType());
        System.out.print("New house type (VILLA/APARTMENT/INDEPENDENT_HOUSE): ");
        String house = sc.nextLine().trim();

        if (!house.isEmpty()) {
            site.setHouseType(
                    HouseType.valueOf(house.toUpperCase())
            );
        }

        ownerRepo.requestSiteUpdate(site);
        System.out.println("Update request sent to admin for approval");
    }

    private void payMaintenance(Scanner sc) {
        OwnedSite site = ownerRepo.getOwnedSiteByOwnerId(loggedInOwnerId);

        if (site == null) {
            System.out.println("No site assigned to you");
            return;
        }

        int amount = site.calculateMaintenanceAmount();
        System.out.println("Maintenance due: INR " + amount);

        System.out.print("Confirm payment (y/n): ");
        if (!sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println("Payment cancelled");
            return;
        }

        maintenanceRepo.payMaintenance(site.getId(), amount);
        System.out.println("Payment submitted. Awaiting admin approval");
    }
}
