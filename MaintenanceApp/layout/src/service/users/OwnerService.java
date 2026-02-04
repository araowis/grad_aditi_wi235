package service.users;

import service.Service;
import service.factory.*;
import service.handlers.*;
import utils.PasswordHashing;

import java.util.Scanner;

import model.site.OwnedSite;
import model.site.Site;
import model.user.Owner;
import persistence.OwnerInterface;

public class OwnerService implements Service {

    private final OwnerInterface owner;
    private String loggedInUsername;
    private int loggedInOwnerId;

    public OwnerService(OwnerInterface owner) {
        this.owner = owner;
    }

    @Override
    public void run() {
        System.out.println("Owner login opened");

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            LoginHandler loginHandler = LoginFactory.create("-o");

            if (loginHandler.login(username, password, owner)) {
                this.loggedInUsername = username;
                System.out.println("Welcome, Owner " + username);
                Owner ownerObj = owner.viewOwnerDetailsByUsername(username);
                this.loggedInOwnerId = ownerObj.getOwnerId();

                startOwnerOperations();
            } else {
                System.out.println("Owner login failed");
            }
            sc.close();
        }

        catch (Exception e) {
            System.out.println("Error during Owner login");
            e.printStackTrace();
        }
    }

    private void startOwnerOperations() throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("Owner services started");

            if (mustChangePassword()) {
                System.out.println("You must change your password.");
                changePassword(sc);
            }

            boolean running = true;
            while (running) {
                System.out.println("1. View My Sites");
                System.out.println("2. Update Site Request");
                System.out.println("3. Pay Maintenance");
                System.out.println("4. Change Password");
                System.out.println("0. Logout");
                System.out.print("Choice: ");

                int choice = Integer.parseInt(sc.nextLine());
                switch (choice) {
                    case 1 -> viewSiteDetails();
                    case 2 -> requestSiteUpdate(sc);
                    case 3 -> payMaintenance(sc);
                    case 4 -> changePassword(sc);
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice");
                }
            }

            System.out.println("Owner logged out");
        }
    }

    private boolean mustChangePassword() throws Exception {
        // fetch current hash from DB and check if default hash
        String storedHash = owner.fetchHashFromDB(loggedInUsername);
        return PasswordHashing.verifyPassword("changeme", storedHash);
    }

    private void changePassword(Scanner sc) {
        System.out.print("Enter new password: ");
        String newPass = sc.nextLine().trim();
        System.out.print("Confirm new password: ");
        String confirmPass = sc.nextLine().trim();

        if (!newPass.equals(confirmPass)) {
            System.out.println("Passwords do not match!");
            return;
        }

        try {
            String hash = PasswordHashing.hashPassword(newPass);
            owner.updatePassword(loggedInUsername, hash);
            System.out.println("Password updated successfully!");
        } catch (Exception e) {
            System.out.println("Error updating password");
            e.printStackTrace();
        }
    }

    private void viewSiteDetails() {
        Site site = owner.viewOwnSite(loggedInOwnerId);
        if (site == null) {
            System.out.println("No site found or access denied.");
            return;
        }

        System.out.println("----- Site Details -----");
        System.out.println("Site ID: " + site.getId());
        System.out.println("Length: " + site.getLengthInFeet() + " ft");
        System.out.println("Breadth: " + site.getBreadthInFeet() + " ft");
        System.out.println("Area: " + site.area() + " sq ft");
        System.out.println("Owned: " + (site.isOccupied() ? "Yes" : "No"));
    }

    private void requestSiteUpdate(Scanner sc) {
        System.out.print("Enter Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        Site site = owner.viewOwnSite(siteId);
        if (site == null) {
            System.out.println("Site not found or not owned by you.");
            return;
        }

        System.out.print("New length (feet) [" + site.getLengthInFeet() + "]: ");
        String lenInput = sc.nextLine();
        if (!lenInput.isBlank()) {
            site.setLength(Integer.parseInt(lenInput));
        }

        System.out.print("New breadth (feet) [" + site.getBreadthInFeet() + "]: ");
        String brInput = sc.nextLine();
        if (!brInput.isBlank()) {
            site.setBreadth(Integer.parseInt(brInput));
        }

        owner.requestSiteUpdate(site);
        System.out.println("Site update request submitted for admin approval.");
    }

    private void payMaintenance(Scanner sc) {
        System.out.print("Enter Site ID: ");
        int siteId = Integer.parseInt(sc.nextLine());

        OwnedSite ownedSite = owner.viewOwnSite(siteId);
        if (ownedSite == null) {
            System.out.println("Site not found or not owned by you.");
            return;
        }

        int amount = ownedSite.getMaintenancePrice();

        System.out.println("Maintenance due: INR" + amount);
        System.out.print("Confirm payment? (y/n): ");
        String confirm = sc.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Payment cancelled.");
            return;
        }

        owner.payMaintenance(ownedSite, amount);
        System.out.println("Maintenance payment submitted. Awaiting admin approval.");
    }

}
