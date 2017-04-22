package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum SittingForward {
    NOT_DONE("Not done"), LOUDER("Louder"), SOFTER("Softer"), NO_CHANGE("No Change");

    private static Map<String, SittingForward> valueToSittingForward = new HashMap<String, SittingForward>();

    static {
        for(SittingForward sittingForward : SittingForward.values()){
            valueToSittingForward.put(sittingForward.value,sittingForward);
        }
    }

    public static SittingForward getSittingForward(String value){
        return valueToSittingForward.get(value);
    }

    private String value;

    private SittingForward(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
