package com.chat.backend.configs;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;
import java.util.UUID;

public class AdminFactory {

    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String FACTORY_ALGO = "PBKDF2WithHmacSHA256";
    private static final String KEY_ALGO = "AES";

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_ALGO);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS_COUNT, KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), KEY_ALGO);
    }

    public static String getNextSalt() {
        return UUID.randomUUID().toString();
    }
}
