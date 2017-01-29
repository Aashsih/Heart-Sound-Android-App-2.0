package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum CHARACTER {
    HIGH_PITCHED("High Pitched"), LOW_PITCHED("Low Pitched"), RUMBLING("Rumbling");

    private String value;
    private static Map<String, CHARACTER> valueToCharacter = new HashMap<String, CHARACTER>();

    static {
        for(CHARACTER accessModifier : CHARACTER.values()){
            valueToCharacter.put(accessModifier.value,accessModifier);
        }
    }

    private CHARACTER(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

    public static CHARACTER getCharacter(String value){
        return valueToCharacter.get(value);
    }
}
