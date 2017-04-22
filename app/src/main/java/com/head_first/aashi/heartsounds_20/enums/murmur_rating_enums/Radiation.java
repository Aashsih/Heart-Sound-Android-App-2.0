package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Radiation {
    @SerializedName("Axilla")AXILLA("Axilla"),
    @SerializedName("Llse")LLSE("Llse"),
    @SerializedName("Ulse")ULSE("Ulse"),
    @SerializedName("Urse")URSE("Urse"),
    @SerializedName("Carotid")CAROTID("Carotid"),
    @SerializedName("Back")BACK("Back"),
    @SerializedName("Pulmonary")PULMONARY("Pulmonary"),
    @SerializedName("Epigastrium")EPIGASTRIUM("Epigastrium");

    private static Map<String, Radiation> valueToRadiation = new HashMap<String, Radiation>();

    static {
        for(Radiation radiation : Radiation.values()){
            valueToRadiation.put(radiation.value,radiation);
        }
    }

    public static Radiation getRadiation(String value){
        return valueToRadiation.get(value);
    }

    private String value;

    private Radiation(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
