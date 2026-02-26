/**
 * App
 * ----------
 * A very simple console program for UC 02 (Authentication).
 * Flow:
 *   1) Creating a demo user for testing.
 *   2) Ask user for email and password.
 *   3) Try to login using BasicAuth (which checks hashed password).
 *   4) If login succeeds, print a dummy contact list.
 *
 *  @author Developer
 *  @version 2.0
 */
package com.app;

import java.util.List;
import java.util.Scanner;

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
                    System.out.println("Exit Successfull!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
            }
        }
    }
    
    // UC 01: Registration flow
    private static void handleRegistration(Scanner sc) {
        System.out.println("\nRegistration(UC 01)");

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

    // UC 02: Login + Contacts
    private static void handleLogin(Scanner sc, Authentication auth) {
        System.out.println("\nLogin (UC 02)");

        System.out.print("Email: ");
        String email = sc.nextLine().trim();

        System.out.print("Password: ");
        String password = sc.nextLine();

        try {
            if (auth.login(email, password)) {
                System.out.println("Login successful.");
                showContacts();
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
            System.out.println(c.getName() + " - " + c.getPhone());
        }
    }
}