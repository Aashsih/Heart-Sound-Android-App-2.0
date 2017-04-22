package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Valsalva {
    @SerializedName("Not Done")NOT_DONE("Not Done"),
    @SerializedName("Softer Inspiration")SOFTER_INSPIRATION("Softer Inspiration"),
    @SerializedName("Louder Inspiration")LOUDER_INSPIRATION("Louder Inspiration");

    private static Map<String, Valsalva> valueToValsalva = new HashMap<String, Valsalva>();

    static {
        for(Valsalva valsalva : Valsalva.values()){
            valueToValsalva.put(valsalva.value,valsalva);
        }
    }

    public static Valsalva getValsalva(String value){
        return valueToValsalva.get(value);
    }

    private String value;

    private Valsalva(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
