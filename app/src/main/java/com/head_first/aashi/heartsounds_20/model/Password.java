package com.head_first.aashi.heartsounds_20.model;

import android.content.Context;

import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CHARACTER;
import com.head_first.aashi.heartsounds_20.exception.InputException;
import com.head_first.aashi.heartsounds_20.exception.InvalidPasswordException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class will not be utilized currently.
 * Currently the authorization and security for
 * passwords is handled by the server and this
 * class will be used incase the app needs to
 * migrate to a server that cannot provide the
 * required security.
 */

public final class Password {
    /**
     * http://stackoverflow.com/questions/1205135/how-to-encrypt-string-in-java
     * base64encoder/decoder can be used to encode/decode passwords.
     */
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final String CORRECT_PASSWORD_FORMAT = "Password must contain:\n" +
            "1. Minimum 6 Characters\n" +
            "2. 1 Special Character\n" +
            "3. 1 Uppercase Character\n" +
            "4. 1 Number";
    private static final String ENCRYPTION_ALGORITHM = "SHA";


    public static boolean isPasswordFormatCorrect(String enteredPassword){
        boolean hasUppercase = false;
        boolean hasDigit = false;
        if(enteredPassword.length() < MIN_PASSWORD_LENGTH){
           return false;
        }
        else if(!enteredPassword.matches("[A-Za-z0-9]*")){
            for(char character : enteredPassword.toCharArray()){
                if(!hasDigit){
                    hasDigit = Character.isDigit(character);
                }
                if(!hasUppercase){
                    hasUppercase = Character.isUpperCase(character);
                }
            }
        }
        if(hasDigit && hasUppercase){
            return true;
        }
        return false;
    }

    public static final String getEncryptedPassword(String password) throws InputException{
        String encryptedPassword = null;
        //make sure the encryptedPassword has min length of MIN_PASSWORD_LENGTH
        if(password.length() < MIN_PASSWORD_LENGTH){
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
            encryptedPassword = encryptedPasswordBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedPassword;
    }
}
