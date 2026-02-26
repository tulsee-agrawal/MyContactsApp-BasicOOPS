package com.contacts;

import java.util.regex.Pattern;

public final class PhoneNumber {
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+()0-9\\-\\s]{5,20}$");

    private final String value;

    public PhoneNumber(String value) {
        if (value == null) throw new IllegalArgumentException("Phone cannot be null");
        String v = value.trim();
        if (v.isEmpty() || !PHONE_PATTERN.matcher(v).matches()) {
            throw new IllegalArgumentException("Invalid phone: " + value);
        }
        this.value = v;
    }

    public String getValue() { return value; }

    @Override public String toString() { return value; }
}