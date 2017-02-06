package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Intensity {
    I("I"), II("II"), III("III"), IV("IV"), V("V"), VI("VI");

    private static Map<String, Intensity> valueToIntensity = new HashMap<String, Intensity>();

    static {
        for(Intensity intensity : Intensity.values()){
            valueToIntensity.put(intensity.value,intensity);
        }
    }

    public static Intensity getIntensity(String value){
        return valueToIntensity.get(value);
    }

    private String value;

    private Intensity(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
