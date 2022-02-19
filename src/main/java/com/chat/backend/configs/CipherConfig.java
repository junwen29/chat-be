package com.chat.backend.configs;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Log
public class CipherConfig {

    @Value("${app.encrypt.password}")
    private String encryptPassword;

    @Value("${app.encrypt.salt}")
    private String encryptSalt;

    private static final String ALGO = "AES/ECB/PKCS5Padding";

    @Bean
    public Cipher encryptCipher(){

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGO);
            SecretKey key;

//            try {
//                key = AdminFactory.getKeyFromPassword(encryptPassword, encryptSalt);
            key = new SecretKeySpec("thisisa128bitkey".getBytes(), "AES");

            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return cipher;

            } catch (InvalidKeyException e) {
                e.printStackTrace();
                log.info("Unable to init Cipher Instance");
            }

//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//                log.info(String.format("Error with getting key as algorithm %s is wrong.", ALGO));
//
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//                log.info("Error with getting key as key generated is invalid");
//            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.info(String.format("Error with getting cipher instance as algorithm %s is wrong.", ALGO));

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            log.info("Error with getting cipher instance with no such padding exception");
        }

        return null;

    }

    @Bean
    public Cipher decryptCipher(){

        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGO);
            SecretKey key;

//            try {
//                key = AdminFactory.getKeyFromPassword(encryptPassword, encryptSalt);
                key = new SecretKeySpec("thisisa128bitkey".getBytes(), "AES");

                try {
                    cipher.init(Cipher.DECRYPT_MODE, key);
                    return cipher;

                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                    log.info("Unable to init Cipher Instance");
                }

//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//                log.info(String.format("Error with getting key as algorithm %s is wrong.", ALGO));
//
//            } catch (InvalidKeySpecException e) {
//                e.printStackTrace();
//                log.info("Error with getting key as key generated is invalid");
//            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.info(String.format("Error with getting cipher instance as algorithm %s is wrong.", ALGO));

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            log.info("Error with getting cipher instance with no such padding exception");
        }

        return null;
    }
}
