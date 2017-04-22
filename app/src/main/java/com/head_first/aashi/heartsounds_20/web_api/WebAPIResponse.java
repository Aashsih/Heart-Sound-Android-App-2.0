package com.head_first.aashi.heartsounds_20.web_api;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.WebAPIErrorFragment;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;

/**
 * Created by Aashish Indorewala on 18-Mar-17.
 */

public class WebAPIResponse {

    private ResponseStatusCode statusCode;
    private String message;

    public WebAPIResponse() {
        this.statusCode = ResponseStatusCode.INVALID_STATUS_CODE;
        this.message = null;
    }

    public WebAPIResponse(ResponseStatusCode statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResponseStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(ResponseStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if(message == null || message.isEmpty()){
            this.message = "No Internet Connection. Please try again";
        }
        else{
            String[] parsedMessage = message.split("error_description\":");
            this.message = (parsedMessage.length > 1)? (parsedMessage[parsedMessage.length - 1].replaceAll("[\"}]","")) : parsedMessage[0];
        }
    }
}
