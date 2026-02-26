package com.contacts;

import java.util.*;
import com.UserManagement.User;
import com.UserManagement.Email;

public class ContactService {
    private static final Map<String, List<Contacts>> store = new HashMap<>();

    private static String key(User owner) {
        return owner.getEmail().getValue().trim().toLowerCase();
    }

    public void addContact(User owner, Contacts contact) {
        if (owner == null) throw new IllegalArgumentException("Owner is required.");
        if (contact == null) throw new IllegalArgumentException("Contact is required.");
        store.computeIfAbsent(key(owner), _k -> new ArrayList<>()).add(contact);
    }

    public List<Contacts> listContacts(User owner) {
        if (owner == null) return Collections.emptyList();
        return new ArrayList<>(store.getOrDefault(key(owner), Collections.emptyList()));
    }

    public List<Contacts> getDummyContacts() {
        List<Contacts> demo = new ArrayList<>();
        try {
            var phones1 = Arrays.asList(new PhoneNumber("+91 98765 43210"));
            var emails1 = Arrays.asList(new Email("t@test.com"));
            demo.add(new PersonContact("tulsee", "ag", phones1, emails1, "friend"));

            var phones2 = Arrays.asList(new PhoneNumber("080-1234567"));
            var emails2 = Arrays.asList(new Email("contact@acorp.com"));
            demo.add(new OrganizationContact("A Corp", phones2, emails2, "Vendor"));
        } catch (IllegalArgumentException ignore) { }
        return demo;
    }
}