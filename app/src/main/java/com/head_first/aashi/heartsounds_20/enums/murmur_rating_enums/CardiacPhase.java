package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum CardiacPhase {
    @SerializedName("Systolic")SYSTOLIC("Systolic"),
    @SerializedName("Diastolic")DIASTOLIC("Diastolic"),
    @SerializedName("Continuous")CONTINUOUS("Continuous");

    private static Map<String, CardiacPhase> valueToCardiacPhase = new HashMap<String, CardiacPhase>();

    static {
        for(CardiacPhase cardiacPhase : CardiacPhase.values()){
            valueToCardiacPhase.put(cardiacPhase.value,cardiacPhase);
        }
    }

    public static CardiacPhase getCardiacPhase(String value){
        return valueToCardiacPhase.get(value);
    }

    private String value;

    private CardiacPhase(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
