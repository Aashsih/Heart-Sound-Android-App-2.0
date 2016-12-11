package com.head_first.aashi.heartsounds_20.model;

import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidPasswordException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class Password {
    /**
     * http://stackoverflow.com/questions/1205135/how-to-encrypt-string-in-java
     * base64encoder/decoder can be used to encode/decode passwords.
     */
    public static final int MIN_PASSWORD_LENGTH = 5;

    private static final String ENCRYPTION_ALGORITHM = "SHA";

    private String encryptedPassword;

    public Password(String password) {
        this.setEncryptedPassword(password);
    }

    public final String getEncryptedPassword() {
        return this.encryptedPassword;
    }

    public final void setEncryptedPassword(String password) throws InputException{
        this.encryptedPassword = null;
        //make sure the encryptedPassword has min length of MIN_PASSWORD_LENGTH
        if(this.encryptedPassword.length() < MIN_PASSWORD_LENGTH){
            throw new InvalidPasswordException(password);
        }
        try {
            //The protection can be increased by generating and appending a SALT of random length
            MessageDigest messageDigest = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            messageDigest.update(password.getBytes());
            byte[] encryptedPasswordBytes = messageDigest.digest();
            StringBuffer encryptedPasswordBuffer = new StringBuffer();
            for(byte aByte : encryptedPasswordBytes){
                encryptedPasswordBuffer.append(Integer.toHexString(aByte & 0xff).toString());
            }
            this.encryptedPassword = encryptedPasswordBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
