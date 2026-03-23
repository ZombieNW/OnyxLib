package com.zombienw.onyxLib.core;

import java.util.regex.Pattern;

public final class OnyxValidation {

    private static final Pattern VALID_ID = Pattern.compile("[a-z0-9_.-]+");

    private OnyxValidation() {}

    public static String requireValidId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ID must not be null or blank.");
        }
        if (!VALID_ID.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid ID '" + value + "': only lowercase letters, digits, '_', '-', and '.' are allowed."
            );
        }
        return value;
    }

    public static String requireValidNamespace(String value) {
        return requireValidId(value);
    }
}
