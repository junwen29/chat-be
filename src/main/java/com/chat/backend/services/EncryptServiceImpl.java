package com.chat.backend.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

@Service
@Log
public class EncryptServiceImpl implements EncryptService{

    @Autowired
    private Cipher encryptCipher;

    @Autowired
    private Cipher decryptCipher;

    @Override
    public byte[] encrypt(String content) {
        if (encryptCipher != null){
            try {
                return encryptCipher.doFinal(content.getBytes());
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
                log.info(String.format("%s content has illegal block size and unable to encrypt", content));
            } catch (BadPaddingException e) {
                e.printStackTrace();
                log.info(String.format("%s content has bad padding and unable to encrypt", content));
            }
        }
        log.info(String.format("Error with encrypting %s",content));
        return null;
    }

    @Override
    public byte[] decrypt(byte[] encrypted) {
        try {
            return decryptCipher.doFinal(encrypted);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            log.info("%s content has illegal block size and unable to decrypt");
        } catch (BadPaddingException e) {
            e.printStackTrace();
            log.info("%s content has bad padding and unable to decrypt");
        }
        log.info("Error with decrypting %s");
        return null;
    }

}
