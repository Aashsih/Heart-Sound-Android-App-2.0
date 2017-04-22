package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum MurmurDuration {
    EJECTION_SYSTOLIC("Ejection Systolic"), MID_SYSTOLIC("Mid Systolic"), LATE_SYSTOLIC("Late Systolic"), HOLOSYSTOLIC("Holosystolic"),
    EARLY_DIASYSTOLIC("Early Diasystolic"), MID_DIASYSTOLIC("Mid Diasystolic");

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
