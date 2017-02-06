package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum CardiacPhase {
    SYSTOLIC("Systolic"), DIASTOLIC("Diastolic"), CONTINUOUS("Continuous");

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
