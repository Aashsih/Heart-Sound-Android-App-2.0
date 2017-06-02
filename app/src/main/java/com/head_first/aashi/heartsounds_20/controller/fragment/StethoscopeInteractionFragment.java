package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.util_interfaces.NavgigationDrawerUtils;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.head_first.aashi.heartsounds_20.utils.BluetoothManager;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.utils.StethoscopeInteraction;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;
import com.mmm.healthcare.scope.AudioType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StethoscopeInteractionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StethoscopeInteractionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StethoscopeInteractionFragment extends Fragment{
    public static final String STETHOSCOPE_INTERACTION_FRAGMENT_TAG = "STETHOSCOPE_INTERACTION_FRAGMENT_TAG";
    private static final String TRACK_NAME = "Track";
    public static final String IS_VOICE_COMMENT_MODE_TAG = "IS_VOICE_COMMENT_MODE_TAG";
    public static final String HEART_SOUND_TAG = "HEART_SOUND_TAG";

    //Layouts and views
    private View mRootView;
    private Button mPlayFromStethoscope;
    private Button mDownloadFromStethoscope;
    private Button mUploadToStethoscope;
    private Button mConnectToStethoscope;
    private RadioGroup mTracksAvailableInStethoscope;
    ProgressDialog progressDialog;

    //Data
    private StethoscopeInteraction stethoscopeInteractor;
    private String selectedTrack;
    private boolean voiceCommentMode;
    private Integer clickedButtonId;
    private String progressDialogMessage;
    //private boolean bluetoothOn;

    //Web API
    private WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public StethoscopeInteractionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StethoscopeInteractionFragment.
     */
    public static StethoscopeInteractionFragment newInstance() {
        StethoscopeInteractionFragment fragment = new StethoscopeInteractionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stethoscopeInteractor = new StethoscopeInteraction(this);
        Bundle bundle = getArguments();
        if(bundle != null){
            voiceCommentMode = (boolean) bundle.getBoolean(StethoscopeInteractionFragment.IS_VOICE_COMMENT_MODE_TAG);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_stethoscope_interaction, container, false);
        mPlayFromStethoscope = (Button) mRootView.findViewById(R.id.playFromStethoscope);
        mDownloadFromStethoscope = (Button) mRootView.findViewById(R.id.downloadFromStethoscope);
        mUploadToStethoscope= (Button) mRootView.findViewById(R.id.uploadToStethoscope);
        mConnectToStethoscope = (Button) mRootView.findViewById(R.id.connectToStethoscope);
        setupListenersForButtons();
        startThreadToConnectToBluetooth();
        if(!stethoscopeInteractor.isStethoscopeConnected()){
            disableStethoscopeInteractionButtons();
        }
        else{
            enableStethoscopeInteractionButtons();
        }
        ((NavgigationDrawerUtils)getActivity()).disableNavigationMenu();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        ((NavgigationDrawerUtils)getActivity()).enableNavigationMenu();
        super.onDetach();
        stethoscopeInteractor.disconnectFromStethoscope();
        mListener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //This method is currently not being used
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //this.bluetoothOn = true;
        }
        else {
            //this.bluetoothOn = false;
        }

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

    private void disableStethoscopeInteractionButtons(){
        if(!stethoscopeInteractor.isStethoscopeConnected()){
            mPlayFromStethoscope.setClickable(false);
            mPlayFromStethoscope.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mDownloadFromStethoscope.setClickable(false);
            mDownloadFromStethoscope.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mUploadToStethoscope.setClickable(false);
            mUploadToStethoscope.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

    }

    private void enableStethoscopeInteractionButtons(){
        if(stethoscopeInteractor.isStethoscopeConnected()){
            mPlayFromStethoscope.setClickable(true);
            mPlayFromStethoscope.getBackground().setColorFilter(null);
            mDownloadFromStethoscope.setClickable(true);
            mDownloadFromStethoscope.getBackground().setColorFilter(null);
            mUploadToStethoscope.setClickable(true);
            mUploadToStethoscope.getBackground().setColorFilter(null);
        }
    }

    private void setupListenersForButtons(){
        mPlayFromStethoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playDataFromDeviceOnStethoscope();
            }
        });
        mDownloadFromStethoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStethoscopeInteractionButtonClick(v);
            }
        });
        mUploadToStethoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStethoscopeInteractionButtonClick(v);
            }
        });
        mConnectToStethoscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToStethoscope();
            }
        });
    }

    /**
     * Entry point for upload and download event in this Fragment
     */
    public void onStethoscopeInteractionButtonClick(View button){
        //bluetoothOn = BluetoothManager.turnBluetoothOn(getActivity());
        if(BluetoothManager.isBluetoothEnabled()){
            //show a dialog box to the user to select the track they want to upload or download.
            displayAvailableTracksDialog(button);
            clickedButtonId = button.getId();
        }
    }

    /**
     * This method is called when the user selects a valid track from the "Available Tracks" Dialog box
     * and executes the method based on the button user clicked.
     */
    private void interactWithStethoscope(){
        switch(clickedButtonId){
            case R.id.downloadFromStethoscope:
                downloadTrackFromStethoscope();
                break;
            case R.id.uploadToStethoscope:
                uploadTrackToStethoscope();
                break;
            default:
                Toast.makeText(getContext(), "The button id is not correct", Toast.LENGTH_SHORT);

        }
    }

    /**
     * Displays a dialog box to the user to select a particular Track to download/upload from/to the stethoscope
     * @param view
     */
    private void displayAvailableTracksDialog(View view){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View tracksSelectorDialog = (View)inflater.inflate(R.layout.dialog_radio_group, null);
        mTracksAvailableInStethoscope = (RadioGroup) tracksSelectorDialog.findViewById(R.id.radioGroup);
        setupRadioButtonsForAvailableTracks();
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.trackSelectorDialogTitle)
                .setCancelable(true)
                .setView(tracksSelectorDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RadioButton selectedRadioButton = (RadioButton) mTracksAvailableInStethoscope.findViewById(mTracksAvailableInStethoscope.getCheckedRadioButtonId());
                        if(selectedRadioButton == null){
                            selectedTrack = null;
                        }
                        else{
                            selectedTrack = selectedRadioButton.getText().toString();
                            interactWithStethoscope();
                        }
                    }
                })
                .setNegativeButton(R.string.negativeButtonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedTrack = null;
                    }
                })
                .create()
                .show();

    }

    private void setupRadioButtonsForAvailableTracks(){
        List<String> availableTrackNames = stethoscopeInteractor.getAllAvailableTrackNames();
        if(mTracksAvailableInStethoscope != null){
            for(int i = 0; i < availableTrackNames.size(); i++){
            RadioButton radioButton = new RadioButton(getContext());
            if(selectedTrack != null && selectedTrack.toLowerCase().contains(availableTrackNames.get(i).toLowerCase())){
                radioButton.setChecked(true);
            }
            radioButton.setText(availableTrackNames.get(i));
            radioButton.setId(i);
            mTracksAvailableInStethoscope.addView(radioButton);
            }
        }
    }

    private void connectToStethoscope(){
        //bluetoothOn = BluetoothManager.turnBluetoothOn(getActivity()); -- This functionality is now moved to a Thread
        DialogBoxDisplayHandler.showIndefiniteProgressDialog( getActivity(), getResources().getString(R.string.connectingToStethoscopeMessage));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stethoscopeInteractor.connectToAvailableStethoscope(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DialogBoxDisplayHandler.dismissProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(stethoscopeInteractor.isStethoscopeConnected() && BluetoothManager.isBluetoothEnabled()){
                            Toast.makeText(getContext(), "Connected to " + stethoscopeInteractor.getStethoscopeSerialNumber(), Toast.LENGTH_SHORT).show();
                            enableStethoscopeInteractionButtons();
                        }
                        else{
                            Toast.makeText(getContext(), "No paired stethoscope found", Toast.LENGTH_SHORT).show();
                            disableStethoscopeInteractionButtons();
                        }
                    }
                });

            }
        }).start();
    }

    private void playDataFromDeviceOnStethoscope(){
        HeartSound heartSound = ((PatientHeartSoundActivity)getActivity()).getHeartSoundObject();
        boolean interactionStarted = false;
        if(heartSound == null){
            Toast.makeText(getContext(), R.string.noDataToPlay, Toast.LENGTH_SHORT).show();
        }
        else {
            if((voiceCommentMode && heartSound.getVoiceCommentData() != null)){
                interactionStarted = stethoscopeInteractor.playDataFromDeviceOnStethoscope(AudioType.VoiceComment);

            }
            else if(heartSound.getHeartSoundData() != null){
                interactionStarted = stethoscopeInteractor.playDataFromDeviceOnStethoscope(AudioType.Body);
            }
            else{
                Toast.makeText(getContext(), R.string.noDataToPlay, Toast.LENGTH_SHORT).show();
            }
            if(interactionStarted){
                DialogBoxDisplayHandler.showIndefiniteProgressDialog( getActivity(), getResources().getString(R.string.playingDataToStethoscopeMessage) + selectedTrack);
            }
        }
    }

    private void downloadTrackFromStethoscope(){
        boolean interactionStarted = false;
        if(selectedTrack != null){
            if(voiceCommentMode){
                interactionStarted = stethoscopeInteractor.downloadTrackFromStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.VoiceComment);
            }
            else{

                interactionStarted = stethoscopeInteractor.downloadTrackFromStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.Body);
            }
            if(interactionStarted){
                DialogBoxDisplayHandler.showIndefiniteProgressDialog( getActivity(), getResources().getString(R.string.downloadingFromStethoscopeMessage) + selectedTrack);
            }
            else{
                Toast.makeText(getContext(), R.string.emptyAudioTrack, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadTrackToStethoscope(){
        HeartSound heartSound = ((PatientHeartSoundActivity)getActivity()).getHeartSoundObject();
        boolean interactionStarted = false;
        if(heartSound == null){
            Toast.makeText(getContext(), R.string.noDataToPlay, Toast.LENGTH_SHORT).show();
        }
        else {
            if(selectedTrack != null) {
                if((voiceCommentMode && heartSound.getVoiceCommentData() != null)){
                    interactionStarted = stethoscopeInteractor.uploadTrackToStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.VoiceComment);
                }
                else if(heartSound.getHeartSoundData() != null){
                    interactionStarted = stethoscopeInteractor.uploadTrackToStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.Body);
                }
                else {
                    Toast.makeText(getContext(), R.string.noDataToPlay, Toast.LENGTH_SHORT).show();
                }
                if(interactionStarted){
                    DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.uploadingToStethoscopeMessage) + selectedTrack);
                }
            }
        }
    }

    public void finishStethoscopeInteraction(){
        if(!stethoscopeInteractor.isStethoscopeInteractionInProgress()){
            HeartSound heartSound = ((PatientHeartSoundActivity)getActivity()).getHeartSoundObject();
            if(heartSound != null){
                try{
                    if(heartSound.getHeartSoundID() == null){
                        createHeartSound(heartSound);
                    }
                    else if(heartSound.hasHeartSoundChanged()){
                        updateHeartSound(heartSound);
                    }
                    else if(heartSound.hasVoiceCommentChanged()){
                        updateVoiceComment(heartSound);
                    }
                    else{
                        DialogBoxDisplayHandler.dismissProgressDialog();
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                DialogBoxDisplayHandler.dismissProgressDialog();
            }
        }
        //Also change the stethoscope id of the heartsound
        //Note, the stethoscope id should change only if a the heart sound that was recorded was from another stethoscope
        //The above doesnt apply for Voice Comment
    }

    private void startThreadToConnectToBluetooth(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothManager.turnBluetoothOn(getActivity());
            }
        }).start();
    }

    public void createHeartSound(HeartSound heartSound) throws JSONException {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JSONObject bodyParams = WebAPI.addCreateHeartSoundParams(heartSound, (int)(((PatientHeartSoundActivity)getActivity()).getPatient().getPatientId()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.HEART_SOUND_BASE_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        Log.v("MEssage","Created heart sound while ruuning the program");
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly
                Log.v("MEssage","Did not Created heart sound while ruuning the program");
                //dismiss the progress dialog box
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
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareJsonRequestHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
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

    public void updateHeartSound(HeartSound heartSound) throws JSONException {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.updatingHeartSound));
        JSONObject bodyParams = WebAPI.addUpdateHeartSoundParams(heartSound);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebAPI.HEART_SOUND_BASE_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        ((PatientHeartSoundActivity)getActivity()).getHeartSoundObject().setHeartSoundChanged(false);
                        Log.v("Message", "This happened");
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
                Log.v("Error",webAPIResponse.getStatusCode() + " : " + webAPIResponse.getMessage());
                //dismiss the progress dialog box
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
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareJsonRequestHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);
                    webAPIResponse.setStatusCode(ResponseStatusCode.getResponseStatusCode(response.statusCode));
                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
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

    public void updateVoiceComment(HeartSound heartSound) throws JSONException {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.updatingHeartSound));
        JSONObject bodyParams = WebAPI.addUpdateVoiceCommentParams(heartSound);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebAPI.HEART_SOUND_BASE_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        ((PatientHeartSoundActivity)getActivity()).getHeartSoundObject().setVoiceCommentChanged(false);
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

                //dismiss the progress dialog box
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
                webAPIResponse = new WebAPIResponse();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return WebAPI.prepareJsonRequestHeader(SharedPreferencesManager.getUserAccessToken(getActivity()));
            }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                    JSONObject result = null;

                    if (jsonString != null && jsonString.length() > 0)
                        result = new JSONObject(jsonString);
                    webAPIResponse.setStatusCode(ResponseStatusCode.getResponseStatusCode(response.statusCode));
                    return Response.success(result,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
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
