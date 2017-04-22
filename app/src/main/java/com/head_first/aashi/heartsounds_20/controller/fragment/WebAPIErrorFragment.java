package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WebAPIErrorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WebAPIErrorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebAPIErrorFragment extends Fragment {
    public static final String WEB_API_ERROR_FRAGMENT_TAG = "WEB_API_ERROR_FRAGMENT_TAG";
    public static final String WEB_API_ERROR_MESSAGE_TAG = "WEB_API_ERROR_MESSAGE_TAG";

    //Layout and Views
    private View mRootView;
    private TextView mErrorMessage;

    //Data
    private String message;

    private OnFragmentInteractionListener mListener;

    public WebAPIErrorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment WebAPIErrorFragment.
     */
    public static WebAPIErrorFragment newInstance() {
        WebAPIErrorFragment fragment = new WebAPIErrorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments() != null){
            message = getArguments().getString(WEB_API_ERROR_MESSAGE_TAG);
        }
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_web_apierror, container, false);
        mErrorMessage = (TextView) mRootView.findViewById(R.id.errorMessageText);
        if(message == null || message.isEmpty()){
            mErrorMessage.setText(getResources().getString(R.string.errorMessage));
        }
        else{
            mErrorMessage.setText(message);
        }
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
}
