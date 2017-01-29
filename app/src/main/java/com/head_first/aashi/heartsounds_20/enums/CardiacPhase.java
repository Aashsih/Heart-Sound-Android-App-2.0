package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum CardiacPhase {
    SYSTOLIC("Systolic"), DIASTOLIC("Diastolic"), CONTINUOUS("Continuous");

    private String value;

    private CardiacPhase(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
