package com.head_first.aashi.heartsounds_20.exception;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class InvalidNameException extends InputException{
    private static final String INVALID_NAME = "Please enter a name";

    public InvalidNameException(String input) {
        super(input);
    }

    @Override
    public String errorMessage(String input) {
        return INVALID_NAME;
    }
}
