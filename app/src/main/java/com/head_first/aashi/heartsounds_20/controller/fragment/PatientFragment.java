package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.enums.Gender;
import com.head_first.aashi.heartsounds_20.utils.MultiSelectorListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    /**
     * Display share patient option at the top
     * Number of doctors that can see the patient (Shared with = number of doctors)
     * Number of Students that have access to this patient.
     * Display number of heartsounds
     *
     * Use the DatePicker (tutorial saved as bookmark) for date of birth
     */

    public static final String PATIENT_FRAGMENT_TAG = "PATIENT_FRAGMENT";

    //Data
    private boolean editMode;
    private String dateOfBirthString;
    private List<String> selectedStudy; //this will be deleted. Instead of this the List<CHARACTER> from the MurmerRating object will be used

    //View
    private Menu mActionBarMenu;
    private View mRootView;
    private TextView mPatientId;
    private TextView mDoctorDetails;
    private TextView mDateOfBirth;
    private TextView mGender;
    private TextView mStudyList;
    private Switch mVisibleToStudents;
    private TextView mVisibleToUsersCount;
    private Button mSavePatientButton;
    private ListView mStudyListSelector;
    private RadioGroup mGenderRadioGroup;

    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;

    //Adapter
    private MultiSelectorListAdapter mMultiStudySelectorListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PatientFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PatientFragment newInstance(String param1, String param2) {
        PatientFragment fragment = new PatientFragment();
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
        selectedStudy = new ArrayList<>();//delete this later
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_patient, container, false);
        mPatientId = (TextView) mRootView.findViewById(R.id.patientId);
        mDoctorDetails = (TextView) mRootView.findViewById(R.id.doctorDetails);
        mDateOfBirth = (TextView) mRootView.findViewById(R.id.dateOfBirth);
        mDateOfBirth.setClickable(false);
        mGender = (TextView) mRootView.findViewById(R.id.genderText);
        mStudyList = (TextView) mRootView.findViewById(R.id.studyList);
        mVisibleToStudents = (Switch) mRootView.findViewById(R.id.visibleTOStudents);
        mVisibleToUsersCount = (TextView) mRootView.findViewById(R.id.visibleTOUsersCount);
        mSavePatientButton = (Button) mRootView.findViewById(R.id.savePatientButton);

        //if dateOfBirth has been set for the patient then initialize dateOfBirthString with that value
        //else
        dateOfBirthString = getResources().getString(R.string.dateOfBirth);
        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerDialogFragment();
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
        mMultiStudySelectorListAdapter = new MultiSelectorListAdapter(getContext(), Arrays.asList(new String[]{"Study1","Study2","Study3","Study4","Study5"}),selectedStudy);
        makeViewsUneditable();
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

    private void showActionBarMenuItems(){
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.sharePatientItem:
                break;
            case R.id.deletePatientItem:
                break;
            case R.id.editItem:
                editUserProfile();
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

    private void editUserProfile(){
        editMode = true;
        makeViewsEditable();
        showActionBarMenuItems();
    }

    private void saveChanges(){
        editMode = false;
        //Copy the data from the views into the models

        makeViewsUneditable();
        showActionBarMenuItems();
    }

    public void cancelChanges(){
        editMode = false;
        //Copy the data from the models into the Views

        makeViewsUneditable();
        showActionBarMenuItems();
    }

    private void makeViewsEditable(){
        if(editMode){
            mGender.setClickable(true);
            mStudyList.setClickable(true);
            mVisibleToStudents.setClickable(true);
        }
    }

    private void makeViewsUneditable(){
        if(!editMode){
            mGender.setClickable(false);
            mStudyList.setClickable(false);
            mVisibleToStudents.setClickable(false);
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

        mStudyListSelector.setAdapter(mMultiStudySelectorListAdapter);
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.multipleStudySelectorDialogTitle)
                .setCancelable(false)
                .setView(studySelectorDialog)
                .setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
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

    private void displayGenderRadioGroupDialog(View view){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View genderSelectorDialog = (View)inflater.inflate(R.layout.dialog_radio_group, null);
        mGenderRadioGroup = (RadioGroup) genderSelectorDialog.findViewById(R.id.radioGroup);
        setupGenderRadioButtons();
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.genderSelectorDialogTitle)
                .setCancelable(false)
                .setView(genderSelectorDialog)
                .setPositiveButton(R.string.positiveButton, new DialogInterface.OnClickListener() {
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

    public boolean editModeEnabled(){
        return this.editMode;
    }
}
