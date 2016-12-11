package com.head_first.aashi.heartsounds_20.exception;

import com.head_first.aashi.heartsounds_20.model.Password;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class InvalidPasswordException extends InputException{
    private static final String INVALID_PASSWORD_LENGTH = "Password should be atleast" + Password.MIN_PASSWORD_LENGTH + " characters long";

    public InvalidPasswordException(String input) {
        super(input);
    }

    @Override
    public String errorMessage(String input) {
        if(input.length() < Password.MIN_PASSWORD_LENGTH ){
            return INVALID_PASSWORD_LENGTH;
        }
        return "";
    }
}
