package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum S1 {
    NORMAL("Normal"), SOFT("Soft"), LOUD("Loud");

    private String value;

    private S1(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
