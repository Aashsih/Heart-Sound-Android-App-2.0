package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";

    private View mRootView;
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        ImageView loginScreenGIFView = (ImageView) mRootView.findViewById(R.id.loadingScreen);
        Glide.with(getContext()).load(R.drawable.loading_screen_1).asGif().crossFade().into(loginScreenGIFView);
        mUsername = (EditText) mRootView.findViewById(R.id.username);
        mPassword = (EditText) mRootView.findViewById(R.id.password);
        mLoginButton = (Button) mRootView.findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAccessToken();
            }
        });
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void storeUserDetailsInSharedPreferences(String response){
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(response, type);
        SharedPreferencesManager.addUsernameAndTokenToSharedPreferences(getActivity(),
                responseMap.get(WebAPI.USER_NAME),
                responseMap.get(WebAPI.ACCESS_TOKEN));
    }

    private void getAccessToken(){
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, WebAPI.GET_USER_TOKEN_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
                        storeUserDetailsInSharedPreferences(response);
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                        //dismiss the progress dialog box
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        //Launch the UserPatientsActivity here
                        Intent heartSoundMainActivityIntent = new Intent(getContext(), UserPatientActivity.class);
                        startActivity(heartSoundMainActivityIntent);

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

                //dismiss the progress dialog box
                DialogBoxDisplayHandler.dismissProgressDialog();
                //use the webAPIResponse to get message from server
                DialogBoxDisplayHandler.displayInformationAlertDialogBox(getActivity(),
                        getResources().getString(R.string.errorTitle),
                        webAPIResponse.getMessage());
                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareAccessTokenRequestHeader();
            }
            @Override
            public String getBodyContentType() {
                return WebAPI.CONTENT_TYPE_URLENCODED_VALUE;
            }
            @Override
            protected Map<String, String> getParams() {
                return WebAPI.addAccessTokenRequestParams(mUsername.getText().toString(), mPassword.getText().toString());//this fragment will have a HeartSound object which will then be passed along with the PatientId
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                webAPIResponse.setStatusCode(ResponseStatusCode.getResponseStatusCode(response.statusCode));
                return super.parseNetworkResponse(response);
            }
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    webAPIResponse.setStatusCode(ResponseStatusCode.getResponseStatusCode(volleyError.networkResponse.statusCode));
                    String errorMessage = new String(volleyError.networkResponse.data);
                    VolleyError error = new VolleyError(errorMessage);
                    volleyError = error;
                    webAPIResponse.setMessage(errorMessage);
                    return volleyError;
                }
                webAPIResponse.setStatusCode(ResponseStatusCode.INTERNAL_SERVER_ERROR);
                webAPIResponse.setMessage("");
                return volleyError;
            }
        };
        RequestQueueSingleton.getInstance(getContext()).addToRequestQueue(request);
    }
}
