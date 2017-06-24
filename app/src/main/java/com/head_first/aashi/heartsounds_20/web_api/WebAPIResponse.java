package com.head_first.aashi.heartsounds_20.web_api;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.WebAPIErrorFragment;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;

/**
 * This class stores some information returned
 * by the server as a respons
 *
 * An object of this class will store:
 * 1. Response Status Code
 * 2. Server Response Message
 * returned after a REST API call.
 */
public class WebAPIResponse {

    private ResponseStatusCode statusCode;
    private String message;

    /**
     * Default Constructor that initializes:
     * 1. statusCode to valid
     * 2. message to null
     */
    public WebAPIResponse() {
        this.statusCode = ResponseStatusCode.INVALID_STATUS_CODE;
        this.message = null;
    }

    /**
     * This constructor is used to initialize the fields to
     * the values passed in the parameters
     *
     * @param statusCode Web API response status code
     * @param message Web API server response
     */
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

    /**
     * This method is used to set the message returned by the
     * server.
     * If the message passed in the parameter is empty, then
     * a default message is stored in it.
     * @param message
     */
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
