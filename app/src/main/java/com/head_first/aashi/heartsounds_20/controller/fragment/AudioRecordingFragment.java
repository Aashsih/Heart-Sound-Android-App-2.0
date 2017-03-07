package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.utils.AudioRecorder;
import com.head_first.aashi.heartsounds_20.utils.AudioRecordingButtonState;
import com.head_first.aashi.heartsounds_20.utils.HeartSoundRecorder;
import com.head_first.aashi.heartsounds_20.utils.RequestPermission;
import com.head_first.aashi.heartsounds_20.utils.VoiceRecorder;
import com.mmm.healthcare.scope.ConfigurationFactory;
import com.mmm.healthcare.scope.IBluetoothManager;
import com.mmm.healthcare.scope.Stethoscope;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.Manifest;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioRecordingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioRecordingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioRecordingFragment extends Fragment implements AudioRecordingButtonState{
    /**
     * if this fragment is exited while recording or playing the mediaPlayer/Recorder needs to be closed
     * Ensure that the stop method is called that releases those resources on the cancelChanges()
     * This will also ensure that these resources are released when the user presses back.
     *
     * The other scenario that needs to be considered is when the user presses the home button.
     * In this case whenever the onPause() or onStop() is called release the resources.
     *
     *
     * There are two modes in which this fragment can be launched:
     * 1. Record New Audio:
     * a) Set the centre button to the record image and grey out the "stop" and "replay" button
     * b) Once the "record" button is pressed, it should be greyed along with the "replay" button and the "stop" button should be made available
     * c) When the stop button is pressed, it should be greyed out and the "replay" and "record" button need to be available.
     * d) if "record" button is pressed then do the same (a)
     * e) if the "replay" button is pressed, it should be greyed out along with the "record" button and the "stop" button should be made available.
     *
     * [Note: in the record mode, the Visualizer will be replaced with a Text saying "Press record" (something similar) and then when the
     * user pressed record the text will display "recording". When the replay button is pressed this Text will be replaced with the visualizer
     * view and vice versa.]
     *
     * 2. Play Stored audio: (the replay button is not available in this mode)
     * a) Set the centre button to the play image, the left button to "pause" image and grey out the "stop" and "pause" button
     * b) Once the "play" button is pressed, it should be greyed out and the "stop" and "pause" button should be made available
     * c) if the "stop" button is pressed, it should be greyed along with the "pause" button and the "play" button should me made available.
     * d) if the "pause" button is pressed, it should be greyed out along with the "stop" button and "play" button should be made available.
     * e) if "play" button is pressed, do the same as (b)
     * f) if "stop" button is pressed, do the same as (c)
     */
    public static final String AUDIO_RECORDING_FRAGMENT_TAG = "AUDIO_RECORDING_FRAGMENT_TAG";
    public static final String IS_REORD_MODE_TAG = "IS_REORD_MODE_TAG";
    public static final String IS_VOICE_COMMENT_MODE_TAG = "IS_VOICE_COMMENT_MODE_TAG";
    private static final int MEDIA_PLAYER_LISTENER_UPDATE_FREQUENCY = 200;

    //View and layout
    private Menu mActionBarMenu;
    private View mRootView;
    private ImageButton mCentreButton;
    private ImageButton mLeftButton;
    private ImageButton mRightButton;
    private AudioVisualization mAudioVisualizerView;
    private SeekBar mSeekBar;

    //Handlers
    private VisualizerDbmHandler visualizerHandler;
    private MediaPlayerListener mediaPlayerListener;

    //Data
    private AudioRecorder audioRecorder;
    private boolean recordMode;
    private boolean voiceCommentMode;
    private boolean permissionGranted;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AudioRecordingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioRecordingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioRecordingFragment newInstance(String param1, String param2) {
        AudioRecordingFragment fragment = new AudioRecordingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if(bundle != null){
            recordMode = (boolean) bundle.getBoolean(AudioRecordingFragment.IS_REORD_MODE_TAG);
            voiceCommentMode = (boolean) bundle.getBoolean(AudioRecordingFragment.IS_VOICE_COMMENT_MODE_TAG);
        }
        if(voiceCommentMode){
            audioRecorder = new VoiceRecorder();
        }
        else{
            audioRecorder = new HeartSoundRecorder();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_audio_recording, container, false);
        mAudioVisualizerView = (AudioVisualization) mRootView.findViewById(R.id.audioVisualizerView);
        mSeekBar = (SeekBar) mRootView.findViewById(R.id.progressBar);
        mCentreButton = (ImageButton) mRootView.findViewById(R.id.centreButton);
        mLeftButton = (ImageButton) mRootView.findViewById(R.id.leftButton);
        mRightButton = (ImageButton) mRootView.findViewById(R.id.rightButton);
        setupListenersForButtons();
        if(recordMode){
            setupRecordModeButtons();
        }
        else{
            setupPlayModeButtons();
        }
        audioRecorder.resetAudioRecorder();
        return mRootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mAudioVisualizerView.onResume();
    }

    @Override
    public void onPause() {
        mAudioVisualizerView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView(){
        mAudioVisualizerView.release();
        if(recordMode){
            if(voiceCommentMode){
                ((VoiceRecorder)audioRecorder).closeMediaRecorder();
            }
        }
        audioRecorder.closeMediaPlayer();
        super.onDestroyView();
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

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_patient_heart_sound_tool_bar_items, menu);
        mActionBarMenu = menu;
        showActionBarMenuItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.saveChangesItem:
                saveChanges();
                break;
            case R.id.cancelChangesItem:
                cancelChanges();
                break;
        }
        return true;
    }

    protected void showActionBarMenuItems(){
        if(recordMode) {
            displayEditableMenuItems();
        }
        else{
            hideAllMenuItems();
        }

    }

    private void displayEditableMenuItems(){
        for(int i = 0; i < mActionBarMenu.size(); i++){
            MenuItem menuItem = mActionBarMenu.getItem(i);
            //if menu item is save or cancel
            if(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.saveChangesItemText)) ||
                    menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.cancelChangesItemText))){
                menuItem.setVisible(true);
            }
            else{
                menuItem.setVisible(false);
            }

        }
    }

    private void hideAllMenuItems(){
        for(int i = 0; i < mActionBarMenu.size(); i++){
            MenuItem menuItem = mActionBarMenu.getItem(i);
                menuItem.setVisible(false);
        }
    }

    private void saveChanges(){
        //upload the file that was recorded

        //close the fragment
        cancelChanges();
    }

    public void cancelChanges(){
        //delete the file that was recorded
        //audioRecorder.deleteRecordedMediaFile(); -- this has been commented out for testing, when the APIs are ready, this line should be taken out of comments
        //close all the Media Resources
        if(recordMode){
            stopRecording();
        }
        stopPlayingRecordedAudio();
        getActivity().getSupportFragmentManager().popBackStack();
        //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void setupRecordModeButtons(){
        enableRecordButton();
        disableStopButton();
        disableReplayButton();
    }

    private void setupPlayModeButtons(){
        enablePlayButton();
        disableStopButton();
        disablePauseButton();
    }

    private void setupListenersForButtons(){
        mCentreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCentreButtonClicked();
            }
        });
        mRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRightButtonClicked();
            }
        });
        mLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLeftButtonClicked();
            }
        });
    }

    private void onCentreButtonClicked(){
        if(recordMode){
            startRecording();
        }
        else{
            replayRecordedMedia();
            enableLeftButton();
        }
    }

    private void onRightButtonClicked(){
        if(recordMode){
            if(audioRecorder.isRecording()){
                stopRecording();
            }
            else if(audioRecorder.isReplaying()){
                stopPlayingRecordedAudio();
            }
        }
        else{
            stopPlayingRecordedAudio();
        }

    }

    private void onLeftButtonClicked(){
        if(recordMode){
            replayRecordedMedia();
        }
        else{
            pausePlayingMedia();
        }

    }

    private void startRecording(){
        this.permissionGranted = RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.RECORD_AUDIO, RequestPermission.RECORD_AUDIO)
                && RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermission.READ_EXTERNAL_STORAGE);
        if(permissionGranted){
            try {
                audioRecorder.startRecording();
                disableCentreButton();
                disableLeftButton();
                enableRightButton();
                Toast.makeText(getContext(),"Recording", Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getContext(),"You did not grant the required permission to the app", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void replayRecordedMedia(){
        this.permissionGranted = RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermission.READ_EXTERNAL_STORAGE)
                && RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RequestPermission.WRITE_EXTERNAL_STORAGE);
        if(permissionGranted){
            try {
                if(!audioRecorder.isPaused()){
                    mSeekBar.setProgress(0);
                }
                audioRecorder.replayRecording();
                //Reset the progress of the SeekBar
                mSeekBar.setMax(audioRecorder.getMediaPlayer().getDuration());
                startMediaPlayerListener();
                disableLeftButton();
                disableCentreButton();
                enableRightButton();
                setupAudioVisualizer();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(),"Playing", Toast.LENGTH_SHORT)
                    .show();

        }
        else{
            Toast.makeText(getContext(),"You did not grant the required permission to the app", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void setupAudioVisualizer(){
        if(visualizerHandler == null){
            visualizerHandler = DbmHandler.Factory.newVisualizerHandler(audioRecorder.getMediaPlayer());
            visualizerHandler.setInnerOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onReplayFinished();
                }
            });
            mAudioVisualizerView.linkTo(visualizerHandler);
        }
    }

    private void stopRecording(){
        this.permissionGranted = RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermission.READ_EXTERNAL_STORAGE)
                && RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RequestPermission.WRITE_EXTERNAL_STORAGE);
        if(permissionGranted){
            audioRecorder.stopRecording();
            disableRightButton();
            enableLeftButton();
            enableCentreButton();
            Toast.makeText(getContext(),"Recording Stopped", Toast.LENGTH_SHORT)
                    .show();

        }
        else{
            Toast.makeText(getContext(),"You did not grant the required permission to the app", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void onReplayFinished(){
        disableRightButton();
        enableCentreButton();
        if(recordMode){
            enableLeftButton();
        }
        else{
            disableLeftButton();
        }
        if(mediaPlayerListener != null){
            mediaPlayerListener.stopMediaPlayerListener();
            mSeekBar.setProgress(audioRecorder.getMediaPlayer().getCurrentPosition());
            Toast.makeText(getContext(),"Stopped", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void stopPlayingRecordedAudio(){
        this.permissionGranted = RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermission.READ_EXTERNAL_STORAGE)
                && RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RequestPermission.WRITE_EXTERNAL_STORAGE);
        if(permissionGranted){
            //The implementation of this methods needs to be checked again
            audioRecorder.stopReplay();
            onReplayFinished();
        }
        else{
            Toast.makeText(getContext(),"You did not grant the required permission to the app", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void pausePlayingMedia(){
        this.permissionGranted = RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE, RequestPermission.READ_EXTERNAL_STORAGE)
                && RequestPermission.requestUserPermission(this.getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE, RequestPermission.WRITE_EXTERNAL_STORAGE);
        if(permissionGranted){
            audioRecorder.pausePlaying();
            mediaPlayerListener.resumeMediaPlayerListener();
            disableLeftButton();
            disableRightButton();
            enableCentreButton();
            Toast.makeText(getContext(),"Paused", Toast.LENGTH_SHORT)
                    .show();
        }
        else{
            Toast.makeText(getContext(),"You did not grant the required permission to the app", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void startMediaPlayerListener(){
        mediaPlayerListener = new MediaPlayerListener();
        new Thread(mediaPlayerListener).start();
    }

    private void enableCentreButton(){
        if(recordMode){
            enableRecordButton();
        }
        else{
            enablePlayButton();
        }
    }

    private void disableCentreButton(){
        if(recordMode){
            disableRecordButton();
        }
        else{
            disablePlayButton();
        }
    }

    private void enableLeftButton(){
        if(recordMode){
            enableReplayButton();
        }
        else{
            enablePauseButton();
        }
    }

    private void disableLeftButton(){
        if(recordMode){
            disableReplayButton();
        }
        else{
            disablePauseButton();
        }
    }

    private void enableRightButton(){
        enableStopButton();
    }

    private void disableRightButton(){
        disableStopButton();
    }


    //---------------------------------------
    @Override
    public void enableRecordButton() {
        mCentreButton.setClickable(true);
        mCentreButton.setImageResource(R.drawable.ic_microphone_white_48dp);
    }

    @Override
    public void disableRecordButton() {
        mCentreButton.setClickable(false);
        mCentreButton.setImageResource(R.drawable.ic_microphone_grey600_48dp);
    }

    @Override
    public void enablePlayButton() {
        mCentreButton.setClickable(true);
        mCentreButton.setImageResource(R.drawable.ic_arrow_right_drop_circle_white_48dp);
    }

    @Override
    public void disablePlayButton() {
        mCentreButton.setClickable(false);
        mCentreButton.setImageResource(R.drawable.ic_arrow_right_drop_circle_grey600_48dp);
    }

    @Override
    public void enableStopButton() {
        mRightButton.setClickable(true);
        mRightButton.setImageResource(R.drawable.ic_stop_circle_white_36dp);
    }

    @Override
    public void disableStopButton() {
        mRightButton.setClickable(false);
        mRightButton.setImageResource(R.drawable.ic_stop_circle_grey600_36dp);
    }

    @Override
    public void enableReplayButton() {
        mLeftButton.setClickable(true);
        mLeftButton.setImageResource(R.drawable.ic_replay_white_36dp);
    }

    @Override
    public void disableReplayButton() {
        mLeftButton.setClickable(false);
        mLeftButton.setImageResource(R.drawable.ic_replay_grey600_36dp);
    }

    @Override
    public void enablePauseButton() {
        mLeftButton.setClickable(true);
        mLeftButton.setImageResource(R.drawable.ic_pause_circle_white_36dp);
    }

    @Override
    public void disablePauseButton() {
        mLeftButton.setClickable(false);
        mLeftButton.setImageResource(R.drawable.ic_pause_circle_grey600_36dp);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            this.permissionGranted = true;
        }
        else {
            this.permissionGranted = false;
        }

    }

    private class MediaPlayerListener implements Runnable{

        private AtomicBoolean stop = new AtomicBoolean(false);

        public void stopMediaPlayerListener(){
            stop.set(true);
        }

        public void resumeMediaPlayerListener(){
            if(audioRecorder.isReplaying()){
                this.notify();
            }
        }

        public void pauseMediaPlayerListener(){
            if(audioRecorder.isPaused()){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {
            while(!stop.get()){
                if(audioRecorder.getMediaPlayer().isPlaying()){
                    try {
                        mSeekBar.setProgress(audioRecorder.getMediaPlayer().getCurrentPosition());
                        Thread.sleep(MEDIA_PLAYER_LISTENER_UPDATE_FREQUENCY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
