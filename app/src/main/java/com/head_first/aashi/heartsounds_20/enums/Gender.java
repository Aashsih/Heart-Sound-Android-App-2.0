package com.head_first.aashi.heartsounds_20.enums;

import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CardiacPhase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 05-Nov-16.
 */

public enum Gender {
    MALE("M"), FEMALE("F"), OTHER("o"); // confirm

    private static Map<String, Gender> valueToGender = new HashMap<String, Gender>();

    static {
        for(Gender gender : Gender.values()){
            valueToGender.put(gender.value,gender);
        }
    }

    public static Gender getGender(String value){
        if(value == null){
            return Gender.OTHER;
        }
        Gender gender = valueToGender.get(value);
        if(gender == null){
            switch (value){
                case "Male": gender = Gender.MALE; break;
                case "Female": gender = Gender.FEMALE; break;
                case "Other": gender = Gender.OTHER; break;
                default: gender = Gender.OTHER; break;
            }
        }
        return gender;
    }

    private String value;

    private Gender(String value){
        this.value = value;
    }

    @Override
    public String toString(){
        switch (this.value){
            case "M": return "Male";
            case "F": return "Female";
            case "o": return "Other";
            default: return "(not set)";
        }
    }
}
