package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.media.Image;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.utils.AudioRecorder;
import com.head_first.aashi.heartsounds_20.utils.StethoscopeInteraction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeartSoundFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeartSoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartSoundFragment extends EditableFragment {
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

    //Data

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


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HeartSoundFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HeartSoundFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeartSoundFragment newInstance(String param1, String param2) {
        HeartSoundFragment fragment = new HeartSoundFragment();
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
        mActionBarMenu =  menu;
        //if the user is the creator of this HeartSound then display the edit menuitem
        showActionBarMenuItems();
    }

    @Override
    protected void editUserProfile() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void saveChanges() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancelChanges() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void showEditableViews() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void showNonEditableViews() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void hideNonEditableViews() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void hideEditableViews() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void showActionBarMenuItems(){
        for(int i = 0; i < mActionBarMenu.size(); i++){
            MenuItem menuItem = mActionBarMenu.getItem(i);
            if(editMode){
                //if menu item is save or cancel
                if(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.saveChangesItemText)) ||
                        menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.cancelChangesItemText))){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }
            }
            else{
                if(!(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.saveChangesItemText)) ||
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.deletePatientItem:
                break;
            case R.id.editItem:
                break;
            case R.id.refreshViewItem:
                break;
        }
        return true;
    }

    @Override
    public boolean editModeEnabled(){
        return editMode;
    }

    @Override
    protected void makeViewsEditable() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void makeViewsUneditable() {
        throw new UnsupportedOperationException();
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
        AudioRecordingFragment audioRecordingFragment = new AudioRecordingFragment();
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
        StethoscopeInteractionFragment stethoscopeInteractionFragment = new StethoscopeInteractionFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(StethoscopeInteractionFragment.IS_VOICE_COMMENT_MODE_TAG, voiceCommentMode);
        stethoscopeInteractionFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, stethoscopeInteractionFragment, StethoscopeInteractionFragment.STETHOSCOPE_INTERACTION_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

}
