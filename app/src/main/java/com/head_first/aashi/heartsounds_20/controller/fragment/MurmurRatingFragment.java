package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.AddedSounds;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CardiacPhase;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.ChangeWithBreathing;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.CHARACTER;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.FinalDiagnosis;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Intensity;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.LeftLateralPosition;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.MostIntenseLocation;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.MurmurDuration;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Radiation;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S1;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.S2;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.SittingForward;
import com.head_first.aashi.heartsounds_20.enums.murmur_rating_enums.Valsalva;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.util_interfaces.NavgigationDrawerUtils;
import com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces.MurmurRatingAPI;
import com.head_first.aashi.heartsounds_20.model.MurmurRating;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;
import com.head_first.aashi.heartsounds_20.utils.MultiSelectorListAdapter;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MurmurRatingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MurmurRatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MurmurRatingFragment extends EditableFragment implements MurmurRatingAPI{
    /**
     * Highlight the selected MurmerRating in the navigation menu
     */
    public static final String MURMER_RATING_FRAGMENT_TAG = "MURMER_RATING_FRAGMENT";
    public static final String SELECTED_MURMER_RATING_TAG = "SELECTED_MURMER_RATING_TAG";

    //Layout and View
    private Activity mParentActivity;
    private Menu mActionBarMenu;
    private View mRootView;
    //TextViews
    private TextView mSelectedCharacters;
    private TextView mPhaseOfCardiacCycleText;
    //private TextView mIntensityText;
    private TextView mMurmerDurationText;
    private TextView mMostIntenseLocationText;
    private TextView mRadiationText;
    ///private TextView mCharacterText;
    private TextView mAddedSoundsText;
    private TextView mSOneText;
    private TextView mSTwoText;
    private TextView mChangeWithBreathingText;
    private TextView mValsalvaText;
    private TextView mLeftLateralPositionText;
    private TextView mSittingForwardText;
    //private TextView mFinalDiagnosisText;

    //Spinners
    private Spinner mPhaseOfCardiacCycleSpinner;
    //private Spinner mIntensitySpinner;
    private Spinner mMurmurDurationSpinner;
    private Spinner mMostIntenseLocationSpinner;
    private Spinner mRadiationSpinner;
    //private Spinner mCharacterSpinner;
    private Spinner mAddedSoundsSpinner;
    private Spinner mSOneSpinner;
    private Spinner mSTwoSpinner;
    private Spinner mChangeWithBreathingSpinner;
    private Spinner mValsalvaSpinner;
    private Spinner mLeftLateralPositionSpinner;
    private Spinner mSittingForwardSpinner;
    //private Spinner mFinalDiagnosisSpinner;

    //ListView
    private ListView mCharacterSelector;

    //Adapters
    private ArrayAdapter<CardiacPhase> mPhaseOfCardiacCycleSpinnerAdapter;
    private ArrayAdapter<Intensity> mIntensitySpinnerAdapter;
    private ArrayAdapter<MurmurDuration> mMurmerDurationSpinnerAdapter;
    private ArrayAdapter<MostIntenseLocation> mMostIntenseLocationSpinnerAdapter;
    private ArrayAdapter<Radiation> mRadiationSpinnerAdapter;
    //private ArrayAdapter<CHARACTER> mCharacterSpinnerAdapter;
    private ArrayAdapter<AddedSounds> mAddedSoundsSpinnerAdapter;
    private ArrayAdapter<S1> mSOneSpinnerAdapter;
    private ArrayAdapter<S2> mSTwoSpinnerAdapter;
    private ArrayAdapter<ChangeWithBreathing> mChangeWithBreathingSpinnerAdapter;
    private ArrayAdapter<Valsalva> mValsalvaSpinnerAdapter;
    private ArrayAdapter<LeftLateralPosition> mLeftLateralPositionSpinnerAdapter;
    private ArrayAdapter<SittingForward> mSittingForwardSpinnerAdapter;
    private ArrayAdapter<FinalDiagnosis> mFinalDiagnosisSpinnerAdapter;
    private MultiSelectorListAdapter mMultiCharacterSelectorListAdapter;

    //Data
    private Integer selectedMurmerRatingPosition;
    private List<CHARACTER> selectedCharacters;
    private MurmurRating murmurRating;

    //Web API
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public MurmurRatingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment MurmurRatingFragment.
     */
    public static MurmurRatingFragment newInstance() {
        MurmurRatingFragment fragment = new MurmurRatingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectedCharacters = new ArrayList<>();//delete this later
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_murmer_rating, container, false);
        setUpTextViews();
        setUpSpinnersAndAdapters();
        Bundle arguments = getArguments();
        if(arguments != null){
            selectedMurmerRatingPosition = arguments.getInt(SELECTED_MURMER_RATING_TAG);
            if(selectedMurmerRatingPosition == null){
                editMode = true;
            }
        }
        else{
            editMode = true;
        }
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mParentActivity = getActivity();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(editMode){
            hideNonEditableViews();
            showEditableViews();
            makeViewsEditable();
            //copyDataFromTextViewToSpinner();
            ((NavgigationDrawerUtils)getActivity()).disableNavigationMenu();
        }
        else{
            ((PatientHeartSoundActivity)getActivity()).setupNavigationDrawerContent();
            requestMurmurRating(MurmurRating.getIdFromString(((PatientHeartSoundActivity)getActivity()).getSelectedMurmurRating(selectedMurmerRatingPosition)).intValue());
        }
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

    public void setupViewsWithMurmurRatingData(){
        if(murmurRating == null){
            getActivity().getSupportFragmentManager().popBackStack();
        }
        else{
            mSelectedCharacters.setText(murmurRating.getCharacter());
            mPhaseOfCardiacCycleText.setText(murmurRating.getCardiacPhase().toString());
            //mIntensityText.setText(murmurRating.getIntensity().toString());
            mMurmerDurationText.setText(murmurRating.getDurationOfMurmur().toString());
            mMostIntenseLocationText.setText(murmurRating.getLocationMostIntense().toString());
            mRadiationText.setText(murmurRating.getRadiation().toString());
            //mCharacterText.setText(murmurRating.get);
            mAddedSoundsText.setText(murmurRating.getAddedSounds().toString());
            mSOneText.setText(murmurRating.getS1().toString());
            mSTwoText.setText(murmurRating.getS2().toString());
            mChangeWithBreathingText.setText(murmurRating.getChangeWithBreathing().toString());
            mValsalvaText.setText(murmurRating.getValsalva().toString());
            mLeftLateralPositionText.setText(murmurRating.getLeftLateralPosition().toString());
            mSittingForwardText.setText(murmurRating.getSittingForward().toString());
            //mFinalDiagnosisText.setText(murmurRating.getFinalDiagnosis().toString());
        }
    }

    private void copyUpdatedDataFromViews(){
        if(murmurRating == null){
            murmurRating = new MurmurRating(SharedPreferencesManager.getActiveUserId(getActivity()),
                    ((PatientHeartSoundActivity)(getActivity())).getSelectedHeartSound());
        }
        else{
            murmurRating.setCardiacPhase(CardiacPhase.getCardiacPhase(mPhaseOfCardiacCycleSpinner.getSelectedItem().toString()));
            murmurRating.setDurationOfMurmur(MurmurDuration.getMurmurDuration(mMurmurDurationSpinner.getSelectedItem().toString()));
            murmurRating.setLocationMostIntense(MostIntenseLocation.getMostIntenseLocation(mMostIntenseLocationSpinner.getSelectedItem().toString()));
            murmurRating.setRadiation(Radiation.getRadiation(mRadiationSpinner.getSelectedItem().toString()));
            murmurRating.setCharacter(mSelectedCharacters.getText().toString());
            murmurRating.setAddedSounds(AddedSounds.getAddedSounds(mAddedSoundsSpinner.getSelectedItem().toString()));
            murmurRating.setS1(S1.getSOne(mSOneSpinner.getSelectedItem().toString()));
            murmurRating.setS2(S2.getSTwo(mSTwoSpinner.getSelectedItem().toString()));
            murmurRating.setChangeWithBreathing(ChangeWithBreathing.getChangeWithBreathing(mChangeWithBreathingSpinner.getSelectedItem().toString()));
            murmurRating.setValsalva(Valsalva.getValsalva(mValsalvaSpinner.getSelectedItem().toString()));
            murmurRating.setLeftLateralPosition(LeftLateralPosition.getLeftLateralPosition(mLeftLateralPositionSpinner.getSelectedItem().toString()));
            murmurRating.setSittingForward(SittingForward.getSittingForward(mSittingForwardSpinner.getSelectedItem().toString()));
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_patient_heart_sound_tool_bar_items, menu);
        mActionBarMenu = menu;
        showActionBarMenuItems();
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
            case R.id.deleteItem:
                if(murmurRating != null){
                    deleteMurmurRating((int)murmurRating.getMurmurRatingID());
                }
                break;
            case R.id.editItem:
                editFragment();
                break;
            case R.id.refreshViewItem:
                break;
            case R.id.saveChangesItem:
                saveChanges();
                break;
            case R.id.cancelChangesItem:
                cancelChanges();
                break;
        }
        return true;
    }

    @Override
    protected void editFragment(){
        editMode = true;
        hideNonEditableViews();
        showEditableViews();
        makeViewsEditable();
        showActionBarMenuItems();
        copyDataFromTextViewToSpinner();
        ((NavgigationDrawerUtils)getActivity()).disableNavigationMenu();
    }

    @Override
    protected void saveChanges(){
        try {
            editMode = false;
            //Copy the data from the views into the models and make a PUT request to update the database
            copyUpdatedDataFromViews();
            hideEditableViews();
            showNonEditableViews();
            makeViewsUneditable();
            showActionBarMenuItems();
            ((NavgigationDrawerUtils)getActivity()).enableNavigationMenu();
            if(selectedMurmerRatingPosition == null){
                createMurmurRating(murmurRating);
            }
            else{
                updateMurmurRating(murmurRating);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelChanges(){
        if(selectedMurmerRatingPosition == null || murmurRating == null){
            getActivity().getSupportFragmentManager().popBackStack();
        }
        editMode = false;
        //Copy the data from the models into the Views
        setupViewsWithMurmurRatingData();
        hideEditableViews();
        showNonEditableViews();
        makeViewsUneditable();
        showActionBarMenuItems();
        ((NavgigationDrawerUtils)getActivity()).enableNavigationMenu();
    }

    @Override
    protected void makeViewsEditable(){
        if(editMode){
//            mPhaseOfCardiacCycleSpinner.setEnabled(true);
//            mIntensitySpinner.setEnabled(true);
//            mMurmurDurationSpinner.setEnabled(true);
//            mMostIntenseLocationSpinner.setEnabled(true);
//            mRadiationSpinner.setEnabled(true);
//            //mCharacterSpinner.setEnabled(true);
//            mAddedSoundsSpinner.setEnabled(true);
//            mSOneSpinner.setEnabled(true);
//            mSTwoSpinner.setEnabled(true);
//            mChangeWithBreathingSpinner.setEnabled(true);
//            mValsalvaSpinner.setEnabled(true);
//            mLeftLateralPositionSpinner.setEnabled(true);
//            mSittingForwardSpinner.setEnabled(true);
//            mFinalDiagnosisSpinner.setEnabled(true);
            mSelectedCharacters.setClickable(true);
        }
    }

    @Override
    protected void makeViewsUneditable(){
        if(!editMode){
//            mPhaseOfCardiacCycleSpinner.setEnabled(false);
//            mIntensitySpinner.setEnabled(false);
//            mMurmurDurationSpinner.setEnabled(false);
//            mMostIntenseLocationSpinner.setEnabled(false);
//            mRadiationSpinner.setEnabled(false);
//            //mCharacterSpinner.setEnabled(false);
//            mAddedSoundsSpinner.setEnabled(false);
//            mSOneSpinner.setEnabled(false);
//            mSTwoSpinner.setEnabled(false);
//            mChangeWithBreathingSpinner.setEnabled(false);
//            mValsalvaSpinner.setEnabled(false);
//            mLeftLateralPositionSpinner.setEnabled(false);
//            mSittingForwardSpinner.setEnabled(false);
//            mFinalDiagnosisSpinner.setEnabled(false);
            mSelectedCharacters.setClickable(false);
        }
    }

    @Override
    protected void showEditableViews(){
        if(editMode){
            mPhaseOfCardiacCycleSpinner.setVisibility(View.VISIBLE);
            //mIntensitySpinner.setVisibility(View.VISIBLE);
            mMurmurDurationSpinner.setVisibility(View.VISIBLE);
            mMostIntenseLocationSpinner.setVisibility(View.VISIBLE);
            mRadiationSpinner.setVisibility(View.VISIBLE);
            mAddedSoundsSpinner.setVisibility(View.VISIBLE);
            mSOneSpinner.setVisibility(View.VISIBLE);
            mSTwoSpinner.setVisibility(View.VISIBLE);
            mChangeWithBreathingSpinner.setVisibility(View.VISIBLE);
            mValsalvaSpinner.setVisibility(View.VISIBLE);
            mLeftLateralPositionSpinner.setVisibility(View.VISIBLE);
            mSittingForwardSpinner.setVisibility(View.VISIBLE);
            //mFinalDiagnosisSpinner.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showNonEditableViews(){
        if(!editMode){
            mPhaseOfCardiacCycleText.setVisibility(View.VISIBLE);
            //mIntensityText.setVisibility(View.VISIBLE);
            mMurmerDurationText.setVisibility(View.VISIBLE);
            mMostIntenseLocationText.setVisibility(View.VISIBLE);
            mRadiationText.setVisibility(View.VISIBLE);
            mSelectedCharacters.setVisibility(View.VISIBLE);
            mAddedSoundsText.setVisibility(View.VISIBLE);
            mSOneText.setVisibility(View.VISIBLE);
            mSTwoText.setVisibility(View.VISIBLE);
            mChangeWithBreathingText.setVisibility(View.VISIBLE);
            mValsalvaText.setVisibility(View.VISIBLE);
            mLeftLateralPositionText.setVisibility(View.VISIBLE);
            mSittingForwardText.setVisibility(View.VISIBLE);
            //mFinalDiagnosisText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void hideNonEditableViews(){
        if(editMode){
            mPhaseOfCardiacCycleText.setVisibility(View.GONE);
            //mIntensityText.setVisibility(View.GONE);
            mMurmerDurationText.setVisibility(View.GONE);
            mMostIntenseLocationText.setVisibility(View.GONE);
            mRadiationText.setVisibility(View.GONE);
            //mSelectedCharacters.setVisibility(View.GONE);
            mAddedSoundsText.setVisibility(View.GONE);
            mSOneText.setVisibility(View.GONE);
            mSTwoText.setVisibility(View.GONE);
            mChangeWithBreathingText.setVisibility(View.GONE);
            mValsalvaText.setVisibility(View.GONE);
            mLeftLateralPositionText.setVisibility(View.GONE);
            mSittingForwardText.setVisibility(View.GONE);
            //mFinalDiagnosisText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void hideEditableViews(){
        if(!editMode){
            mPhaseOfCardiacCycleSpinner.setVisibility(View.GONE);
            //mIntensitySpinner.setVisibility(View.GONE);
            mMurmurDurationSpinner.setVisibility(View.GONE);
            mMostIntenseLocationSpinner.setVisibility(View.GONE);
            mRadiationSpinner.setVisibility(View.GONE);
            mAddedSoundsSpinner.setVisibility(View.GONE);
            mSOneSpinner.setVisibility(View.GONE);
            mSTwoSpinner.setVisibility(View.GONE);
            mChangeWithBreathingSpinner.setVisibility(View.GONE);
            mValsalvaSpinner.setVisibility(View.GONE);
            mLeftLateralPositionSpinner.setVisibility(View.GONE);
            mSittingForwardSpinner.setVisibility(View.GONE);
            //mFinalDiagnosisSpinner.setVisibility(View.GONE);
        }
    }

    private void copyDataFromTextViewToSpinner(){
        mPhaseOfCardiacCycleSpinner.setSelection(mPhaseOfCardiacCycleSpinnerAdapter.getPosition(CardiacPhase.getCardiacPhase(mPhaseOfCardiacCycleText.getText().toString())));
        //mIntensitySpinner.setSelection(mIntensitySpinnerAdapter.getPosition(Intensity.getIntensity(mIntensityText.getText().toString())));
        mMurmurDurationSpinner.setSelection(mMurmerDurationSpinnerAdapter.getPosition(MurmurDuration.getMurmurDuration(mMurmerDurationText.getText().toString())));
        mMostIntenseLocationSpinner.setSelection(mMostIntenseLocationSpinnerAdapter.getPosition(MostIntenseLocation.getMostIntenseLocation(mMostIntenseLocationText.getText().toString())));;
        mRadiationSpinner.setSelection(mRadiationSpinnerAdapter.getPosition(Radiation.getRadiation(mRadiationText.getText().toString())));;
        mAddedSoundsSpinner.setSelection(mAddedSoundsSpinnerAdapter.getPosition(AddedSounds.getAddedSounds(mAddedSoundsText.getText().toString())));;
        mSOneSpinner.setSelection(mSOneSpinnerAdapter.getPosition(S1.getSOne(mSOneText.getText().toString())));;
        mSTwoSpinner.setSelection(mSTwoSpinnerAdapter.getPosition(S2.getSTwo(mSTwoText.getText().toString())));;
        mChangeWithBreathingSpinner.setSelection(mChangeWithBreathingSpinnerAdapter.getPosition(ChangeWithBreathing.getChangeWithBreathing(mChangeWithBreathingText.getText().toString())));;
        mValsalvaSpinner.setSelection(mValsalvaSpinnerAdapter.getPosition(Valsalva.getValsalva(mValsalvaText.getText().toString())));;
        mLeftLateralPositionSpinner.setSelection(mLeftLateralPositionSpinnerAdapter.getPosition(LeftLateralPosition.getLeftLateralPosition(mLeftLateralPositionText.getText().toString())));;
        mSittingForwardSpinner.setSelection(mSittingForwardSpinnerAdapter.getPosition(SittingForward.getSittingForward(mSittingForwardText.getText().toString())));;
        //mFinalDiagnosisSpinner.setSelection(mFinalDiagnosisSpinnerAdapter.getPosition(FinalDiagnosis.getFinalDiagnosis(mFinalDiagnosisText.getText().toString())));;
    }

    private void setUpTextViews(){
        mPhaseOfCardiacCycleText = (TextView) mRootView.findViewById(R.id.phaseOfCardiacCycleText);
        //mIntensityText = (TextView) mRootView.findViewById(R.id.intensityText);
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
        //mFinalDiagnosisText = (TextView) mRootView.findViewById(R.id.finalDiagnosisText);
        //mCharacterText = (TextView) mRootView.findViewById(R.id.murmerDurationText);
        mSelectedCharacters = (TextView) mRootView.findViewById(R.id.selectedCharacters);
        mSelectedCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCharactersDialog(view);
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
        //mIntensitySpinner = (Spinner) mRootView.findViewById(R.id.intensitySpinner);
        //mIntensitySpinnerAdapter = new ArrayAdapter<Intensity>(getContext(), android.R.layout.simple_spinner_item, Intensity.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mIntensitySpinner.setAdapter(mIntensitySpinnerAdapter);

        //Murmer Duration
        mMurmurDurationSpinner = (Spinner) mRootView.findViewById(R.id.murmerDurationSpinner);
        mMurmerDurationSpinnerAdapter = new ArrayAdapter<MurmurDuration>(getContext(), android.R.layout.simple_spinner_item, MurmurDuration.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMurmurDurationSpinner.setAdapter(mMurmerDurationSpinnerAdapter);

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
        //mFinalDiagnosisSpinner = (Spinner) mRootView.findViewById(R.id.finalDiagnosisSpinner);
        mFinalDiagnosisSpinnerAdapter = new ArrayAdapter<FinalDiagnosis>(getContext(), android.R.layout.simple_spinner_item, FinalDiagnosis.values());
        //mPhaseOfCardiacCycleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mFinalDiagnosisSpinner.setAdapter(mFinalDiagnosisSpinnerAdapter);

        //Character Adapter Setup
        mMultiCharacterSelectorListAdapter = new MultiSelectorListAdapter(getContext(), Arrays.asList(CHARACTER.values()),selectedCharacters);
    }

    private void loadDefaultDataIntoViews(){

        mPhaseOfCardiacCycleText.setText(mPhaseOfCardiacCycleSpinner.getSelectedItem().toString());
        //mIntensityText.setText(mIntensitySpinner.getSelectedItem().toString());
        mMurmerDurationText.setText(mMurmurDurationSpinner.getSelectedItem().toString());
        mMostIntenseLocationText.setText(mMostIntenseLocationSpinner.getSelectedItem().toString());
        mRadiationText.setText(mRadiationSpinner.getSelectedItem().toString());
        mAddedSoundsText.setText(mAddedSoundsSpinner.getSelectedItem().toString());
        mSOneText.setText(mSOneSpinner.getSelectedItem().toString());
        mSTwoText.setText(mSTwoSpinner.getSelectedItem().toString());
        mChangeWithBreathingText.setText(mChangeWithBreathingSpinner.getSelectedItem().toString());
        mValsalvaText.setText(mValsalvaSpinner.getSelectedItem().toString());
        mLeftLateralPositionText.setText(mLeftLateralPositionSpinner.getSelectedItem().toString());
        mSittingForwardText.setText(mSittingForwardSpinner.getSelectedItem().toString());
        //mFinalDiagnosisText.setText(mFinalDiagnosisSpinner.getSelectedItem().toString());

    }

    private void displayCharactersDialog(View view){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View characterSelectorDialog = (View)inflater.inflate(R.layout.dialog_multi_selector, null);
        mCharacterSelector = (ListView)characterSelectorDialog.findViewById(R.id.multiSelectorList);
        Button selectAllButton = (Button) characterSelectorDialog.findViewById(R.id.selectAllButton);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCharacters.clear();
                selectedCharacters.addAll(Arrays.asList(CHARACTER.values()));
                mMultiCharacterSelectorListAdapter.notifyDataSetChanged();
            }
        });
        Button deselectAllButton = (Button) characterSelectorDialog.findViewById(R.id.deselectAllButton);
        deselectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCharacters.clear();
                mMultiCharacterSelectorListAdapter.notifyDataSetChanged();
            }
        });
        mCharacterSelector.setAdapter(mMultiCharacterSelectorListAdapter);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.multipleCharacterSelectorDialogTitle)
                .setCancelable(false)
                .setView(characterSelectorDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
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

    private void launchWebAPIErrorFragment(){
        Bundle bundle = new Bundle();
        bundle.putString(WebAPIErrorFragment.WEB_API_ERROR_MESSAGE_TAG, webAPIResponse.getMessage());
        WebAPIErrorFragment webAPIErrorFragment = WebAPIErrorFragment.newInstance();
        webAPIErrorFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, webAPIErrorFragment, WebAPIErrorFragment.WEB_API_ERROR_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean editModeEnabled(){
        return editMode;
    }

    //------------------------------------------------------
    //MurmurRatingAPI implementation
    @Override
    public void requestMurmurRatingsForHeartSound(int heartSoundId) {
        Toast.makeText(getContext(), getResources().getString(R.string.retrievingMurmurRating), Toast.LENGTH_SHORT).show();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, WebAPI.GET_MURMUR_RATING_FOR_HEART_SOUND_URL + heartSoundId, null,
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

    @Override
    public void requestMurmurRating(final int murmurRatingId) {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WebAPI.MURMUR_RATING_BASE_URL + murmurRatingId, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        murmurRating = JsonObjectParser.getMurmurRatingFromJsonString(response.toString());
                        setupViewsWithMurmurRatingData();
                        if(!murmurRating.getDoctorID().equalsIgnoreCase(SharedPreferencesManager.getActiveUserId(mParentActivity))){
                            mActionBarMenu.findItem(R.id.deleteItem).setVisible(false);
                        }
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
                    launchWebAPIErrorFragment();
                }
                //recreate the webAPIResponse object with default values
                webAPIResponse = new WebAPIResponse();
            }
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
    public void createMurmurRating(final MurmurRating murmurRating) throws JSONException {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JSONObject bodyParams = WebAPI.addCreateMurmurRatingParams(murmurRating);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.MURMUR_RATING_BASE_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        MurmurRatingFragment.this.murmurRating = JsonObjectParser.getMurmurRatingFromJsonString(response.toString());
                        Log.d("MurmurRatingID:",MurmurRatingFragment.this.murmurRating.getMurmurRatingID() + "");
                        Log.d("MurmurRatingID:",MurmurRatingFragment.this.murmurRating.toString());
                        selectedMurmerRatingPosition = ((PatientHeartSoundActivity)(getActivity())).addNewMurmurRatingToHeartSoundToMurmurRatingMap(MurmurRatingFragment.this.murmurRating.toString());
                        if(selectedMurmerRatingPosition <= 0){
                            getActivity().finish();
                        }
                        setupViewsWithMurmurRatingData();
                        ((PatientHeartSoundActivity)getActivity()).setupNavigationDrawerContent();
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
                    launchWebAPIErrorFragment();
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

    @Override
    public void updateMurmurRating(MurmurRating murmurRating) throws JSONException {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.updatingMurmurRating));
        JSONObject bodyParams = WebAPI.addUpdateMurmurRatingParams(murmurRating);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebAPI.MURMUR_RATING_BASE_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        setupViewsWithMurmurRatingData();
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
                    launchWebAPIErrorFragment();
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

    @Override
    public void deleteMurmurRating(final int murmurRatingId) {
        Toast.makeText(getContext(), getResources().getString(R.string.deletingMurmurRating), Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, WebAPI.MURMUR_RATING_BASE_URL + murmurRatingId, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        ((PatientHeartSoundActivity)getActivity()).deleteMurmurRatingFromHeartSoundToMurmurRatingMap(murmurRating);
                        getActivity().getSupportFragmentManager().popBackStack();
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {

                //update UI accordingly
                Toast.makeText(getContext(), getResources().getString(R.string.deletingMurmurRatingFailed), Toast.LENGTH_SHORT).show();
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
    //------------------------------------------------------
}
