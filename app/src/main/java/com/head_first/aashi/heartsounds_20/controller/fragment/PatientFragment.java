package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.enums.Gender;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.util_interfaces.NavgigationDrawerUtils;
import com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces.PatientAPI;
import com.head_first.aashi.heartsounds_20.model.Patient;
import com.head_first.aashi.heartsounds_20.model.User;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;
import com.head_first.aashi.heartsounds_20.utils.MultiSelectorListAdapter;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends EditableFragment implements DatePickerDialog.OnDateSetListener, PatientAPI {
    /**
     * if the Patient object in the parent activity is null -> launch the fragment in edit mode
     *
     * if save changes is clicked and the patient object (or even the id) in the parent activity is null
     *      -> Use the POST HTTP method and create a Patient
     * else -> Use the PUT HTTP method to update the Patient
     *
     * if cancel changes is clicked and the patient object (or even the id) in the parent activity is null
     *      -> Pop off the fragment/finish the activity
     * else -> Normal flow of not saving changes
     */

    public static final String PATIENT_FRAGMENT_TAG = "PATIENT_FRAGMENT";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    //Data
    private List<String> selectedStudy; //this will be deleted. Instead of this the List<CHARACTER> from the MurmerRating object will be used

    //View
    private Menu mActionBarMenu;
    private View mRootView;
    private TextView mPatientId;
    private TextView mDoctorDetails;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mDateOfBirth;
    private TextView mGender;
    private TextView mStudyList;
    private Switch mVisibleToStudents;
    private TextView mVisibleToUsersCount;
    private Button mSavePatientButton;
    private ListView mStudyListSelector;
    private RadioGroup mGenderRadioGroup;
    private ListView mDoctorListSelector;

    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;

    //Adapter
    private MultiSelectorListAdapter mMultiStudySelectorListAdapter;
    private MultiSelectorListAdapter mDoctorListSelectorAdapter;

    //Data
    private List<User> selectedDoctors;
    private List<User> allDoctors;

    //Web API
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment PatientFragment.
     */
    public static PatientFragment newInstance() {
        PatientFragment fragment = new PatientFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        selectedStudy = new ArrayList<>();//delete this later
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_patient, container, false);
        mPatientId = (TextView) mRootView.findViewById(R.id.patientId);
        mDoctorDetails = (TextView) mRootView.findViewById(R.id.doctorDetails);
        mFirstName = (TextView) mRootView.findViewById(R.id.firstName);
        mLastName = (TextView) mRootView.findViewById(R.id.lastName);
        mDateOfBirth = (TextView) mRootView.findViewById(R.id.dateOfBirth);
        //mDateOfBirth.setClickable(false);
        mGender = (TextView) mRootView.findViewById(R.id.genderText);
        mStudyList = (TextView) mRootView.findViewById(R.id.studyList);
        mVisibleToStudents = (Switch) mRootView.findViewById(R.id.visibleTOStudents);
        mVisibleToUsersCount = (TextView) mRootView.findViewById(R.id.visibleTOUsersCount);
        mSavePatientButton = (Button) mRootView.findViewById(R.id.savePatientButton);

        //if dateOfBirth has been set for the patient then initialize dateOfBirthString with that value
        //else
        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerDialogFragment();
            }
        });
        mFirstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFirstNameClicked();
            }
        });
        mLastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLastNameClicked();
            }
        });
        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayGenderRadioGroupDialog(v);
            }
        });
        mStudyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayStudyListDialog(view);
            }
        });
        //mMultiStudySelectorListAdapter = new MultiSelectorListAdapter(getContext(), Arrays.asList(new String[]{"Study1","Study2","Study3","Study4","Study5"}),selectedStudy);
        //If Patient object is not null copy data from the Patient object into the views
        setupViewsWithPatientData();
        makeViewsUneditable();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume(){
        ((PatientHeartSoundActivity)getActivity()).setActionBarTitle(PatientHeartSoundActivity.PATIENT);
        ((PatientHeartSoundActivity)getActivity()).setupNavigationDrawerContent();
        super.onResume();
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
                        menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.cancelChangesItemText)))){
                    //if() user id matches the CreatedBy user id then display the edit button
                    //mActionBarMenu.findItem(R.id.editItem).setVisible(false);
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }
            }
        }
        Patient patient = ((PatientHeartSoundActivity)getActivity()).getPatient();
        if(patient != null){
            if(!patient.getPrimaryDoctorId().equalsIgnoreCase(SharedPreferencesManager.getActiveUserId(getActivity()))){
                mActionBarMenu.findItem(R.id.deleteItem).setVisible(false);
                mActionBarMenu.findItem(R.id.sharePatientItem).setVisible(false);
            }
        }
        mActionBarMenu.findItem(R.id.refreshViewItem).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.sharePatientItem:
                displayDoctorListDialog();
                break;
            case R.id.deleteItem:
                confirmPatientDeletion();
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

    private void confirmPatientDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getResources().getString(R.string.deletePatient));
        builder.setMessage(getContext().getResources().getString(R.string.confirmationMesage));
        builder.setPositiveButton(getContext().getResources().getString(R.string.positiveButtonYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePatient((int)((PatientHeartSoundActivity)getActivity()).getPatient().getPatientId());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getContext().getResources().getString(R.string.negativeButtonNo), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void editFragment(){
        editMode = true;
        makeViewsEditable();
        showActionBarMenuItems();
        ((NavgigationDrawerUtils)getActivity()).disableNavigationMenu();
    }

    public Patient getUpdatedDataFromViewToModel() throws ParseException {
        Patient patient = ((PatientHeartSoundActivity)getActivity()).getPatient();
        if(patient == null){
            patient = new Patient(SharedPreferencesManager.getActiveUserId(getActivity()));
        }
        patient.setDateOfBirth((new SimpleDateFormat(DATE_FORMAT)).parse(mDateOfBirth.getText().toString()));
        patient.setGender(mGender.getText().toString());
        patient.setFirstName(mFirstName.getText().toString());
        patient.setLastName(mLastName.getText().toString());
        return patient;
    }

    @Override
    protected void saveChanges(){
        try {
            //Copy the data from the views into the models
            Patient patient = getUpdatedDataFromViewToModel();
            editMode = false;
            makeViewsUneditable();
            showActionBarMenuItems();
            ((NavgigationDrawerUtils)getActivity()).enableNavigationMenu();
            if(((PatientHeartSoundActivity)getActivity()).getPatient() == null){
                createPatient(patient);
            }
            else{
                updatePatient(patient);
            }
            mDateOfBirth.setError(null);
            mDateOfBirth.clearFocus();
        } catch (ParseException e) {
            //Incorrect date was entered show error and do not save
            mDateOfBirth.setError(getResources().getString(R.string.incorrectDateOfBirthMessage));
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelChanges(){
        if(((PatientHeartSoundActivity)(getActivity())).getPatient() == null){
            getActivity().finish();
        }
        editMode = false;
        //Copy the data from the models into the Views
        setupViewsWithPatientData();
        setupViewsWithPatientData();
        makeViewsUneditable();
        showActionBarMenuItems();
        ((NavgigationDrawerUtils)getActivity()).enableNavigationMenu();
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
    protected void makeViewsEditable(){
        if(editMode){
            mFirstName.setClickable(true);
            mLastName.setClickable(true);
            mGender.setClickable(true);
            mStudyList.setClickable(true);
            mVisibleToStudents.setClickable(true);
            mDateOfBirth.setClickable(true);
        }
    }

    @Override
    protected void makeViewsUneditable(){
        if(!editMode){
            mFirstName.setClickable(false);
            mLastName.setClickable(false);
            mGender.setClickable(false);
            mStudyList.setClickable(false);
            mVisibleToStudents.setClickable(false);
            mDateOfBirth.setClickable(false);
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        if(year > mCalendar.get(Calendar.YEAR)){
            mDateOfBirth.setText(mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR));
        }
        else if (year == mCalendar.get(Calendar.YEAR)){
            if(month > (mCalendar.get(Calendar.MONTH))){
                mDateOfBirth.setText(mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR));
            }
            else if(month == (mCalendar.get(Calendar.MONTH))){
                if(dayOfMonth > mCalendar.get(Calendar.DAY_OF_MONTH)){
                    mDateOfBirth.setText(mCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.YEAR));
                }
                else{
                    mDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }
            else {
                mDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }
        else{
            mDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        }
    }

    public void setupViewsWithPatientData(){
        Patient patient = ((PatientHeartSoundActivity)getActivity()).getPatient();
        if(patient == null){
            editMode = true;
            makeViewsEditable();
        }
        else{
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            mPatientId.setText(patient.getPatientId() + "");
            mDoctorDetails.setText(((PatientHeartSoundActivity)getActivity()).getUserName(patient.getPrimaryDoctorId()));
            mFirstName.setText(patient.getFirstName());
            mLastName.setText(patient.getLastName());
            mDateOfBirth.setText(dateFormat.format(patient.getDateOfBirth()).toString());
            mGender.setText(Gender.getGender(patient.getGender()).toString());
        }
    }

    private void displayDatePickerDialogFragment(){
        if(mDateOfBirth.isClickable()){
            mCalendar = Calendar.getInstance();
            //If date of birth is set for the patient then use that
            //else
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            mDatePickerDialog = new DatePickerDialog(getContext(), this, year, month, day);
            mDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            mDatePickerDialog.show();
        }
    }

    private void displayStudyListDialog(View view){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View studySelectorDialog = (View)inflater.inflate(R.layout.dialog_multi_selector, null);
        mStudyListSelector = (ListView) studySelectorDialog.findViewById(R.id.multiSelectorList);
        Button selectAllButton = (Button) studySelectorDialog.findViewById(R.id.selectAllButton);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStudy.clear();
                selectedStudy.addAll(Arrays.asList(new String[]{"Study1","Study2","Study3","Study4","Study5"}));
                mMultiStudySelectorListAdapter.notifyDataSetChanged();
            }
        });
        Button deselectAllButton = (Button) studySelectorDialog.findViewById(R.id.deselectAllButton);
        deselectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStudy.clear();
                mMultiStudySelectorListAdapter.notifyDataSetChanged();
            }
        });
        mStudyListSelector.setAdapter(mMultiStudySelectorListAdapter);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.multipleStudySelectorDialogTitle)
                .setCancelable(false)
                .setView(studySelectorDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(selectedStudy.isEmpty()){
                            mStudyList.setText(R.string.selectedCharacters);
                        }
                        else{
                            mStudyList.setText(selectedStudy.toString());
                        }
                    }
                })
                .create()
                .show();

    }

    private void displayDoctorListDialog(){
        if(allDoctors == null){
            allDoctors = ((PatientHeartSoundActivity)getActivity()).getAllUserObjects();
        }
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View doctorSelectorDialog = inflater.inflate(R.layout.dialog_multi_selector, null);
        mDoctorListSelector = (ListView) doctorSelectorDialog.findViewById(R.id.multiSelectorList);
        mDoctorListSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setChecked(!checkBox.isChecked());
                if(checkBox.isChecked()){
                    selectedDoctors.add(allDoctors.get(position));
                }
                else{
                    selectedDoctors.remove(allDoctors.get(position));
                }
            }
        });
        List<User> allOtherDoctors = new ArrayList<>();
        for(User aUser : allDoctors){
            if(!aUser.getId().equalsIgnoreCase(SharedPreferencesManager.getActiveUserId(getActivity()))){
                allOtherDoctors.add(aUser);
            }
        }
        mDoctorListSelectorAdapter = new MultiSelectorListAdapter(getContext(),allOtherDoctors, selectedDoctors = new ArrayList<>());
        mDoctorListSelector.setAdapter(mDoctorListSelectorAdapter);
        Button selectAllButton = (Button) doctorSelectorDialog.findViewById(R.id.selectAllButton);
        selectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDoctors.clear();
                selectedDoctors.addAll(allDoctors);
                mDoctorListSelectorAdapter.notifyDataSetChanged();
            }
        });
        Button deselectAllButton = (Button) doctorSelectorDialog.findViewById(R.id.deselectAllButton);
        deselectAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDoctors.clear();
                mDoctorListSelectorAdapter.notifyDataSetChanged();
            }
        });
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.multipleDoctorSelectorDialogTitle)
                .setCancelable(false)
                .setView(doctorSelectorDialog)
                .setPositiveButton(R.string.positiveButtonShare, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!selectedDoctors.isEmpty()){
                            Toast.makeText(getContext(), getResources().getString(R.string.sharingPatient), Toast.LENGTH_SHORT).show();
                        }
                        //share Patient
                        for(User aDoctor : selectedDoctors){
                            try {
                                sharePatient(aDoctor.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.negativeButtonHide, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!selectedDoctors.isEmpty()){
                            Toast.makeText(getContext(), getResources().getString(R.string.unsharingPatient), Toast.LENGTH_SHORT).show();
                        }
                        //hide Patient
                        for(User aDoctor : selectedDoctors){
                            try {
                                unSharePatient(aDoctor.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .setNeutralButton(R.string.negativeButtonCancel, null)
                .create()
                .show();

    }

    private void onFirstNameClicked(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View enterTextDialog = (View)inflater.inflate(R.layout.dialog_enter_text, null);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.firstNameLabel)
                .setCancelable(true)
                .setView(enterTextDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText enteredText = (EditText) enterTextDialog.findViewById(R.id.enteredText);
                        mFirstName.setText(enteredText.getText());
                    }
                })
                .setNegativeButton(R.string.negativeButtonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void onLastNameClicked(){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View enterTextDialog = (View)inflater.inflate(R.layout.dialog_enter_text, null);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.lastNameLabel)
                .setCancelable(true)
                .setView(enterTextDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText enteredText = (EditText) enterTextDialog.findViewById(R.id.enteredText);
                        mLastName.setText(enteredText.getText());
                    }
                })
                .setNegativeButton(R.string.negativeButtonCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void displayGenderRadioGroupDialog(View view){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View genderSelectorDialog = (View)inflater.inflate(R.layout.dialog_radio_group, null);
        mGenderRadioGroup = (RadioGroup) genderSelectorDialog.findViewById(R.id.radioGroup);
        setupGenderRadioButtons();
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.genderSelectorDialogTitle)
                .setCancelable(false)
                .setView(genderSelectorDialog)
                .setPositiveButton(R.string.positiveButtonOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RadioButton selectedGender = (RadioButton) mGenderRadioGroup.findViewById(mGenderRadioGroup.getCheckedRadioButtonId());
                        if(selectedGender == null){
                            mGender.setText(R.string.gender);
                        }
                        else{
                            mGender.setText(selectedGender.getText().toString());
                        }
                    }
                })
                .create()
                .show();

    }

    private void setupGenderRadioButtons(){
        if(mGenderRadioGroup != null){
            Gender[] genderArray = Gender.values();
            for(int i = 0; i < genderArray.length; i++){
                RadioButton radioButton = new RadioButton(getContext());
                //this will be done using the Patient class later
                if(mGender.getText().toString().toLowerCase().contains(genderArray[i].toString().toLowerCase())){
                    radioButton.setChecked(true);
                }
                radioButton.setText(genderArray[i].toString());
                radioButton.setId(i);
//                radioButton.setScaleX(0.75f);
//                radioButton.setScaleY(0.75f);
//                RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
//                        0, RadioGroup.LayoutParams.WRAP_CONTENT, 1.0f);
//                radioButton.setLayoutParams(layoutParams);
                mGenderRadioGroup.addView(radioButton);
            }
        }
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
        return this.editMode;
    }

    //--------------------------------------------------------------
    //Implementation for PatientAPI
    @Override
    public void sharePatient(String doctorId) throws JSONException {
        //Toast.makeText(getContext(), getResources().getString(R.string.sharingPatient), Toast.LENGTH_SHORT).show();
        JSONObject bodyParams = WebAPI.addShareUnsharePatientParams(((int)((PatientHeartSoundActivity)getActivity()).getPatient().getPatientId()), doctorId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.ADD_DOCTOR_TO_PATIENT_URL, bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

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
    public void unSharePatient(String doctorId) throws JSONException {
        //Toast.makeText(getContext(), getResources().getString(R.string.unsharingPatient), Toast.LENGTH_SHORT).show();
        JSONObject bodyParams = WebAPI.addShareUnsharePatientParams(((int)((PatientHeartSoundActivity)getActivity()).getPatient().getPatientId()), doctorId);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.REMOVE_DOCTOR_FROM_PATIENT_URL , bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
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
    public void requestPatient(int patientId) {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.retrievingPatientDetails));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WebAPI.PATIENT_BASE_URL + "{Place Patient id here}" , null,
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
    public void requestPatients() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createPatient(Patient patient) throws JSONException {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JSONObject bodyParams = WebAPI.addCreatePatientParams(patient);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.PATIENT_BASE_URL , bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        ((PatientHeartSoundActivity)(getActivity())).setPatient(JsonObjectParser.getPatientFromJsonString(response.toString()));
                        setupViewsWithPatientData();

                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
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
    public void updatePatient(final Patient patient) throws JSONException {
        Toast.makeText(getContext(), getResources().getString(R.string.updatingPatient), Toast.LENGTH_SHORT).show();
        JSONObject bodyParams = WebAPI.addUpdatePatientParams(patient);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, WebAPI.PATIENT_BASE_URL , bodyParams,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        setupViewsWithPatientData();
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                        Toast.makeText(getContext(), getResources().getString(R.string.patientUpdated), Toast.LENGTH_SHORT).show();
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
    public void deletePatient(int patientId) {
        Toast.makeText(getContext(), getResources().getString(R.string.deletingPatient), Toast.LENGTH_SHORT).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, WebAPI.PATIENT_BASE_URL + patientId , null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        Toast.makeText(getContext(), getResources().getString(R.string.patientDeleted), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly

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
    public void getAllUsers() {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, WebAPI.GET_ALL_USERS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
//                        Collection<Patient> parsedResponse = JsonObjectParser.getPatientListFromJsonString(response.toString());
//                        if(parsedResponse != null){
//                            patientList = new ArrayList<>(parsedResponse);
//                            if(displayState == PatientListFragment.DISPLAY_STATE.LIST_VIEW){
//                                setupListView();
//                            }
//                            else{
//                                setupExpandableListView();
//                            }
//                        }
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
    //--------------------------------------------------------------
}
