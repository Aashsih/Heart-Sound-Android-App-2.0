package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum ChangeWithBreathing {
    NOT_DONE("Not Done"), SOFTER_INSPIRATION("Softer Inspiration"), LOUDER_INSPIRATION("Louder Inspiration"),
    SOFTER_EXPIRATION("Softer Expiration"), LOUDER_EXPIRATION("Louder Expiration"), NO_CHANGE("No Change");

    private String value;

    private ChangeWithBreathing(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
