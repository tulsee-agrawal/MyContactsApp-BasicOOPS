/**
 * App
 * ----------
 * A very simple console program for:
 *   UC 01: Registration
 *   UC 02: Authentication + Dummy Contacts
 *   UC 03: Profile Management (view, update name, change password)
 *   UC 04: Contact Management (hierarchy-Person/Organization)
 *
 * Flow:
 *   1) Register → validates, hashes, stores user
 *   2) Login → authenticates against stored users
 *   3) After login → shows dummy contacts + contact menu + profile menu
 *
 *  @author Developer
 *  @version 4.0
 */
package com.app;

import java.util.*;

// UC-04
import com.contacts.PhoneNumber;
import com.contacts.PersonContact;
import com.contacts.OrganizationContact;

// UC-03
import com.profile.ProfileService;

// UC-02 (Authentication + store + contacts)
import com.auth.Authentication;
import com.auth.BasicAuth;
import com.auth.AuthException;
import com.auth.UserDatabase;

import com.contacts.Contacts;
import com.contacts.ContactService;

import com.UserManagement.Email;
import com.UserManagement.User;
import com.UserManagement.FreeUser;
import com.UserManagement.PremiumUser;
import com.UserManagement.PasswordValidation;
import com.UserManagement.PasswordHashing;
import com.UserManagement.UserException;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Authentication auth = new BasicAuth(UserDatabase.getUsers());

        while (true) {
            System.out.println("\nContact App");
            System.out.println("1) Register (UC 01)");
            System.out.println("2) Login (UC 02)");
            System.out.println("3) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegistration(sc);
                    break;
                case "2":
                    handleLogin(sc, auth);
                    break;
                case "3":
                    System.out.println("Exit Successful!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }

    // UC 01: Registration flow
    private static void handleRegistration(Scanner sc) {
        System.out.println("\nRegistration (UC 01)");

        try {
            // Ask inputs
            System.out.print("Enter user type (free/premium): ");
            String type = sc.nextLine().trim();

            System.out.print("Enter email: ");
            String emailRaw = sc.nextLine().trim();

            System.out.print("Enter name: ");
            String name = sc.nextLine().trim();

            System.out.print("Enter password: ");
            String passwordRaw = sc.nextLine();

            // Validate email + password
            Email email = new Email(emailRaw);
            PasswordValidation.validate(passwordRaw);
            String pwHash = PasswordHashing.hash(passwordRaw);

            // Build user type
            User user;
            if (type.equalsIgnoreCase("free")) {
                user = new FreeUser(email, pwHash, name);
            } else if (type.equalsIgnoreCase("premium")) {
                user = new PremiumUser(email, pwHash, name);
            } else {
                throw new UserException("Invalid user type: " + type);
            }

            // Store in UC 02 DB for later login
            UserDatabase.addUser(user);

            System.out.println("User registered: " + user);
            System.out.println("(You can now choose option 2 to log in.)");

        } catch (UserException ue) {
            // Your validation or constructor errors
            System.out.println("Registration failed: " + ue.getMessage());
        } catch (IllegalArgumentException iae) {
            // Email invalid format
            System.out.println("Registration failed: " + iae.getMessage());
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    // UC 02: Login + Contacts + Profile
    private static void handleLogin(Scanner sc, Authentication auth) {
        System.out.println("\nLogin (UC 02)");

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        try {
            if (auth.login(email, password)) {
                System.out.println("Login successful.");

                // Load the logged-in user from DB
                User currentUser = UserDatabase.findByEmail(email);
                if (currentUser == null) {
                    System.out.println("Unexpected: user not found after login.");
                    return;
                }

                // UC 02: show dummy contact list
                showContacts();

                // UC-04: hierarchy contact management
                showContactMenu(sc, currentUser);

                // UC 03: open the profile menu for the logged-in user
                showProfileMenu(sc, currentUser);
            }
        } catch (AuthException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void showContacts() {
        ContactService cs = new ContactService();
        List<Contacts> contacts = cs.getDummyContacts();

        System.out.println("\nYour Contact List:");
        for (Contacts c : contacts) {
            String phones = c.getPhones().isEmpty()
                    ? "-"
                    : c.getPhones().stream().map(PhoneNumber::getValue).reduce((a, b) -> a + ", " + b).orElse("-");
            System.out.println(c.getName() + " - " + phones);
        }
    }

    // UC 03: Profile menu
    private static void showProfileMenu(Scanner sc, User user) {
        ProfileService ps = new ProfileService();

        while (true) {
            System.out.println("\nUC 03: Profile Management");
            System.out.println("1) View Profile");
            System.out.println("2) Update Name");
            System.out.println("3) Change Password");
            System.out.println("4) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        ps.viewProfile(user);
                        break;

                    case "2":
                        System.out.print("Enter new name: ");
                        String newName = sc.nextLine();
                        ps.updateName(user, newName);
                        System.out.println("Name updated successfully.");
                        break;

                    case "3":
                        System.out.print("Enter old password: ");
                        String oldPw = sc.nextLine();
                        System.out.print("Enter new password: ");
                        String newPw = sc.nextLine();
                        ps.changePassword(user, oldPw, newPw);
                        System.out.println("Password changed successfully.");
                        break;

                    case "4":
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (UserException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // UC-04: Contact Management menu
    private static void showContactMenu(Scanner sc, User currentUser) {
        ContactService service = new ContactService();

        while (true) {
            System.out.println("\nUC 04: Contact Management");
            System.out.println("1) Create Person Contact");
            System.out.println("2) Create Organization Contact");
            System.out.println("3) List My Contacts");
            System.out.println("4) Back");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    createPersonContactFlow(sc, currentUser, service);
                    break;
                case "2":
                    createOrgContactFlow(sc, currentUser, service);
                    break;
                case "3":
                    listContactsFlow(currentUser, service);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void createPersonContactFlow(Scanner sc, User owner, ContactService service) {
        try {
            System.out.print("First name (required): ");
            String first = sc.nextLine().trim();
            System.out.print("Last name (optional): ");
            String last = sc.nextLine().trim();

            List<PhoneNumber> phones = readPhones(sc);
            List<com.UserManagement.Email> emails = readEmails(sc);

            System.out.print("Notes (optional): ");
            String notes = sc.nextLine();

            Contacts c = new PersonContact(first, last, phones, emails, notes);
            service.addContact(owner, c);
            System.out.println("Person contact created:" + c.getName() + " (ID:" + c.getId() + ")");
        } catch (Exception e) {
            System.out.println("Failed to create person contact: " + e.getMessage());
        }
    }

    private static void createOrgContactFlow(Scanner sc, User owner, ContactService service) {
        try {
            System.out.print("Company name (required): ");
            String company = sc.nextLine().trim();

            List<PhoneNumber> phones = readPhones(sc);
            List<com.UserManagement.Email> emails = readEmails(sc);

            System.out.print("Notes (optional): ");
            String notes = sc.nextLine();

            Contacts c = new OrganizationContact(company, phones, emails, notes);
            service.addContact(owner, c);
            System.out.println("Organization contact created: " + c.getName() + " (ID: " + c.getId() + ")");
        } catch (Exception e) {
            System.out.println("Failed to create organization contact: " + e.getMessage());
        }
    }

    private static void listContactsFlow(User owner, ContactService service) {
        List<Contacts> list = service.listContacts(owner);
        if (list.isEmpty()) {
            System.out.println("No contacts found.");
            return;
        }
        System.out.println("\nYour Contacts:");
        for (Contacts c : list) {
            String phones = c.getPhones().isEmpty()
                    ? "-"
                    : c.getPhones().stream().map(PhoneNumber::getValue).reduce((a, b) -> a + ", " + b).orElse("-");
            String emails = c.getEmails().isEmpty()
                    ? "-"
                    : c.getEmails().stream().map(com.UserManagement.Email::getValue).reduce((a, b) -> a + ", " + b).orElse("-");
            String type = c.getClass().getSimpleName();
            System.out.println("- [" + type + "] " + c.getName()
                    + " | Phones: " + phones
                    + " | Emails: " + emails
                    + " | Created: " + c.getCreatedAt());
        }
    }

    private static java.util.List<PhoneNumber> readPhones(Scanner sc) {
        System.out.print("Phone numbers (comma separated, optional): ");
        String raw = sc.nextLine();
        java.util.List<PhoneNumber> out = new java.util.ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            for (String p : raw.split(",")) {
                String v = p.trim();
                if (!v.isEmpty()) out.add(new PhoneNumber(v)); // validates
            }
        }
        return out;
    }

    private static java.util.List<com.UserManagement.Email> readEmails(Scanner sc) {
        System.out.print("Emails (optional): ");
        String raw = sc.nextLine();
        java.util.List<com.UserManagement.Email> out = new java.util.ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            for (String e : raw.split(",")) {
                String v = e.trim();
                if (!v.isEmpty()) out.add(new com.UserManagement.Email(v));
            }
        }
        return out;
    }
}