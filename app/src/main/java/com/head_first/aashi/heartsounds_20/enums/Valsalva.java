package com.head_first.aashi.heartsounds_20.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Valsalva {
    NOT_DONE("Not Done"), SOFTER_INSPIRATION("Softer Inspiration"), LOUDER_INSPIRATION("Louder Inspiration");

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
