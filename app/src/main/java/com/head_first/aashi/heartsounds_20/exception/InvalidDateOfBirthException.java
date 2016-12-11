package com.head_first.aashi.heartsounds_20.exception;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public final class InvalidDateOfBirthException extends InputException{
    private static final String NULL_DOB = "Please enter a date of birth";
    private static final String INVALID_DOB = "Please enter a valid date of birth";

    public InvalidDateOfBirthException(String input) {
        super(input);
    }

    @Override
    public String errorMessage(String input) {
        if(input == null){
            return NULL_DOB;
        }
        else{
            return INVALID_DOB;
        }
    }

}
