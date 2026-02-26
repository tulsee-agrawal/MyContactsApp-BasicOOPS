package com.contacts;

import java.util.List;
import com.UserManagement.Email;

public class PersonContact extends Contacts {
    private String firstName;
    private String lastName;

    public PersonContact(String firstName,
                         String lastName,
                         List<PhoneNumber> phones,
                         List<Email> emails,
                         String notes) {
        super(buildDisplayName(firstName, lastName), phones, emails, notes);
        this.firstName = (firstName == null) ? "" : firstName.trim();
        this.lastName  = (lastName  == null) ? "" : lastName.trim();
    }

    private static String buildDisplayName(String first, String last) {
        String f = (first == null) ? "" : first.trim();
        String l = (last  == null) ? "" : last.trim();
        String full = (f + " " + l).trim();
        if (full.isEmpty()) throw new IllegalArgumentException("First or last name required.");
        return full;
    }

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName;  }

    public void setFirstName(String firstName) {
        this.firstName = (firstName == null) ? "" : firstName.trim();
        super.setName(buildDisplayName(this.firstName, this.lastName));
    }

    public void setLastName(String lastName) {
        this.lastName = (lastName == null) ? "" : lastName.trim();
        super.setName(buildDisplayName(this.firstName, this.lastName));
    }
}