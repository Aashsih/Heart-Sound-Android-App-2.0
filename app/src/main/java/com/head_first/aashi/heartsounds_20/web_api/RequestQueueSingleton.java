package com.head_first.aashi.heartsounds_20.web_api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aashish Indorewala on 17-Mar-17.
 */

public class RequestQueueSingleton {
    private static final int REQUEST_TIMEOUT_MS = 10000;
    private static RequestQueueSingleton singletonInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueSingleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestQueueSingleton getInstance(Context context){
        if(singletonInstance == null){
            singletonInstance = new RequestQueueSingleton(context);
        }
        return singletonInstance;
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
