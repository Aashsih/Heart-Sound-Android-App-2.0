package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum MostIntenseLocation {
    MITRAL("Mitral"), TRICUSPID("Tricuspid"), PULMONARY("Pulmonary"), AORTIC("Aortic");

    private String value;

    private MostIntenseLocation(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
