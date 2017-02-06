package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.EditableFragment;
import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends EditableFragment {
    public static final String USER_PROFILE_FRAGMENT_TAG = "USER_PROFILE_FRAGMENT";
    private static final String PROFILE_PAGE_TITLE = "User Profile";

    //Layouts and views
    private Menu mActionBarMenu;
    private View mRootView;
    private TextView mFirstNameText;
    private TextView mLastNameText;
    private TextView mUserNameText;
    private TextView mLogoutButtonText;
    private EditText mEditableFirstName;
    private EditText mEditableLastName;

    //Data

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
        mRootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mFirstNameText = (TextView) mRootView.findViewById(R.id.firstNameText);
        mLastNameText = (TextView) mRootView.findViewById(R.id.lastNameText);
        mUserNameText = (TextView) mRootView.findViewById(R.id.userNameText);
        mEditableFirstName = (EditText) mRootView.findViewById(R.id.editableFirstName);
        mEditableLastName = (EditText) mRootView.findViewById(R.id.editableLastName);
        mLogoutButtonText = (Button) mRootView.findViewById(R.id.logoutButton);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_user_patient_tool_bar_items, menu);
        mActionBarMenu = menu;
        showActionBarMenuItems();
//        menu.findItem(R.id.filterPatientsItem).setVisible(false);
//        menu.findItem(R.id.addPatientItem).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
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

    @Override
    protected void editUserProfile(){
        editMode = true;
        hideNonEditableViews();
        showEditableViews();
        showActionBarMenuItems();
        copyDataFromTextViewToEditText();
    }

    @Override
    protected void saveChanges(){
        editMode = false;
        //Copy data into the models and make a PUT request to update the database

        saveChangesFromEditText();
        hideEditableViews();
        showNonEditableViews();
        showActionBarMenuItems();
    }

    @Override
    public void cancelChanges(){
        editMode = false;
        hideEditableViews();
        showNonEditableViews();
        showActionBarMenuItems();
    }

    @Override
    protected void showEditableViews(){
        if(editMode){
            mEditableFirstName.setVisibility(View.VISIBLE);
            mEditableLastName.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void showNonEditableViews(){
        if(!editMode){
            mFirstNameText.setVisibility(View.VISIBLE);
            mLastNameText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void hideNonEditableViews(){
        if(editMode){
            mFirstNameText.setVisibility(View.GONE);
            mLastNameText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void hideEditableViews(){
        if(!editMode){
            mEditableFirstName.setVisibility(View.GONE);
            mEditableLastName.setVisibility(View.GONE);
        }
    }

    private void copyDataFromTextViewToEditText(){
        mEditableFirstName.setText(mFirstNameText.getText().toString());
        mEditableLastName.setText(mLastNameText.getText().toString());
    }


    private void saveChangesFromEditText(){
        //Save the changes made to the EditText in the models
        //after that copy the same to the TextViews
        mFirstNameText.setText(mEditableFirstName.getText().toString());
        mLastNameText.setText(mEditableLastName.getText().toString());
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
                //if menu item is edit or refresh
                if(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.editItemText)) ||
                        menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.refreshViewItemText))){
                    menuItem.setVisible(true);
                }
                else{
                    menuItem.setVisible(false);
                }
            }
        }
    }

    @Override
    public boolean editModeEnabled(){
        return this.editMode;
    }

    @Override
    protected void makeViewsEditable() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void makeViewsUneditable(){
        throw new UnsupportedOperationException();
    }
}
