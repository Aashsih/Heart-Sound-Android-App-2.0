package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum AddedSounds {
    @SerializedName("nil")NIL("nil"),
    @SerializedName("S3")S3("S3"),
    @SerializedName("S4")S4("S4"),
    @SerializedName("Ejection Click")EJECTION_CLICK("Ejection Click"),
    @SerializedName("Systolic Click")SYSTOLIC_CLICK("Systolic Click");

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
