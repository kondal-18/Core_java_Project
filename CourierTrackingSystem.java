package com.BusPassManagement;


import java.util.*;
import java.io.*;

public class CourierTrackingSystem {

    static Scanner sc = new Scanner(System.in);
    static HashMap<String, Courier> couriers = new HashMap<>();
    static HashMap<String, String> users = new HashMap<>();

    static class Courier implements Serializable {
        String id, sender, receiver, contact;
        String source, destination, status;

        Courier(String id, String sender, String receiver,
                String contact, String source, String destination) {
            this.id = id;
            this.sender = sender;
            this.receiver = receiver;
            this.contact = contact;
            this.source = source;
            this.destination = destination;
            this.status = "BOOKED";
        }

        void display() {
            System.out.println("\nTracking ID : " + id);
            System.out.println("Sender      : " + sender);
            System.out.println("Receiver    : " + receiver);
            System.out.println("Contact     : " + contact);
            System.out.println("Source      : " + source);
            System.out.println("Destination : " + destination);
            System.out.println("Status      : " + status);
        }
    }

    static void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("couriers.dat"))) {
            out.writeObject(couriers);
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    @SuppressWarnings("unchecked")
    static void loadData() {
        File file = new File("couriers.dat");

        if (file.exists()) {
            try (ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(file))) {
                couriers = (HashMap<String, Courier>) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data.");
            }
        }

        users.put("admin", "admin123");
    }

    static void register() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (password.length() < 6) {
            System.out.println("Password must have 6 characters.");
            return;
        }

        users.put(username, password);
        System.out.println("Registration successful.");
    }

    static boolean login() {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (users.containsKey(username)
                && users.get(username).equals(password)) {
            System.out.println("Login successful.");
            return true;
        }

        System.out.println("Invalid username or password.");
        return false;
    }

    static void bookCourier() {
        System.out.print("Enter tracking ID: ");
        String id = sc.nextLine();

        if (couriers.containsKey(id)) {
            System.out.println("Tracking ID already exists.");
            return;
        }

        System.out.print("Enter sender name: ");
        String sender = sc.nextLine();

        System.out.print("Enter receiver name: ");
        String receiver = sc.nextLine();

        System.out.print("Enter contact number: ");
        String contact = sc.nextLine();

        if (!contact.matches("[0-9]{10}")) {
            System.out.println("Enter a valid 10-digit contact.");
            return;
        }

        System.out.print("Enter source: ");
        String source = sc.nextLine();

        System.out.print("Enter destination: ");
        String destination = sc.nextLine();

        couriers.put(id, new Courier(id, sender, receiver,
                contact, source, destination));

        saveData();
        System.out.println("Courier booked successfully.");
    }

    static void trackCourier() {
        System.out.print("Enter tracking ID: ");
        String id = sc.nextLine();

        Courier c = couriers.get(id);

        if (c != null) {
            c.display();
        } else {
            System.out.println("Courier not found.");
        }
    }

    static void viewCouriers() {
        if (couriers.isEmpty()) {
            System.out.println("No courier records found.");
            return;
        }

        for (Courier c : couriers.values()) {
            c.display();
        }
    }

    static void updateCourier() {
        System.out.print("Enter tracking ID: ");
        String id = sc.nextLine();

        Courier c = couriers.get(id);

        if (c == null) {
            System.out.println("Courier not found.");
            return;
        }

        System.out.println("1. Update sender");
        System.out.println("2. Update receiver");
        System.out.println("3. Update contact");
        System.out.println("4. Update source");
        System.out.println("5. Update destination");

        int choice = readChoice();

        switch (choice) {
            case 1:
                System.out.print("New sender name: ");
                c.sender = sc.nextLine();
                break;
            case 2:
                System.out.print("New receiver name: ");
                c.receiver = sc.nextLine();
                break;
            case 3:
                System.out.print("New contact: ");
                String contact = sc.nextLine();
                if (!contact.matches("[0-9]{10}")) {
                    System.out.println("Invalid contact.");
                    return;
                }
                c.contact = contact;
                break;
            case 4:
                System.out.print("New source: ");
                c.source = sc.nextLine();
                break;
            case 5:
                System.out.print("New destination: ");
                c.destination = sc.nextLine();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        saveData();
        System.out.println("Courier updated successfully.");
    }

    static void updateStatus() {
        System.out.print("Enter tracking ID: ");
        String id = sc.nextLine();

        Courier c = couriers.get(id);

        if (c == null) {
            System.out.println("Courier not found.");
            return;
        }

        System.out.println("1. BOOKED");
        System.out.println("2. PICKED UP");
        System.out.println("3. IN TRANSIT");
        System.out.println("4. OUT FOR DELIVERY");
        System.out.println("5. DELIVERED");
        System.out.println("6. CANCELLED");

        int choice = readChoice();

        switch (choice) {
            case 1: c.status = "BOOKED"; break;
            case 2: c.status = "PICKED UP"; break;
            case 3: c.status = "IN TRANSIT"; break;
            case 4: c.status = "OUT FOR DELIVERY"; break;
            case 5: c.status = "DELIVERED"; break;
            case 6: c.status = "CANCELLED"; break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        saveData();
        System.out.println("Courier status updated.");
    }

    static void deleteCourier() {
        System.out.print("Enter tracking ID: ");
        String id = sc.nextLine();

        if (couriers.remove(id) != null) {
            saveData();
            System.out.println("Courier deleted successfully.");
        } else {
            System.out.println("Courier not found.");
        }
    }

    static void report() {
        int delivered = 0;
        int inTransit = 0;
        int booked = 0;
        int cancelled = 0;

        for (Courier c : couriers.values()) {
            switch (c.status) {
                case "DELIVERED": delivered++; break;
                case "IN TRANSIT":
                case "PICKED UP":
                case "OUT FOR DELIVERY": inTransit++; break;
                case "BOOKED": booked++; break;
                case "CANCELLED": cancelled++; break;
            }
        }

        System.out.println("\n===== COURIER REPORT =====");
        System.out.println("Total Couriers: " + couriers.size());
        System.out.println("Delivered     : " + delivered);
        System.out.println("In Transit    : " + inTransit);
        System.out.println("Booked        : " + booked);
        System.out.println("Cancelled     : " + cancelled);
    }

    static int readChoice() {
        try {
            System.out.print("Enter choice: ");
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
            return -1;
        }
    }

    static void userMenu() {
        while (true) {
            System.out.println("\n===== USER MENU =====");
            System.out.println("1. Book Courier");
            System.out.println("2. Track Courier");
            System.out.println("3. Update Courier");
            System.out.println("4. View All Couriers");
            System.out.println("5. Generate Report");
            System.out.println("6. Logout");

            switch (readChoice()) {
                case 1: bookCourier(); break;
                case 2: trackCourier(); break;
                case 3: updateCourier(); break;
                case 4: viewCouriers(); break;
                case 5: report(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    static void adminMenu() {
        while (true) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. View All Couriers");
            System.out.println("2. Track Courier");
            System.out.println("3. Update Courier Status");
            System.out.println("4. Delete Courier");
            System.out.println("5. Generate Report");
            System.out.println("6. Logout");

            switch (readChoice()) {
                case 1: viewCouriers(); break;
                case 2: trackCourier(); break;
                case 3: updateStatus(); break;
                case 4: deleteCourier(); break;
                case 5: report(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n===== COURIER TRACKING SYSTEM =====");
            System.out.println("1. User Registration");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");

            int choice = readChoice();

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    if (login()) userMenu();
                    break;
                case 3:
                    System.out.println("Admin login");
                    if (loginAsAdmin()) adminMenu();
                    break;
                case 4:
                    saveData();
                    System.out.println("Thank you!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static boolean loginAsAdmin() {
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        if (username.equals("admin")
                && password.equals("admin123")) {
            System.out.println("Admin login successful.");
            return true;
        }

        System.out.println("Invalid admin credentials.");
        return false;
    }
}
