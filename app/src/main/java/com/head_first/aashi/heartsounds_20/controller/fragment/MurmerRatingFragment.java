package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.enums.AddedSounds;
import com.head_first.aashi.heartsounds_20.enums.CardiacPhase;
import com.head_first.aashi.heartsounds_20.enums.ChangeWithBreathing;
import com.head_first.aashi.heartsounds_20.enums.CHARACTER;
import com.head_first.aashi.heartsounds_20.enums.FinalDiagnosis;
import com.head_first.aashi.heartsounds_20.enums.Intensity;
import com.head_first.aashi.heartsounds_20.enums.LeftLateralPosition;
import com.head_first.aashi.heartsounds_20.enums.MostIntenseLocation;
import com.head_first.aashi.heartsounds_20.enums.MurmurDuration;
import com.head_first.aashi.heartsounds_20.enums.Radiation;
import com.head_first.aashi.heartsounds_20.enums.S1;
import com.head_first.aashi.heartsounds_20.enums.S2;
import com.head_first.aashi.heartsounds_20.enums.SittingForward;
import com.head_first.aashi.heartsounds_20.enums.Valsalva;
import com.head_first.aashi.heartsounds_20.utils.MultiCharacterSelectorListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MurmerRatingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MurmerRatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MurmerRatingFragment extends Fragment {
    public static final String MURMER_RATING_FRAGMENT_TAG = "MURMER_RATING_FRAGMENT";
    public static final String SELECTED_MURMER_RATING_TAG = "SELECTED_MURMER_RATING_TAG";

    //Layout and View
    private View mRootView;
    //TextViews
    private TextView mPhaseOfCardiacCycleText;
    private TextView mIntensityText;
    private TextView mMurmerDurationText;
    private TextView mMostIntenseLocationText;
    private TextView mRadiationText;
    private TextView mCharacterText;
    private TextView mSelectedCharacters;
    private TextView mAddedSoundsText;
    private TextView mSOneText;
    private TextView mSTwoText;
    private TextView mChangeWithBreathingText;
    private TextView mValsalvaText;
    private TextView mLeftLateralPositionText;
    private TextView mSittingForwardText;
    private TextView mFinalDiagnosisText;

    //Spinners
    private Spinner mPhaseOfCardiacCycleSpinner;
    private Spinner mIntensitySpinner;
    private Spinner mMurmerDurationSpinner;
    private Spinner mMostIntenseLocationSpinner;
    private Spinner mRadiationSpinner;
    private Spinner mCharacterSpinner;
    private Spinner mAddedSoundsSpinner;
    private Spinner mSOneSpinner;
    private Spinner mSTwoSpinner;
    private Spinner mChangeWithBreathingSpinner;
    private Spinner mValsalvaSpinner;
    private Spinner mLeftLateralPositionSpinner;
    private Spinner mSittingForwardSpinner;
    private Spinner mFinalDiagnosisSpinner;

    //ListView
    private ListView mCharacterSelector;

    //Adapters
    private ArrayAdapter<CardiacPhase> mPhaseOfCardiacCycleSpinnerAdapter;
    private ArrayAdapter<Intensity> mIntensitySpinnerAdapter;
    private ArrayAdapter<MurmurDuration> mMurmerDurationSpinnerAdapter;
    private ArrayAdapter<MostIntenseLocation> mMostIntenseLocationSpinnerAdapter;
    private ArrayAdapter<Radiation> mRadiationSpinnerAdapter;
    private ArrayAdapter<CHARACTER> mCharacterSpinnerAdapter;
    private ArrayAdapter<AddedSounds> mAddedSoundsSpinnerAdapter;
    private ArrayAdapter<S1> mSOneSpinnerAdapter;
    private ArrayAdapter<S2> mSTwoSpinnerAdapter;
    private ArrayAdapter<ChangeWithBreathing> mChangeWithBreathingSpinnerAdapter;
    private ArrayAdapter<Valsalva> mValsalvaSpinnerAdapter;
    private ArrayAdapter<LeftLateralPosition> mLeftLateralPositionSpinnerAdapter;
    private ArrayAdapter<SittingForward> mSittingForwardSpinnerAdapter;
    private ArrayAdapter<FinalDiagnosis> mFinalDiagnosisSpinnerAdapter;
    private MultiCharacterSelectorListAdapter mMultiCharacterSelectorListAdapter;

    //Data
    private int selectedMurmerRatingPosition;
    private List<CHARACTER> selectedCharacters; //this will be deleted. Instead of this the List<CHARACTER> from the MurmerRating object will be used

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MurmerRatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MurmerRatingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MurmerRatingFragment newInstance(String param1, String param2) {
        MurmerRatingFragment fragment = new MurmerRatingFragment();
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
        selectedCharacters = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        if(arguments == null){
            //throw an exception
        }
        selectedMurmerRatingPosition = arguments.getInt(SELECTED_MURMER_RATING_TAG);
        mRootView = inflater.inflate(R.layout.fragment_murmer_rating, container, false);
        setUpTextViews();
        setUpSpinnersAndAdapters();

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
        inflater.inflate(R.menu.heart_sound_murmer_rating_tool_bar_items, menu);
        //if the user is the creator of this MurmerRating then display the edit menuitem
        menu.findItem(R.id.editItem).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.deletePatientItem:

        }
        return true;
    }

    private void setUpTextViews(){
        mPhaseOfCardiacCycleText = (TextView) mRootView.findViewById(R.id.phaseOfCardiacCycleText);
        mIntensityText = (TextView) mRootView.findViewById(R.id.intensityText);
        mMurmerDurationText = (TextView) mRootView.findViewById(R.id.murmerDurationText);
        mMostIntenseLocationText = (TextView) mRootView.findViewById(R.id.mostIntenseLocationText);
        mRadiationText = (TextView) mRootView.findViewById(R.id.radiationText);
        mAddedSoundsText = (TextView) mRootView.findViewById(R.id.addedSoundsText);
        mSOneText = (TextView) mRootView.findViewById(R.id.sOneText);
        mSTwoText = (TextView) mRootView.findViewById(R.id.sTwoText);
        mChangeWithBreathingText = (TextView) mRootView.findViewById(R.id.changeWithBreathingText);
        mValsalvaText = (TextView) mRootView.findViewById(R.id.valsalvaText);
        mLeftLateralPositionText = (TextView) mRootView.findViewById(R.id.leftLateralPositionText);
        mSittingForwardText = (TextView) mRootView.findViewById(R.id.sittingForwardText);
        mFinalDiagnosisText = (TextView) mRootView.findViewById(R.id.finalDiagnosisText);
        mCharacterText = (TextView) mRootView.findViewById(R.id.murmerDurationText);
        mSelectedCharacters = (TextView) mRootView.findViewById(R.id.selectedCharacters);
        mSelectedCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCharactersDialog();
            }
        });

    }

    private void setUpSpinnersAndAdapters(){
        //Cardiac Phase
        mPhaseOfCardiacCycleSpinner = (Spinner) mRootView.findViewById(R.id.phaseOfCardiacCycleSpinner);
        mPhaseOfCardiacCycleSpinnerAdapter = new ArrayAdapter<CardiacPhase>(getContext(), android.R.layout.simple_spinner_item, CardiacPhase.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPhaseOfCardiacCycleSpinner.setAdapter(mPhaseOfCardiacCycleSpinnerAdapter);

        //Intensity
        mIntensitySpinner = (Spinner) mRootView.findViewById(R.id.intensitySpinner);
        mIntensitySpinnerAdapter = new ArrayAdapter<Intensity>(getContext(), android.R.layout.simple_spinner_item, Intensity.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mIntensitySpinner.setAdapter(mIntensitySpinnerAdapter);

        //Murmer Duration
        mMurmerDurationSpinner = (Spinner) mRootView.findViewById(R.id.murmerDurationSpinner);
        mMurmerDurationSpinnerAdapter = new ArrayAdapter<MurmurDuration>(getContext(), android.R.layout.simple_spinner_item, MurmurDuration.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMurmerDurationSpinner.setAdapter(mMurmerDurationSpinnerAdapter);

        //Most Intense Location
        mMostIntenseLocationSpinner = (Spinner) mRootView.findViewById(R.id.mostIntenseLocationSpinner);
        mMostIntenseLocationSpinnerAdapter = new ArrayAdapter<MostIntenseLocation>(getContext(), android.R.layout.simple_spinner_item, MostIntenseLocation.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMostIntenseLocationSpinner.setAdapter(mMostIntenseLocationSpinnerAdapter);


        //Radiation
        mRadiationSpinner = (Spinner) mRootView.findViewById(R.id.radiationSpinner);
        mRadiationSpinnerAdapter = new ArrayAdapter<Radiation>(getContext(), android.R.layout.simple_spinner_item, Radiation.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRadiationSpinner.setAdapter(mRadiationSpinnerAdapter);

        //Added Sound
        mAddedSoundsSpinner = (Spinner) mRootView.findViewById(R.id.addedSoundsSpinner);
        mAddedSoundsSpinnerAdapter = new ArrayAdapter<AddedSounds>(getContext(), android.R.layout.simple_spinner_item, AddedSounds.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAddedSoundsSpinner.setAdapter(mAddedSoundsSpinnerAdapter);

        //S1
        mSOneSpinner = (Spinner) mRootView.findViewById(R.id.sOneSpinner);
        mSOneSpinnerAdapter = new ArrayAdapter<S1>(getContext(), android.R.layout.simple_spinner_item, S1.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSOneSpinner.setAdapter(mSOneSpinnerAdapter);

        //S2
        mSTwoSpinner = (Spinner) mRootView.findViewById(R.id.sTwoSpinner);
        mSTwoSpinnerAdapter = new ArrayAdapter<S2>(getContext(), android.R.layout.simple_spinner_item, S2.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSTwoSpinner.setAdapter(mSTwoSpinnerAdapter);

        //Change With Breathing
        mChangeWithBreathingSpinner = (Spinner) mRootView.findViewById(R.id.changeWithBreathingSpinner);
        mChangeWithBreathingSpinnerAdapter = new ArrayAdapter<ChangeWithBreathing>(getContext(), android.R.layout.simple_spinner_item, ChangeWithBreathing.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChangeWithBreathingSpinner.setAdapter(mChangeWithBreathingSpinnerAdapter);

        //Valsalva
        mValsalvaSpinner = (Spinner) mRootView.findViewById(R.id.valsalvaSpinner);
        mValsalvaSpinnerAdapter = new ArrayAdapter<Valsalva>(getContext(), android.R.layout.simple_spinner_item, Valsalva.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mValsalvaSpinner.setAdapter(mValsalvaSpinnerAdapter);

        //Left Lateral Position
        mLeftLateralPositionSpinner = (Spinner) mRootView.findViewById(R.id.leftLateralPositionSpinner);
        mLeftLateralPositionSpinnerAdapter = new ArrayAdapter<LeftLateralPosition>(getContext(), android.R.layout.simple_spinner_item, LeftLateralPosition.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLeftLateralPositionSpinner.setAdapter(mLeftLateralPositionSpinnerAdapter);

        //Sitting Forward
        mSittingForwardSpinner = (Spinner) mRootView.findViewById(R.id.sittingForwardSpinner);
        mSittingForwardSpinnerAdapter = new ArrayAdapter<SittingForward>(getContext(), android.R.layout.simple_spinner_item, SittingForward.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSittingForwardSpinner.setAdapter(mSittingForwardSpinnerAdapter);

        //Final Diagnosis
        mFinalDiagnosisSpinner = (Spinner) mRootView.findViewById(R.id.finalDiagnosisSpinner);
        mFinalDiagnosisSpinnerAdapter = new ArrayAdapter<FinalDiagnosis>(getContext(), android.R.layout.simple_spinner_item, FinalDiagnosis.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFinalDiagnosisSpinner.setAdapter(mFinalDiagnosisSpinnerAdapter);

        //Character Adapter Setup
        mMultiCharacterSelectorListAdapter = new MultiCharacterSelectorListAdapter(getContext(), selectedCharacters);
    }

    private void displayCharactersDialog(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View characterSelectorDialog = (View)inflater.inflate(R.layout.dialog_character_select, null);

        mCharacterSelector = (ListView)characterSelectorDialog.findViewById(R.id.characterSelector);
        mCharacterSelector.setAdapter(mMultiCharacterSelectorListAdapter);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.multipleCharacterSelectorDialogTitle)
                .setCancelable(false)
                .setView(characterSelectorDialog)
                .setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selectedCharacters.isEmpty()){
                            mSelectedCharacters.setText(R.string.selectedCharacters);
                        }
                        else{
                            mSelectedCharacters.setText(selectedCharacters.toString());
                        }
                    }
                })
                .create()
                .show();

    }
}
