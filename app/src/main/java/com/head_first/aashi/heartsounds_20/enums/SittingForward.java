package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum SittingForward {
    NOT_DONE("Not done"), LOUDER("Louder"), SOFTER("Softer"), NO_CHANGE("No Change");

    private String value;

    private SittingForward(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
