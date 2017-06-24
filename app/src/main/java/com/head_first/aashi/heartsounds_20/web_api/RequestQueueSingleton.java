package com.head_first.aashi.heartsounds_20.web_api;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class uses the Singleton Design pattern and
 * its instance provides functionality to add an API
 * request to a RequestQueue
 */
public class RequestQueueSingleton {
    private static final int REQUEST_TIMEOUT_MS = 10000;
    private static RequestQueueSingleton singletonInstance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * This constructor initializes the RequestQueue
     * @param context of the Activity/Application that is
     *                using the RequestQueue
     */
    private RequestQueueSingleton(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * This method is used to get an instance
     * of the RequestQueue.
     *
     * @return requestQueue
     */
    public RequestQueue getRequestQueue(){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * This method is used to get the singleton instance
     * of this class.
     *
     * @param context of the Activity/Application that is
     *                using the RequestQueue
     * @return singleton instance of this class
     */
    public static synchronized RequestQueueSingleton getInstance(Context context){
        if(singletonInstance == null){
            singletonInstance = new RequestQueueSingleton(context);
        }
        return singletonInstance;
    }

    /**
     * This method is used to add an API request to the
     * existing request Queue
     * @param request an API request
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> request){
        request.setRetryPolicy(new DefaultRetryPolicy(
                REQUEST_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
