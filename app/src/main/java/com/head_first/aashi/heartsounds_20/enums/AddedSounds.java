package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum AddedSounds {
    NIL("nil"), S3("S3"), S4("S4"), EJECTION_CLICK("Ejection Click"), SYSTOLIC_CLICK("Systolic Click");

    private static Map<String, AddedSounds> valueToAddedSounds = new HashMap<String, AddedSounds>();

    static {
        for(AddedSounds addedSounds : AddedSounds.values()){
            valueToAddedSounds.put(addedSounds.value,addedSounds);
        }
    }

    public static AddedSounds getAddedSounds(String value){
        return valueToAddedSounds.get(value);
    }

    private String value;

    private AddedSounds(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
