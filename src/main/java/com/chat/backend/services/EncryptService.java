package com.chat.backend.services;

public interface EncryptService {
    /**
     * @param content string to be encrypted with app password and salt
     * @return encrypted string
     */
    byte[] encrypt(String content);

    /**
     * @param encrypted string to be decrypted with app password and salt
     * @return decrypted string
     */
    byte[] decrypt(byte[] encrypted);

}
