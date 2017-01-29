package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum AddedSounds {
    NIL("nil"), S3("S3"), S4("S4"), EJECTION_CLICK("Ejection Click"), SYSTOLIC_CLICK("Systolic Click");

    private String value;

    private AddedSounds(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
