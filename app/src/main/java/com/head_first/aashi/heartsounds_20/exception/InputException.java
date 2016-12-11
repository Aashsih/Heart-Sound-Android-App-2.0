package com.head_first.aashi.heartsounds_20.exception;

import java.util.Date;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public abstract class InputException extends IllegalArgumentException{
    /**
     * We could also use tagging for each exception instead of making separate classes
     * and then use switch case to identify the error
     */
    private Date time;
    private String input;

    public InputException (String input){
        this.time = new Date();
        this.input = input;
    }

    public abstract String errorMessage(String input);
}
