package com.aulikcz.loop.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;

public class AuthUtils {
    private AuthUtils() {}   // nikdo nemá vytvářet instance

    public static String sha256Hex(String value) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String normalizeEmail(String value) {
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
