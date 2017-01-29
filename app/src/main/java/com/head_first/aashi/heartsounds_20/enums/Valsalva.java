package com.head_first.aashi.heartsounds_20.enums;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Valsalva {
    NOT_DONE("Not Done"), SOFTER_INSPIRATION("Softer Inspiration"), LOUDER_INSPIRATION("Louder Inspiration");

    private String value;

    private Valsalva(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
