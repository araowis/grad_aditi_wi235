package utils;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.security.*;
import java.util.*;

public class PasswordHashing {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) throws Exception {
        char[] pswd = password.toCharArray();
        byte[] salt = new byte[SALT_LENGTH];
        SecureRandom.getInstanceStrong().nextBytes(salt);

        byte[] hash = pbkdf2(pswd, salt, ITERATIONS, KEY_LENGTH);

        return (ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" +  Base64.getEncoder().encodeToString(hash));
    }

    public static boolean verifyPassword(String password, String stored) throws Exception {
        char[] pswd = password.toCharArray();
        String[] parts = stored.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        byte[] testHash = pbkdf2(pswd, salt, iterations, hash.length * 8);

        return slowEquals(hash, testHash);
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }
}
