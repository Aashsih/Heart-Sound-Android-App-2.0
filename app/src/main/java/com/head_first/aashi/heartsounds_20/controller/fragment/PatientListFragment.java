package com.head_first.aashi.heartsounds_20.controller.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.activities.PatientHeartSoundActivity;
import com.head_first.aashi.heartsounds_20.controller.activities.UserPatientActivity;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.model.Filter;
import com.head_first.aashi.heartsounds_20.model.Patient;
import com.head_first.aashi.heartsounds_20.model.User;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.DynamicSearchFilter;
import com.head_first.aashi.heartsounds_20.utils.ExpandablePatientListAdapter;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PatientListFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener{
    public static final String PATIENT_LIST_FRAGMENT_TAG = "PATIENT_LIST_FRAGMENT";
    public static final String MY_PATIENT_SELECTED = "MY_PATIENT_SELECTED";
    private enum DISPLAY_STATE {
        LIST_VIEW (1), EXPANDABLE_LIST_VIEW (2);
        private int value;
        private DISPLAY_STATE(int value){
            this.value = value;
        }
    };
    private static final String MY_PATIENTS_PAGE_TITLE = "My Patients";
    private static final String SHARED_PATIENTS_PAGE_TITLE = "Shared Patients";
    private static final String DEFAULT_SEARCH_STRING = "";

    //Views and Layouts
    private DialogFragment filterFragment;
    private View mRootView;
    private SearchView mSearchView;
    private ListView mListView;
    private ArrayAdapter<Patient> patientListAdapter;
    private ExpandableListView mExpandableListView;
    private ExpandablePatientListAdapter expandablePatientListAdapter;

    //Data for the fragment
    private List<Patient> allPatients;
    private List<Patient> filteredPatients;
    private DISPLAY_STATE displayState;
    private Filter filter;
    private String oldFilterString; //this is made equal to the filter String from filter.getSearchString() before the view of this fragment is destroyed
    //There will be a few more data structures here that will store the information from the Database, more specifically
    //Information about Patient and Study.
    //The Study model along with Patient model will be used to create the HashMap<String, Patient> in the Filter Model
    //That HashMap will have all the information that the app requires based on the FilterDialogFragment.
    //Another HashMap will be created from the previous one which will contain all the information based on the
    //search filter in this fragment.

    //This Map gets it information from the filterContent of the Filter Model
    //it is basially its replica apart from the fact that the groupItems here are String instead of Filter.GroupItem
    private LinkedHashMap<String, List<String>> filterContentMap;
    private LinkedHashMap<String, List<String>> searchContentMap;
    private boolean myPatientClicked;

    //Web API
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    private OnFragmentInteractionListener mListener;

    public PatientListFragment() {
        filter = new Filter(DEFAULT_SEARCH_STRING);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment PatientListFragment.
     */
    public static PatientListFragment newInstance() {
        PatientListFragment fragment = new PatientListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        oldFilterString = "null";
        displayState = DISPLAY_STATE.LIST_VIEW;
        myPatientClicked = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!oldFilterString.equals(filter.getSearchString())){
            setUpDataForViews();
        }
        if(getArguments() != null){
            myPatientClicked = getArguments().getBoolean(MY_PATIENT_SELECTED);
        }

        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_patients_list, container, false);
        if(myPatientClicked){

        }
        else{

        }
        //SearchView Setup
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) mRootView.findViewById(R.id.patientSearchView);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnCloseListener(this);

        setHasOptionsMenu(true);
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(myPatientClicked){
            ((UserPatientActivity)getActivity()).setTitle(UserPatientActivity.MY_PATIENTS_PAGE_TITLE);
        }
        requestUserDetails();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        oldFilterString = filter.getSearchString();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_user_patient_tool_bar_items, menu);
        //if the Shared Patients is selected then set add patient button to false
        if(!myPatientClicked){
            menu.findItem(R.id.addPatientItem).setVisible(false);
        }
        menu.findItem(R.id.editItem).setVisible(false);
        menu.findItem(R.id.saveChangesItem).setVisible(false);
        menu.findItem(R.id.cancelChangesItem).setVisible(false);
        menu.findItem(R.id.logoutItem).setVisible(false);
        menu.findItem(R.id.filterPatientsItem).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handling item selection
        switch (item.getItemId()) {
            case R.id.addPatientItem:
                launchPatientHeartSoundActivityForNewPatient();
                break;
            case R.id.filterPatientsItem:
                launchFilterFragment();
                break;
            case R.id.refreshViewItem:
                requestPatients();
                break;
        }
        return true;
    }


    public void setMyPatientClicked(boolean myPatientClicked){
        this.myPatientClicked = myPatientClicked;
    }

    public boolean isMyPatientClicked(){
        return this.myPatientClicked;
    }

    private void setupExpandableListView(){
        //This is not in use at the moment
        //Setup ExpandableListView
        expandablePatientListAdapter = new ExpandablePatientListAdapter(getContext(), searchContentMap);
        mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.expandableListView);
        mExpandableListView.setAdapter(expandablePatientListAdapter);
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                onPatientSelected(parent, v, groupPosition, childPosition, id);
                return false;
            }
        });
    }

    private void setupListView(){
        if(myPatientClicked){
            getMyPatientList();
            patientListAdapter = new ArrayAdapter<Patient>(getContext(), android.R.layout.simple_list_item_1, filteredPatients);
        }
        else{
            getSharedPatientList();
            patientListAdapter = new ArrayAdapter<Patient>(getContext(), android.R.layout.simple_list_item_1, filteredPatients);
        }
        mListView = (ListView) mRootView.findViewById(R.id.listView);
        mListView.setAdapter(patientListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPatientSelected(parent, view, position, id);
            }
        });
    }

    private List<Patient> getMyPatientList(){
        List<Patient> myPatientList = new ArrayList<>();
        if(filteredPatients == null){
            filteredPatients = new ArrayList<>();
        }
        else{
            filteredPatients.clear();
        }
        String userId = SharedPreferencesManager.getActiveUserId(getActivity());
        for(Patient aPatient : allPatients){
            if(aPatient.getPrimaryDoctorId().equalsIgnoreCase(userId)){
                myPatientList.add(aPatient);
                filteredPatients.add(aPatient);
            }
        }
        return myPatientList;
    }

    private List<Patient> getSharedPatientList(){
        List<Patient> sharedPatientList = new ArrayList<>();
        if(filteredPatients == null){
            filteredPatients = new ArrayList<>();
        }
        else{
            filteredPatients.clear();
        }
        String userId = SharedPreferencesManager.getActiveUserId(getActivity());
        for(Patient aPatient : allPatients){
            if(!aPatient.getPrimaryDoctorId().equalsIgnoreCase(userId)){
                sharedPatientList.add(aPatient);
                filteredPatients.add(aPatient);
            }
        }
        return sharedPatientList;
    }

    private void setUpDataForViews(){
        //Load all the models over here based on myPatientClicked
        if(myPatientClicked){
            //Load models for MyPatients
        }
        else{
            //Load models for SharedPatients
        }
        //after the models are loaded, load the filterContent in the Filter Model
        filter.populateFilterContent(myPatientClicked);//Pass in the required models as parameters
        // the models will be usd to populate the filterContentMap and not filter.filterContent
        //this is just used temporarily
        filterContentMap = getFilterContentMap(filter.getFilterContent());
        setupSearchContent();//This will get content from models.
    }


    private void launchFilterFragment(){
        oldFilterString = filter.getSearchString();
        if(filterFragment == null){
            filterFragment = FilterDialogFragment.newInstance();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterDialogFragment.FILTER_CONTENT_MAP_TAG, filter);
        filterFragment.setArguments(bundle);
        filterFragment.show(getFragmentManager(), "");
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, filterFragment)
//                .addToBackStack(null)
//                .commit();

    }
    //Utility Methods for the fragment
    private void setupSearchContent(){
        if(searchContentMap == null){
            searchContentMap = new LinkedHashMap<>();
        }
        searchContentMap.clear();
        searchContentMap.putAll(filterContentMap);
        //set the content for searchContentMap here
        //SearchBar.onQueryTextSubmit("", filterContentMap, searchContentMap);

    }

    private LinkedHashMap<String, List<String>> getFilterContentMap(Map<String, List<Filter.GroupItem>> filterContent){
        LinkedHashMap<String, List<String>> filterContentMap = new LinkedHashMap<>();
        for(String groupHeader : filter.getFilterContent().keySet()){
            List<String> groupItems = new ArrayList<>();
            for(Filter.GroupItem groupItem : filter.getFilterContent().get(groupHeader)){
                groupItems.add(groupItem.getItemName());
            }
            filterContentMap.put(groupHeader, groupItems);
        }
        return filterContentMap;
    }

    private void onPatientSelected(ExpandableListView parent, View view, int groupPosition, int childPosition, long id){
        Intent patientHeartSoundActivityIntent = new Intent(getActivity(), PatientHeartSoundActivity.class);
        startActivity(patientHeartSoundActivityIntent);
    }

    private void onPatientSelected(AdapterView<?> parent, View view, int position, long id){
        Intent patientHeartSoundActivityIntent = new Intent(getActivity(), PatientHeartSoundActivity.class);
        patientHeartSoundActivityIntent.putExtra(PatientHeartSoundActivity.PATIENT, JsonObjectParser.getJsonStringFromPatient(filteredPatients.get(position)));
        startActivity(patientHeartSoundActivityIntent);
    }

    private void launchPatientHeartSoundActivityForNewPatient(){
        Intent patientHeartSoundActivityIntent = new Intent(getActivity(), PatientHeartSoundActivity.class);
        startActivity(patientHeartSoundActivityIntent);
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

    //To display the search button on the toolbar
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.search_view_menu, menu);
//    }

    //SearchView.OnQueryTextListener Implementation
    @Override
    public boolean onQueryTextSubmit(String query) {
        if(displayState == DISPLAY_STATE.LIST_VIEW){
            if(mListView == null || patientListAdapter == null){
                setupListView();
            }
            if(myPatientClicked){
                DynamicSearchFilter.onQueryTextSubmit(query , getMyPatientList(), filteredPatients);
            }
            else{
                DynamicSearchFilter.onQueryTextSubmit(query , getSharedPatientList(), filteredPatients);
            }
            patientListAdapter.notifyDataSetChanged();
        }
        else if(displayState == DISPLAY_STATE.EXPANDABLE_LIST_VIEW){
            if(mExpandableListView == null || expandablePatientListAdapter == null){
                setupExpandableListView();
            }
            DynamicSearchFilter.onQueryTextSubmit(query, filterContentMap, searchContentMap);
            expandablePatientListAdapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(displayState == DISPLAY_STATE.LIST_VIEW){
            if(mListView == null || patientListAdapter == null){
                setupListView();
            }
            if(myPatientClicked){
                DynamicSearchFilter.onQueryTextChange(newText , getMyPatientList(), filteredPatients);
            }
            else{
                DynamicSearchFilter.onQueryTextChange(newText , getSharedPatientList(), filteredPatients);
            }
            patientListAdapter.notifyDataSetChanged();
        }
        else if(displayState == DISPLAY_STATE.EXPANDABLE_LIST_VIEW){
            if(mExpandableListView == null || expandablePatientListAdapter == null){
                setupExpandableListView();
            }
            DynamicSearchFilter.onQueryTextChange(newText , filterContentMap, searchContentMap);
            expandablePatientListAdapter.notifyDataSetChanged();
        }
        return false;
    }

    //SearchView.OnCloseListener Implementation
    @Override
    public boolean onClose() {
        return false;
    }

    public void requestPatients() {
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, WebAPI.PATIENT_BASE_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
                        Collection<Patient> parsedResponse = JsonObjectParser.getPatientListFromJsonString(response.toString());
                        if(parsedResponse != null){
                            allPatients = new ArrayList<>(parsedResponse);
                            Collections.sort(allPatients, new Comparator<Patient>() {
                                @Override
                                public int compare(Patient o1, Patient o2) {
                                    return o1.toString().compareTo(o2.toString());
                                }
                            });
                            if(displayState == DISPLAY_STATE.LIST_VIEW){
                                setupListView();
                            }
                            else{
                                setupExpandableListView();
                            }
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

    //Request user details
    private void requestUserDetails() {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(getActivity());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, WebAPI.GET_USER_INFO_URL, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        //update UI accordingly
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        //1. get user object
                        User loggedInUser = JsonObjectParser.getUserFromJsonString(response.toString());
                        SharedPreferencesManager.addUserIdToSharedPreferences(getActivity(), loggedInUser.getUsername(), loggedInUser.getId());
                        requestPatients();
                        //use the webAPIResponse to get message from server

                        //recreate the webAPIResponse object with default values
                        webAPIResponse = new WebAPIResponse();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //update UI accordingly
                DialogBoxDisplayHandler.dismissProgressDialog();
                //use the webAPIResponse to get message from server
                if(webAPIResponse.getStatusCode().equals(ResponseStatusCode.UNAUTHORIZED)){
                    SharedPreferencesManager.invalidateUserAccessToken(getActivity());;
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
        RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
}
