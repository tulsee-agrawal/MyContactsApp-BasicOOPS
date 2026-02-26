package com.contacts;

import java.util.List;
import com.UserManagement.Email;

public class OrganizationContact extends Contacts {
    private String companyName;

    public OrganizationContact(String companyName,
                               List<PhoneNumber> phones,
                               List<Email> emails,
                               String notes) {
        super(validateCompany(companyName), phones, emails, notes);
        this.companyName = companyName.trim();
    }

    private static String validateCompany(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name is required.");
        }
        return name.trim();
    }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String name) {
        if (name == null || name.trim().isEmpty()) return;
        this.companyName = name.trim();
        super.setName(this.companyName); 
    }
}