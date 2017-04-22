package com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum LeftLateralPosition {
    NOT_DONE("Not Done"), LOUDER("Louder"), SOFTER("Softer"), NO_CHANGE("No Change");

    private static Map<String, LeftLateralPosition> valueToLeftLateralPosition = new HashMap<String, LeftLateralPosition>();

    static {
        for(LeftLateralPosition leftLateralPosition : LeftLateralPosition.values()){
            valueToLeftLateralPosition.put(leftLateralPosition.value,leftLateralPosition);
        }
    }

    public static LeftLateralPosition getLeftLateralPosition(String value){
        return valueToLeftLateralPosition.get(value);
    }

    private String value;

    private LeftLateralPosition(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
