package com.UserManagement;

public class Main{

    public static void main(String[] args) {

        try {
           
            String type = "premium";    
            String emailRaw = "tulsee@example.com";
            String passwordRaw = "Password123";
            String name = "Tulsee";
            
            Email email = new Email(emailRaw);
            PasswordValidation.validate(passwordRaw);
            String pwHash = PasswordHashing.hash(passwordRaw);

            User user;

            if (type.equalsIgnoreCase("free")) {
                user = new FreeUser(email, pwHash, name);
            }
            else if (type.equalsIgnoreCase("premium")) {
                user = new PremiumUser(email, pwHash, name);
            }
            else {
                throw new UserException("Invalid user type: " + type);
            }

            System.out.println("User registered: " + user);

        } catch (UserException e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }
}
