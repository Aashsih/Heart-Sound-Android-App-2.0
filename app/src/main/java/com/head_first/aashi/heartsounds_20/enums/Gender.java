package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Gender {
    MALE("M"), FEMALE("F"), OTHER("Other"); // confirm

    private String value;

    private Gender(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
