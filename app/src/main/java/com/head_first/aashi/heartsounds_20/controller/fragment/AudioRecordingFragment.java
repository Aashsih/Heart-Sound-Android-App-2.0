package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.utils.AudioRecorder;
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
    public static final String AUDIO_RECORDING_FRAGMENT_TAG = "AUDIO_RECORDING_FRAGMENT_TAG";

    //View and layout
    private Menu mActionBarMenu;
    private View mRootView;
    private ImageButton mRecordButton;
    private ImageButton mReplayButton;
    private ImageButton mStopButton;

    //Data
    private AudioRecorder audioRecorder;

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
        //Check which Recorder needs to be instantiated : VoiceRecoreder or HeartSoundRecorder
        audioRecorder = new VoiceRecorder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_audio_recording, container, false);
        mRecordButton = (ImageButton) mRootView.findViewById(R.id.recordButton);
        mReplayButton = (ImageButton) mRootView.findViewById(R.id.replayButton);
        mStopButton = (ImageButton) mRootView.findViewById(R.id.stopButton);
        setupListenersForButtons();
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
                    audioRecorder.beginRecording();
                    //before setting any boolean to true, check which ones needs to be set to false
                    audioRecorder.setRecording(true);
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
                    audioRecorder.finishRecording();
                }
                else if(audioRecorder.isReplaying()){
                    audioRecorder.stopReplay();
                }
                //before setting any boolean to true, check which ones needs to be set to false
                audioRecorder.setStopped(true);
                Toast.makeText(getContext(),"Recording/Playing Stopped", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mReplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audioRecorder.isStopped()){
                    try {
                        audioRecorder.playRecording();
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
