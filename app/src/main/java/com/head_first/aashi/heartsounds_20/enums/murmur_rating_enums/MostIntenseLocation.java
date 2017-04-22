package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum MostIntenseLocation {
    @SerializedName("Mitral")MITRAL("Mitral"),
    @SerializedName("Tricuspid")TRICUSPID("Tricuspid"),
    @SerializedName("Pulmonary")PULMONARY("Pulmonary"),
    @SerializedName("Aortic")AORTIC("Aortic");

    private static Map<String, MostIntenseLocation> valueToLeftMostIntenseLocation = new HashMap<String, MostIntenseLocation>();

    static {
        for(MostIntenseLocation mostIntenseLocation : MostIntenseLocation.values()){
            valueToLeftMostIntenseLocation.put(mostIntenseLocation.value,mostIntenseLocation);
        }
    }

    public static MostIntenseLocation getMostIntenseLocation(String value){
        return valueToLeftMostIntenseLocation.get(value);
    }

    private String value;

    private MostIntenseLocation(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
