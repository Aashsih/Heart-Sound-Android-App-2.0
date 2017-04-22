package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum S2 {
    @SerializedName("Normal")NORMAL("Normal"),
    @SerializedName("Split")SPLIT("Split"),
    @SerializedName("Fixed Split")FIXED_SPLIT("Fixed Split"),
    @SerializedName("Reserved Split")REVERSED_SPLIT("Reserved Split"),
    @SerializedName("Single")SINGLE("Single");

    private static Map<String, S2> valueToSTwo = new HashMap<String, S2>();

    static {
        for(S2 sTwo : S2.values()){
            valueToSTwo.put(sTwo.value,sTwo);
        }
    }

    public static S2 getSTwo(String value){
        return valueToSTwo.get(value);
    }

    private String value;

    private S2(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
