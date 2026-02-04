package service.users;

import service.Service;
import service.factory.LoginFactory;
import service.handlers.LoginHandler;
import utils.PasswordHashing;
import persistence.AdminInterface;
import persistence.MaintenanceInterface;
import persistence.OwnerInterface;
import persistence.SiteInterface;
import model.site.OwnedSite;
import model.site.Site;
import model.user.Owner;

import java.util.List;
import java.util.Scanner;

public class AdminService implements Service {

    private final MaintenanceInterface maintenance;
    private final SiteInterface site;
    private final AdminInterface admin;

    public AdminService(MaintenanceInterface maintenance, SiteInterface site, AdminInterface admin) {
        this.maintenance = maintenance;
        this.site = site;
        this.admin = admin;
    }

    @Override
    public void run() {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("1. Login");
            System.out.println("2. Signup");
            System.out.print("Choose option: ");
            int option = Integer.parseInt(sc.nextLine());

            if (option == 2) {
                adminSignup(sc);
                return;
            }

            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            LoginHandler loginHandler = LoginFactory.create("-a");

            if (loginHandler.login(username, password, admin)) {
                System.out.println("Welcome, Admin " + username);
                startAdminOperations(sc);
            } else {
                System.out.println("Admin login failed");
            }
        } catch (Exception e) {
            System.out.println("Error during admin login/signup");
            e.printStackTrace();
        }
    }

    private void adminSignup(Scanner sc) {
        try {
            System.out.println("--- Admin Signup ---");
            System.out.print("Enter username: ");
            String username = sc.nextLine();

            System.out.print("Enter password: ");
            String password = sc.nextLine();

            String hash = PasswordHashing.hashPassword(password);

            admin.saveUserToDB(username, hash);

            System.out.println("Admin account created successfully!");
        } catch (Exception e) {
            System.out.println("Error during admin signup");
            e.printStackTrace();
        }
    }

    private void startAdminOperations(Scanner sc) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> addOwner(sc);
                case 2 -> updateOwner(sc);
                case 3 -> removeOwner(sc);
                case 4 -> addSite(sc);
                case 5 -> updateSite(sc);
                case 6 -> updateOwnedSite(sc);
                case 7 -> removeSite(sc);
                case 8 -> collectMaintenance(sc); // Owner pays maintenance manually
                case 9 -> viewPendingSites();
                case 10 -> approveOrRejectSite(sc);
                case 11 -> calculateMonthlyMaintenance(); // New: Auto-calc maintenance for all sites
                case 12 -> approveOwnerPayment(sc); // New: Approve owner maintenance payment
                case 13 -> viewAllPendingMaintenance(); // New: See all pending payments
                case 0 -> running = false;
                default -> System.out.println("Invalid choice");
            }
        }
        System.out.println("Admin logged out");
    }

    private void printMenu() {
        System.out.println("================================");
        System.out.println("--- Admin Menu ----");
        System.out.println("1. Add Owner");
        System.out.println("2. Update Owner");
        System.out.println("3. Remove Owner");
        System.out.println("4. Add Site");
        System.out.println("5. Update Site (Open)");
        System.out.println("6. Update Site (Owned)");
        System.out.println("7. Remove Site");
        System.out.println("8. Collect Maintenance");
        System.out.println("9. View Pending Sites");
        System.out.println("10. Approve/Reject Site Update");
        System.out.println("11. Calculate Monthly Maintenance");
        System.out.println("12. Approve Owner Payment");
        System.out.println("13. View All Pending Maintenance");
        System.out.println("0. Logout");
        System.out.println("================================");
    }

    private void addOwner(Scanner sc) {
        System.out.print("Owner username: ");
        String username = sc.nextLine().trim();
        Owner owner = new Owner();
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty");
            return;
        }
        try {
            String defaultPassword = "changeme"; // default password
            String hash = PasswordHashing.hashPassword(defaultPassword);
            admin.saveOwnerUserToDB(username, hash);
            System.out.println("Owner created successfully. Default password is 'changeme'. Owner must change password on first login.");
        } catch (Exception e) {
            System.out.println("Error creating owner");
            e.printStackTrace();
        }

        admin.addOwner(owner);
        System.out.println("Owner added");
    }

    private void updateOwner(Scanner sc) {
        Owner owner = new Owner();
        System.out.print("Owner ID: ");
        owner.setOwnerId(Integer.parseInt(sc.nextLine()));

        System.out.print("New name: ");
        owner.setName(sc.nextLine());

        admin.updateOwner(owner);
        System.out.println("Owner updated");
    }

    private void removeOwner(Scanner sc) {
        System.out.print("Owner ID: ");
        int id = Integer.parseInt(sc.nextLine());

        admin.removeOwner(id);
        System.out.println("Owner removed");
    }

    private void addSite(Scanner sc) {
        int siteId = site.addSite();
        if (siteId != -1) {
            System.out.println("Site added with ID " + siteId);
        } else {
            System.out.println("Failed to add site");
        }
    }

    private void updateSite(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        Site existing = site.getSiteById(siteId);
        if (existing == null) {
            System.out.println("Site not found");
            return;
        }

        String currentStatus = existing.ownershipStatus ? "y" : "n";
        System.out.print("New occupancy status (y/n) [" + currentStatus + "]: ");
        String input = sc.nextLine().trim().toLowerCase();
        if (!input.isEmpty()) {
            if (input.equals("y")) {
                existing.ownershipStatus = true;
            } else {
                existing.ownershipStatus = true;
            }
        }

        System.out.print("New length (feet) [" + existing.getLengthInFeet() + "]: ");
        String lengthInput = sc.nextLine();
        if (!lengthInput.isBlank()) {
            existing.setLength(Integer.parseInt(lengthInput));
        }

        System.out.print("New breadth (feet) [" + existing.getBreadthInFeet() + "]: ");
        String breadthInput = sc.nextLine();
        if (!breadthInput.isBlank()) {
            existing.setBreadth(Integer.parseInt(breadthInput));
        }

        site.updateSite(existing);
        System.out.println("Site updated");
    }

    private void updateOwnedSite(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        Site existingSite = site.getOwnedSiteById(siteId);
        if (existingSite == null) {
            System.out.println("Site not found");
            return;
        }

        OwnedSite existing;
        if (existingSite instanceof OwnedSite) {
            existing = (OwnedSite) existingSite;
        } else {
            System.out.println("This site is not owned. Consider updating as an OpenSite instead.");
            return;
        }

        System.out.print("New length (feet) [" + existing.getLengthInFeet() + "]: ");
        String lengthInput = sc.nextLine();
        if (!lengthInput.isBlank()) {
            existing.setLength(Integer.parseInt(lengthInput));
        }

        System.out.print("New breadth (feet) [" + existing.getBreadthInFeet() + "]: ");
        String breadthInput = sc.nextLine();
        if (!breadthInput.isBlank()) {
            existing.setBreadth(Integer.parseInt(breadthInput));
        }

        System.out.print("New Owner ID [" + existing.getOwnerId() + "]: ");
        String ownerInput = sc.nextLine();
        if (!ownerInput.isBlank()) {
            int newOwnerId = Integer.parseInt(ownerInput);

            // Check that owner exists to avoid foreign key violation
            if (site.ownerExists(newOwnerId)) {
                existing.setOwnerId(newOwnerId);
            } else {
                System.out.println("Owner ID " + newOwnerId + " does not exist. Owner not updated.");
            }
        }

        site.updateOwnedSite(existing);
        System.out.println("Owned site updated successfully");
    }

    private void removeSite(Scanner sc) {
        System.out.print("Site ID: ");
        int id = Integer.parseInt(sc.nextLine());

        site.removeSite(id);
        System.out.println("Site removed");
    }

    private void calculateMonthlyMaintenance() {
        maintenance.calculateMonthlyMaintenance();
        System.out.println("Monthly maintenance calculated for all sites.");
    }

    // Admin collects maintenance payment from owner manually
    private void collectMaintenance(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        System.out.print("Amount paid by owner: ");
        double amount = Double.parseDouble(sc.nextLine());

        maintenance.payMaintenance(siteId, amount);
        System.out.println("Payment recorded. Waiting for admin approval.");
    }

    // Admin approves an owner's payment
    private void approveOwnerPayment(Scanner sc) {
        System.out.print("Site ID to approve payment: ");
        int siteId = Integer.parseInt(sc.nextLine());

        admin.approvePayment(siteId);
        System.out.println("Owner payment approved for site " + siteId);
    }

    // Admin views all pending maintenance payments
    private void viewAllPendingMaintenance() {
        var pending = maintenance.getPendingMaintenance();
        if (pending.isEmpty()) {
            System.out.println("No pending maintenance payments.");
            return;
        }
        System.out.println("--- Pending Maintenance ---");
        for (var record : pending) {
            System.out.printf("Site ID: %d | Amount Due: %d | Month: %s%n",
                    record.siteId, record.amount, record.month);
        }
    }

    private void viewPendingSites() {
        List<Site> pending = maintenance.getAllPendingSites();

        if (pending.isEmpty()) {
            System.out.println("No pending sites");
            return;
        }

        pending.forEach(
                site -> System.out.println("Pending Site ID: " + site.getId() + ", Area: " + site.area() + " sq ft"));
    }

    private void approveOrRejectSite(Scanner sc) {
        System.out.print("Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        System.out.print("Approve (A) / Reject (R): ");
        String choice = sc.nextLine();

        if (choice.equalsIgnoreCase("A")) {
            maintenance.approveSiteUpdate(siteId);
            System.out.println("Site approved");
        } else {
            System.out.print("Rejection reason: ");
            String reason = sc.nextLine();
            maintenance.rejectSiteUpdate(siteId, reason);
            System.out.println("Site rejected");
        }
    }
}
