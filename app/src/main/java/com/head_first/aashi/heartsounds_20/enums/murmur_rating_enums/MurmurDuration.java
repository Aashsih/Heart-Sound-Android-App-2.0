package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum MurmurDuration {
    @SerializedName("Ejection Systolic")EJECTION_SYSTOLIC("Ejection Systolic"),
    @SerializedName("Mid Systolic")MID_SYSTOLIC("Mid Systolic"),
    @SerializedName("Late Systolic")LATE_SYSTOLIC("Late Systolic"),
    @SerializedName("Holosystolic")HOLOSYSTOLIC("Holosystolic"),
    @SerializedName("Early Diasystolic")EARLY_DIASYSTOLIC("Early Diasystolic"),
    @SerializedName("Mid Diasystolic")MID_DIASYSTOLIC("Mid Diasystolic");

    private static Map<String, MurmurDuration> valueToMurmurDuration = new HashMap<String, MurmurDuration>();

    static {
        for(MurmurDuration murmurDuration : MurmurDuration.values()){
            valueToMurmurDuration.put(murmurDuration.value,murmurDuration);
        }
    }

    public static MurmurDuration getMurmurDuration(String value){
        return valueToMurmurDuration.get(value);
    }

    private String value;

    private MurmurDuration(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
