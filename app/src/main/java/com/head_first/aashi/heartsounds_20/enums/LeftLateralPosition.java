package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum LeftLateralPosition {
    NOT_DONE("Not Done"), LOUDER("Louder"), SOFTER("Softer"), NO_CHANGE("No Change");

    private String value;

    private LeftLateralPosition(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
