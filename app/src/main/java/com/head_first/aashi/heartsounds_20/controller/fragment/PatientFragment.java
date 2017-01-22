package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.enums.Radiation;

import java.nio.channels.CancelledKeyException;
import java.util.Calendar;
import java.util.Date;

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

    //Data
    private String dateOfBirthString;

    //View
    private View mRootView;
    private TextView mPatientId;
    private TextView mDoctorDetails;
    private RadioGroup mGender;
    private TextView mDateOfBirth;
    private Switch mVisibleToStudents;
    private TextView mVisibleToUsersCount;
    private Button mSavePatientButton;

    private DatePickerDialog mDatePickerDialog;
    private Calendar mCalendar;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_patient, container, false);
        mPatientId = (TextView) mRootView.findViewById(R.id.patientId);
        mDoctorDetails = (TextView) mRootView.findViewById(R.id.doctorDetails);
        mGender = (RadioGroup) mRootView.findViewById(R.id.gender);
        mDateOfBirth = (TextView) mRootView.findViewById(R.id.dateOfBirth);
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
