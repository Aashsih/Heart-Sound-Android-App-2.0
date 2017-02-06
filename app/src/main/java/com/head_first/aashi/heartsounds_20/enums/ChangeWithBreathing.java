package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum ChangeWithBreathing {
    NOT_DONE("Not Done"), SOFTER_INSPIRATION("Softer Inspiration"), LOUDER_INSPIRATION("Louder Inspiration"),
    SOFTER_EXPIRATION("Softer Expiration"), LOUDER_EXPIRATION("Louder Expiration"), NO_CHANGE("No Change");

    private static Map<String, ChangeWithBreathing> valueToChangeWithBreathing = new HashMap<String, ChangeWithBreathing>();

    static {
        for(ChangeWithBreathing changeWithBreathing : ChangeWithBreathing.values()){
            valueToChangeWithBreathing.put(changeWithBreathing.value,changeWithBreathing);
        }
    }

    public static ChangeWithBreathing getChangeWithBreathing(String value){
        return valueToChangeWithBreathing.get(value);
    }

    private String value;

    private ChangeWithBreathing(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }
}
