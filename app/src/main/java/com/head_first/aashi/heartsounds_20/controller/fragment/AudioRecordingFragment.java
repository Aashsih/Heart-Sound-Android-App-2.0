package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.cleveroad.audiovisualization.AudioVisualization;
import com.cleveroad.audiovisualization.DbmHandler;
import com.cleveroad.audiovisualization.VisualizerDbmHandler;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.utils.AudioRecorder;
import com.head_first.aashi.heartsounds_20.utils.HeartSoundRecorder;
import com.head_first.aashi.heartsounds_20.utils.VoiceRecorder;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioRecordingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioRecordingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioRecordingFragment extends Fragment {
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
     * d) if the "pause" button is pressed, it should be greyed out and the "stop" and "play" button should be made available.
     * e) if "play" button is pressed, do the same as (b)
     * f) if "stop" button is pressed, do the same as (c)
     */
    public static final String AUDIO_RECORDING_FRAGMENT_TAG = "AUDIO_RECORDING_FRAGMENT_TAG";

    //View and layout
    private Menu mActionBarMenu;
    private View mRootView;
    private ImageButton mRecordButton;
    private ImageButton mReplayButton;
    private ImageButton mStopButton;
    private AudioVisualization mAudioVisualizerView;

    //Data
    private AudioRecorder audioRecorder;
    private boolean recordMode;
    private boolean voiceCommentMode;

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
        mRecordButton = (ImageButton) mRootView.findViewById(R.id.recordButton);
        mReplayButton = (ImageButton) mRootView.findViewById(R.id.replayButton);
        mStopButton = (ImageButton) mRootView.findViewById(R.id.stopButton);
        setupListenersForButtons();
        /*
        VisualizerDbmHandler speechRecognizerDbmHandler = DbmHandler.Factory.newVisualizerHandler(((VoiceRecorder)audioRecorder).getMediaPlayer());
        //if(Voice comment is accessed)
        //then set the speechRecognizerDbmHandler.innerRecognitionListener()
        speechRecognizerDbmHandler.setInnerOnPreparedListener(new MediaPlayer.OnPreparedListener(){
            @Override
            public void onPrepared(MediaPlayer mediaPlayer){
                try {
                    ((VoiceRecorder)audioRecorder).getMediaPlayer().prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //speechRecognizerDbmHandler.setInnerOnCompletionListener();
        mAudioVisualizerView.linkTo(speechRecognizerDbmHandler);
        */
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

    private void saveChanges(){
        //upload the file that was recorded

        //close the fragment
    }

    public void cancelChanges(){
        //delete the file that was recorded

        //close the fragment
    }

    private void setupListenersForButtons(){
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    audioRecorder.startRecording();
                    Toast.makeText(getContext(),"Recording Started", Toast.LENGTH_SHORT)
                            .show();
                } catch (IOException e) {
                    Log.d("Logging an exception", "Logging an exception");
                    e.printStackTrace();
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioRecorder.isRecording()){
                    audioRecorder.stopRecording();
                }
                else if(audioRecorder.isReplaying()){
                    audioRecorder.stopReplay();
                }
                mAudioVisualizerView.release();
                //before setting any boolean to true, check which ones needs to be set to false
                audioRecorder.stopRecording();
                Toast.makeText(getContext(),"Recording/Playing Stopped", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioRecorder.isStopRecording()){
                    try {
                        audioRecorder.replayRecording();
                        mAudioVisualizerView.onResume();
                        VisualizerDbmHandler speechRecognizerDbmHandler = DbmHandler.Factory.newVisualizerHandler(((VoiceRecorder)audioRecorder).getMediaPlayer());
                        mAudioVisualizerView.linkTo(speechRecognizerDbmHandler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getContext(),"Replaying Recording", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}
