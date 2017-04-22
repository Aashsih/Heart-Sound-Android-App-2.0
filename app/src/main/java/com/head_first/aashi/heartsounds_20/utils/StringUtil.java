package com.head_first.aashi.heartsounds_20.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aashish Indorewala on 11-Jan-17.
 */

public class StringUtil {

    public static String[] split(String string, Character regex){
        int countOfRegexOccurence = string.length() - string.replace(regex.toString(), "").length();
        String[] array = new String[countOfRegexOccurence + 1];
        int j = 0;
        array[0] = "";
        for(int i = 0; i < string.length() && j <= countOfRegexOccurence; i++){
            if(string.charAt(i) == regex){
                array[++j] = "";
            }
            else{
                array[j] += string.charAt(i);
            }
        }
        return array;
    }

    public static <T> List<String> convertListToListOfString(List<T> collection){
        List<String> stringCollection = new ArrayList<>();
        if(collection == null){
            return stringCollection;
        }
        for(Object anObject : collection){
            stringCollection.add(anObject.toString());
        }
        return stringCollection;
    }
}
