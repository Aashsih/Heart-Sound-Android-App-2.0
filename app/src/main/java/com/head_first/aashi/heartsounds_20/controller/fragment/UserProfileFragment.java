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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.web_api_interfaces.UserAPI;
import com.head_first.aashi.heartsounds_20.model.Doctor;
import com.head_first.aashi.heartsounds_20.model.User;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;

import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends EditableFragment implements UserAPI{
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
    private User loggedInUser;

    //Web API
    private WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment UserProfileFragment.
     */
    public static UserProfileFragment newInstance() {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initializeViews();
        //requestUserDetails();
        return mRootView;
    }

    private void initializeViews(){
        mFirstNameText = (TextView) mRootView.findViewById(R.id.firstNameText);
        mLastNameText = (TextView) mRootView.findViewById(R.id.lastNameText);
        mUserNameText = (TextView) mRootView.findViewById(R.id.userNameText);
        mEditableFirstName = (EditText) mRootView.findViewById(R.id.editableFirstName);
        mEditableLastName = (EditText) mRootView.findViewById(R.id.editableLastName);
        //mLogoutButtonText = (Button) mRootView.findViewById(R.id.logoutButton);
    }

    private void logoutUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getResources().getString(R.string.logoutTitle));
        builder.setMessage(getContext().getResources().getString(R.string.confirmationMesage));
        builder.setPositiveButton(getContext().getResources().getString(R.string.positiveButtonYes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesManager.invalidateUserAccessToken(getActivity());
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
    public void onResume(){
        requestUserDetails();
        super.onResume();
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
            case R.id.refreshViewItem:
                requestUserDetails();
                break;
            case R.id.logoutItem:
                logoutUser();
                break;

            case R.id.editItem:
                editUserProfile();
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

    //Editable Fragment Method Implementations
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
                //if(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.editItemText)) ||
                if(menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.refreshViewItemText))
                        || menuItem.getTitle().toString().equalsIgnoreCase(getString(R.string.logoutItemText))){
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

    //---------------------------------------------------------------------
    //User API Implementation and helper methods
    @Override
    public void requestUserDetails() {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WebAPI.GET_USER_INFO_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        //1. get user object
                        loggedInUser = JsonObjectParser.getUserFromJsonString(response.toString());
                        //2. update UI
                        mUserNameText.setText(loggedInUser.getUsername());
                        mFirstNameText.setText(loggedInUser.getFirstName());
                        mLastNameText.setText(loggedInUser.getLastName());
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
    public void changeUserPassword(String oldPassword, String newPassword) {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity(), getResources().getString(R.string.retrievingUserDetails));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, WebAPI.CHANGE_USER_PASSWORD_URL, null,
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
            protected Map<String, String> getParams() {
                return WebAPI.addChangePasswordParams(null, null, null);
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
    public void registerUser(Doctor doctor) {

    }
    //---------------------------------------------------------------------
}
