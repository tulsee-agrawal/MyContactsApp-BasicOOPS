package com.contacts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.UserManagement.Email;

public abstract class Contacts {
    private final String id = UUID.randomUUID().toString();
    private final LocalDateTime createdAt = LocalDateTime.now();

    private String name;                    
    private List<PhoneNumber> phones;      
    private List<Email> emails;             
    private String notes;                   

    protected Contacts(String name, List<PhoneNumber> phones, List<Email> emails, String notes) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact name is required.");
        }
        this.name   = name.trim();
        this.phones = (phones == null) ? new ArrayList<>() : new ArrayList<>(phones);
        this.emails = (emails == null) ? new ArrayList<>() : new ArrayList<>(emails);
        this.notes  = (notes == null) ? "" : notes.trim();
    }

    public String getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public String getName() { return name; }
    /** Only subclasses change display name to keep it consistent. */
    protected void setName(String name) {
        if (name != null && !name.trim().isEmpty()) this.name = name.trim();
    }

    public List<PhoneNumber> getPhones() { return new ArrayList<>(phones); }
    public void setPhones(List<PhoneNumber> phones) {
        this.phones = (phones == null) ? new ArrayList<>() : new ArrayList<>(phones);
    }
    public void addPhone(PhoneNumber phone) { if (phone != null) this.phones.add(phone); }

    public List<Email> getEmails() { return new ArrayList<>(emails); }
    public void setEmails(List<Email> emails) {
        this.emails = (emails == null) ? new ArrayList<>() : new ArrayList<>(emails);
    }
    public void addEmail(Email email) { if (email != null) this.emails.add(email); }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = (notes == null) ? "" : notes.trim(); }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name=" + name +
                ", phones=" + phones +
                ", emails=" + emails +
                ", createdAt=" + createdAt +
                (notes.isEmpty() ? "" : ", notes=" + notes) +
                "}";
    }
}