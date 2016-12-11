package com.head_first.aashi.heartsounds_20.exception;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class InvalidUsernameException extends InputException{

    private static final String INVALID_USERNAME = "Invalid Username";
    private static final String USERNAME_ALREADY_EXISTS = "Username Already Exists";

    public InvalidUsernameException(String input) {
        super(input);
    }

    @Override
    public String errorMessage(String input) {
        if(input == null || input.equals("")){
            return INVALID_USERNAME;
        }
        else{
            return USERNAME_ALREADY_EXISTS;
        }
    }
}
