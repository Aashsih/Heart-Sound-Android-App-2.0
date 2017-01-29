package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum S2 {
    NORMAL("Normal"), SPLIT("Split"), FIXED_SPLIT("Fixed Split"), REVERSED_SPLIT("Reserved Split"), SINGLE("Single");

    private String value;

    private S2(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
