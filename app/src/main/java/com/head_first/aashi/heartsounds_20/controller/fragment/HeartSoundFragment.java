package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeartSoundFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeartSoundFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartSoundFragment extends Fragment {
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
     *
     */

    public static final String HEART_SOUND_FRAGMENT_TAG = "HEART_SOUND_FRAGMENT";

    //Data
    private TextView mHeartSoundId;
    private TextView mDoctorDetails;
    private TextView mDeviceId;
    private Button mSaveHeartSoundButton;

    //Layout and View
    private View mRootView;

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
        mHeartSoundId = (TextView) mRootView.findViewById(R.id.heartSoundId);
        mDoctorDetails = (TextView) mRootView.findViewById(R.id.doctorDetails);
        mDeviceId = (TextView) mRootView.findViewById(R.id.deviceId);
        mSaveHeartSoundButton = (Button) mRootView.findViewById(R.id.saveHeartSoundButton);
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
        //if the user is the creator of this HeartSound then display the edit menuitem
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
}
