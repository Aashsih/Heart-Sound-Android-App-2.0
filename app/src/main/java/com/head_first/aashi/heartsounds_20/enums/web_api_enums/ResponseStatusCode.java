package com.head_first.aashi.heartsounds_20.enums.web_api_enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public enum ResponseStatusCode {
    OK(200), CREATED(201), BAD_REQUEST(400), UNAUTHORIZED(401), INTERNAL_SERVER_ERROR(500), INVALID_STATUS_CODE(0), UNKNOWN_STATUS_CODE(1);
    private static Map<Integer, ResponseStatusCode> valueToResponseStatusCode = new HashMap<>();

    static {
        for(ResponseStatusCode responseStatusCode : ResponseStatusCode.values()){
            valueToResponseStatusCode.put(responseStatusCode.value,responseStatusCode);
        }
    }

    public static ResponseStatusCode getResponseStatusCode(Integer value){
        ResponseStatusCode responseStatusCode = valueToResponseStatusCode.get(value);
        if(responseStatusCode == null){
            return ResponseStatusCode.UNKNOWN_STATUS_CODE;
        }
        return responseStatusCode;
    }

    private Integer value;

    private ResponseStatusCode(Integer value){
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value.toString();
    }
}
