package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum ChangeWithBreathing {
    @SerializedName("Not Done")NOT_DONE("Not Done"),
    @SerializedName("Softer Inspiration")SOFTER_INSPIRATION("Softer Inspiration"),
    @SerializedName("Louder Inspiration")LOUDER_INSPIRATION("Louder Inspiration"),
    @SerializedName("Softer Expiration")SOFTER_EXPIRATION("Softer Expiration"),
    @SerializedName("Louder Expiration")LOUDER_EXPIRATION("Louder Expiration"),
    @SerializedName("No Change")NO_CHANGE("No Change");

    private static Map<String, ChangeWithBreathing> valueToChangeWithBreathing = new HashMap<String, ChangeWithBreathing>();

    static {
        for(ChangeWithBreathing changeWithBreathing : ChangeWithBreathing.values()){
            valueToChangeWithBreathing.put(changeWithBreathing.value,changeWithBreathing);
        }
    }

    public static ChangeWithBreathing getChangeWithBreathing(String value){
        return valueToChangeWithBreathing.get(value);
    }

    private String value;

    private ChangeWithBreathing(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
