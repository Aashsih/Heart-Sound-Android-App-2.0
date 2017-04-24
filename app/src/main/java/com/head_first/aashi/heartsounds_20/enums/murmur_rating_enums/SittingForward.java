package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum SittingForward {
    @SerializedName("Not Done")NOT_DONE("Not Done"),
    @SerializedName("Louder")LOUDER("Louder"),
    @SerializedName("Softer")SOFTER("Softer"),
    @SerializedName("No Change")NO_CHANGE("No Change");

    private static Map<String, SittingForward> valueToSittingForward = new HashMap<String, SittingForward>();

    static {
        for(SittingForward sittingForward : SittingForward.values()){
            valueToSittingForward.put(sittingForward.value,sittingForward);
        }
    }

    public static SittingForward getSittingForward(String value){
        return valueToSittingForward.get(value);
    }

    private String value;

    private SittingForward(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
