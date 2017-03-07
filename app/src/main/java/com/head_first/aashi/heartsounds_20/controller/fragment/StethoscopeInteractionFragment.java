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

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.utils.BluetoothManager;
import com.head_first.aashi.heartsounds_20.utils.StethoscopeInteraction;
import com.mmm.healthcare.scope.AudioType;

import java.util.List;
import java.util.concurrent.RunnableFuture;

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

    //Layouts and views
    private View mRootView;
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
    private boolean bluetoothOn;

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
    // TODO: Rename and change types and number of parameters
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
        mDownloadFromStethoscope = (Button) mRootView.findViewById(R.id.downloadFromStethoscope);
        mUploadToStethoscope= (Button) mRootView.findViewById(R.id.uploadToStethoscope);
        mConnectToStethoscope = (Button) mRootView.findViewById(R.id.connectToStethoscope);
        setupListenersForButtons();
        if(!stethoscopeInteractor.isStethoscopeConnected()){
            disableStethoscopeInteractionButtons();
        }
        else{
            enableStethoscopeInteractionButtons();
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.bluetoothOn = true;
        }
        else {
            this.bluetoothOn = false;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void disableStethoscopeInteractionButtons(){
        if(!stethoscopeInteractor.isStethoscopeConnected()){
            mDownloadFromStethoscope.setClickable(false);
            mDownloadFromStethoscope.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            mUploadToStethoscope.setClickable(false);
            mUploadToStethoscope.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }

    }

    private void enableStethoscopeInteractionButtons(){
        if(stethoscopeInteractor.isStethoscopeConnected()){
            mDownloadFromStethoscope.setClickable(true);
            mDownloadFromStethoscope.getBackground().setColorFilter(null);
            mUploadToStethoscope.setClickable(true);
            mUploadToStethoscope.getBackground().setColorFilter(null);
        }
    }

    private void setupListenersForButtons(){
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
     * Entry point for any button click event in this Fragment
     */
    public void onStethoscopeInteractionButtonClick(View button){
        bluetoothOn = BluetoothManager.turnBluetoothOn(getActivity());
        if(bluetoothOn){
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
                .setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
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
                .setNegativeButton(R.string.negativeButton, new DialogInterface.OnClickListener() {
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
        boolean connected = stethoscopeInteractor.connectToAvailableStethoscope(getContext());
        bluetoothOn = BluetoothManager.turnBluetoothOn(getActivity());
        if(connected && bluetoothOn){
            enableStethoscopeInteractionButtons();
        }
        else{
            disableStethoscopeInteractionButtons();
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
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Downloading " + selectedTrack);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }

        }

    }

    private void uploadTrackToStethoscope(){
        boolean interactionStarted = false;
        if(selectedTrack != null){
            if(voiceCommentMode){
                interactionStarted = stethoscopeInteractor.uploadTrackFromStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.VoiceComment);
            }
            else{
                interactionStarted = stethoscopeInteractor.uploadTrackFromStethoscope(stethoscopeInteractor.getTrackId(selectedTrack), AudioType.Body);
            }
            if(interactionStarted){
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Uploading " + selectedTrack);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
        }
    }

    public void finishStethoscopeInteraction(){
        if(!stethoscopeInteractor.isStethoscopeInteractionInProgress()){
            progressDialog.dismiss();
        }
        //Also change the stethoscope id of the heartsound
        //Note, the stethoscope id should change only if a the heart sound that was recorded was from another stethoscope
        //The above doesnt apply for Voice Comment
    }

}
