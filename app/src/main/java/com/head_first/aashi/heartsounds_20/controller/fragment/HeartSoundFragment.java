package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces.HeartSoundAPI;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeartSoundFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeartSoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartSoundFragment extends Fragment implements HeartSoundAPI {
    /**
     * This fragment will display the following:
     * 1. HeartSoundId (if not already displayed in the navigation view)
     * 2. HeartSound Data
     * 3. Voice Comment Data
     * 4. Created by (doctor Id)
     * For heartsound data and voice comment, there will be two buttons:
     * 1. Play
     * 2. Record
     *
     * If a user that has not created this data is viewing this fragment, they should only be able to playback the audio
     * and not be able to edit anything
     * meaning the record button will need to be hidden for this user.
     *
     * Different Scenarios and app's behaviour:
     * 1. User wants to record voice comment from phone
     * - When the user clicks on the record button for the voice comment, the user will be using the phone's microphone to do a recording.
     *
     * 2. User wants to playback a recorded voice comment/HeartSound
     * - When the user clicks on the playback button for the recording, the user will be using the phone's MediaPlayer to playback the recording
     *
     * 3. User wishes to upload/download data from the stethoscope
     * - A new fragment will be created that will provide an interface for the user to communicate with the stethoscope
     *
     * Whenever a voice comment or heart sound is uploaded to the DB, make sure you update the Stethoscope id in the heartSound object/fragment
     *
     */

    public static final String HEART_SOUND_FRAGMENT_TAG = "HEART_SOUND_FRAGMENT";
    public static final String HEART_SOUND_ID_TAG = "HEART_SOUND_ID_TAG";

    //Layout and View
    private Menu mActionBarMenu;
    private View mRootView;
    private ImageButton mPlayVoiceCommentButton;
    private ImageButton mRecordNewVoiceCommentButton;
    private ImageButton mPlayHeartSoundButton;
    private ImageButton mStethoscopeVoiceCommentInteractionButton;
    private ImageButton mStethoscopeHeartSoundInteractionButton;
    private TextView mHeartSoundId;
    private TextView mDoctorDetails;
    private TextView mDeviceId;

    //Data
    private HeartSound heartSound;

    //Web API
    private WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public HeartSoundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment HeartSoundFragment.
     */
    public static HeartSoundFragment newInstance() {
        HeartSoundFragment fragment = new HeartSoundFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_heart_sound, container, false);
        //Buttons
        mRecordNewVoiceCommentButton = (ImageButton) mRootView.findViewById(R.id.voiceCommentNewRecordingButton);
        mPlayVoiceCommentButton = (ImageButton) mRootView.findViewById(R.id.voiceCommentPlayRecordingButton);
        mPlayHeartSoundButton = (ImageButton) mRootView.findViewById(R.id.heartSoundPlayRecordingButton);
        mStethoscopeVoiceCommentInteractionButton = (ImageButton) mRootView.findViewById(R.id.stethoscopeVoiceCommentInteractionButton);
        mStethoscopeHeartSoundInteractionButton = (ImageButton) mRootView.findViewById(R.id.stethoscopeHeartSoundInteractionButton);
        //TextViews
        mHeartSoundId = (TextView) mRootView.findViewById(R.id.heartSoundId);
        mDoctorDetails = (TextView) mRootView.findViewById(R.id.doctorDetails);
        mDeviceId = (TextView) mRootView.findViewById(R.id.deviceId);
        //If HeartSound object is not null copy data from the HeartSound object into the views

        setupListenersForButtons();
        return mRootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        if(getArguments() != null){
            long heartSoundId = getArguments().getLong(HEART_SOUND_ID_TAG);
            requestHeartSound((int)heartSoundId);
        }
        ((PatientHeartSoundActivity)getActivity()).setupNavigationDrawerContent();
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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_patient_heart_sound_tool_bar_items, menu);
        mActionBarMenu =  menu;
        //if the user is the creator of this HeartSound then display the edit menuitem
        showActionBarMenuItems();
    }

    protected void showActionBarMenuItems(){
        for(int i = 0; i < mActionBarMenu.size(); i++){
            MenuItem menuItem = mActionBarMenu.getItem(i);
            if(!(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.saveChangesItemText)) ||
                    menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.editItemText)) ||
                    menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.cancelChangesItemText)) ||
                    menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.sharePatientItemText)))){
                //if() user id matches the CreatedBy user id then display the edit button
                //mActionBarMenu.findItem(R.id.editItem).setVisible(false);
                menuItem.setVisible(true);
            }
            else{
                menuItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.deleteItem:
                break;
            case R.id.refreshViewItem:
                break;
        }
        return true;
    }

    private void setupListenersForButtons(){
        mRecordNewVoiceCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAudioRecordingFragment(true, true);
            }
        });
        mPlayVoiceCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAudioRecordingFragment(false, true);
            }
        });
        mPlayHeartSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAudioRecordingFragment(false, false);
            }
        });
        mStethoscopeVoiceCommentInteractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStethoscopeInteractionFragment(true);
            }
        });
        mStethoscopeHeartSoundInteractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStethoscopeInteractionFragment(false);
            }
        });
    }

    private void launchAudioRecordingFragment(boolean recordMode, boolean voiceCommentMode){
        //Pass the state of the fragment eg: play or record
        AudioRecordingFragment audioRecordingFragment = AudioRecordingFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean(AudioRecordingFragment.IS_REORD_MODE_TAG, recordMode);
        bundle.putBoolean(AudioRecordingFragment.IS_VOICE_COMMENT_MODE_TAG, voiceCommentMode);
        audioRecordingFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, audioRecordingFragment, AudioRecordingFragment.AUDIO_RECORDING_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    private void launchStethoscopeInteractionFragment(boolean voiceCommentMode){
        StethoscopeInteractionFragment stethoscopeInteractionFragment = StethoscopeInteractionFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putBoolean(StethoscopeInteractionFragment.IS_VOICE_COMMENT_MODE_TAG, voiceCommentMode);
        stethoscopeInteractionFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, stethoscopeInteractionFragment, StethoscopeInteractionFragment.STETHOSCOPE_INTERACTION_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    //-------------------------------------------------------------
    //Implementation for HearSound API
    @Override
    public void requestPatientHeartSounds(int patientId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void requestHeartSound(final int heartSoundId) {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WebAPI.HEART_SOUND_BASE_URL + heartSoundId, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        heartSound = JsonObjectParser.getHeartSoundFromJsonString(response.toString());
                        ((PatientHeartSoundActivity)getActivity()).setHeartSounObject(heartSound);
                        mHeartSoundId.setText(heartSound.getHeartSoundID() + "");
                        mDeviceId.setText(heartSound.getDeviceID());
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                        //dismiss the progress dialog box
                        DialogBoxDisplayHandler.dismissProgressDialog();

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly
                DialogBoxDisplayHandler.dismissProgressDialog();
                //use the webAPIResponse to get message from server
                if(webAPIResponse.getStatusCode().equals(ResponseStatusCode.UNAUTHORIZED)){
                    SharedPreferencesManager.invalidateUserAccessToken(getActivity());
                }
                else{
                    //Launch Web API Error Fragment
                    ((PatientHeartSoundActivity)getActivity()).launchWebAPIErrorFragment();
                }
                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareAccessTokenHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
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

    @Override
    public void createHeartSound(HeartSound heartSound) {
        Toast.makeText(getContext(), getResources().getString(R.string.creatingHeartSound), Toast.LENGTH_SHORT).show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, WebAPI.HEART_SOUND_BASE_URL, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        //update UI accordingly

                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

                //use the webAPIResponse to get message from server

                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareJsonRequestHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Map<String, String> getParams() {
                return WebAPI.addCreateHeartSoundParams(null, 0);//this fragment will have a HeartSound object which will then be passed along with the PatientId
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
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

    @Override
    public void updateHeartSound(HeartSound heartSound) {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.updatingHeartSound));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebAPI.HEART_SOUND_BASE_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly

                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                        //dismiss the progress dialog box
                        DialogBoxDisplayHandler.dismissProgressDialog();

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

                //use the webAPIResponse to get message from server

                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();
                //dismiss the progress dialog box
                DialogBoxDisplayHandler.dismissProgressDialog();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareJsonRequestHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Map<String, String> getParams() {
                return WebAPI.addUpdateHeartSoundParams(null);//this fragment will have a HeartSound object which will then be passed
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
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

    @Override
    public void deleteHeartSound(int heartSoundId) {
        Toast.makeText(getContext(), getResources().getString(R.string.deletingHeartSound), Toast.LENGTH_SHORT).show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.DELETE, WebAPI.HEART_SOUND_BASE_URL + heartSoundId, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        //update UI accordingly

                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

                //use the webAPIResponse to get message from server

                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareAccessTokenHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
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
    //-------------------------------------------------------------
}
