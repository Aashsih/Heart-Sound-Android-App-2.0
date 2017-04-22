package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum S1 {
    @SerializedName("Normal")NORMAL("Normal"),
    @SerializedName("Soft")SOFT("Soft"),
    @SerializedName("Loud")LOUD("Loud");

    private static Map<String, S1> valueToSOne = new HashMap<String, S1>();

    static {
        for(S1 sOne : S1.values()){
            valueToSOne.put(sOne.value,sOne);
        }
    }

    public static S1 getSOne(String value){
        return valueToSOne.get(value);
    }

    private String value;

    private S1(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
