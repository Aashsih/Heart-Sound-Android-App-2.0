package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum CHARACTER {
    @SerializedName("High Pitched")HIGH_PITCHED("High Pitched"),
    @SerializedName("Low Pitched")LOW_PITCHED("Low Pitched"),
    @SerializedName("Rumbling")RUMBLING("Rumbling");

    private static Map<String, CHARACTER> valueToCharacter = new HashMap<String, CHARACTER>();

    static {
        for(CHARACTER character : CHARACTER.values()){
            valueToCharacter.put(character.value,character);
        }
    }

    public static CHARACTER getCharacter(String value){
        return valueToCharacter.get(value);
    }

    private String value;

    private CHARACTER(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }


}
