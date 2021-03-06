package com.head_first.aashi.heartsounds_20.controller.activities;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.transition.Visibility;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.AudioRecordingFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.HeartSoundFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.MurmurRatingFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.PatientFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.WebAPIErrorFragment;
import com.head_first.aashi.heartsounds_20.enums.web_api_enums.ResponseStatusCode;
import com.head_first.aashi.heartsounds_20.interfaces.util_interfaces.NavgigationDrawerUtils;
import com.head_first.aashi.heartsounds_20.model.Doctor;
import com.head_first.aashi.heartsounds_20.model.HeartSound;
import com.head_first.aashi.heartsounds_20.model.MurmurRating;
import com.head_first.aashi.heartsounds_20.model.Patient;
import com.head_first.aashi.heartsounds_20.model.User;
import com.head_first.aashi.heartsounds_20.utils.DialogBoxDisplayHandler;
import com.head_first.aashi.heartsounds_20.utils.NavigationDrawerContentListAdapter;
import com.head_first.aashi.heartsounds_20.utils.JsonObjectParser;
import com.head_first.aashi.heartsounds_20.utils.SharedPreferencesManager;
import com.head_first.aashi.heartsounds_20.utils.StringUtil;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPI;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The Default Fragment for this activity will be the PatientFragment
//The edit option should only be visible if the user viewing is the one that created the data
//When edit button is clicked the fields that can not be edited should be greyed out and
//the ones that can be edited should be highlighted.
//The save button for all the fragments in this activity should only be shown when the edit button is clicked

public class PatientHeartSoundActivity extends AppCompatActivity implements NavgigationDrawerUtils {
    //the PATIENT is made public because Android has some limitation right now
    //and it does not change the actionbar title for the first attached fragment
    public static final String PATIENT = "Patient";
    private static final String HEART_SOUND_PAGE_TITLE = "Heart Sound";
    private static final String MURMER_RATING_PAGE_TITLE = "Murmer Rating";
    private static final String VOICE_COMMENT_PAGE_TITLE = "Voice Comment";
    private static final int NUMBER_OF_FRAGMENT_BEFORE_MURMER_RATING_FRAGMENT = 2;

    //Layout and Views
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ListView mNavigationViewListContent;
    private Button mAddNewButton;
    private Drawable mNavigationIcon;
    private ImageButton mNavigationDrawerHeader;

    //Adapters
    NavigationDrawerContentListAdapter<String> navigationDrawerContentListAdapter;

    //Data
    private Patient patient;
    private Map<String, User> userIdToUserMap;
    private List<String> heartSounds;
    private Map<Long, List<String>> heartSoundToMurmurRating;
    private HeartSound selectedHeartSoundObject;
    private Long selectedHeartSound; //Stores the id of the selected heart sound

    //Web API
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null){
            patient = JsonObjectParser.getPatientFromJsonString(getIntent().getStringExtra(PATIENT));
        }
        setContentView(R.layout.activity_patient_heart_sound);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigationDrawerOpen,  R.string.navigationDrawerClose);
        mActionBarDrawerToggle.syncState();
        mNavigationViewListContent = (ListView) findViewById(R.id.navigationViewContent);
        mNavigationDrawerHeader = (ImageButton) findViewById(R.id.header);

        mAddNewButton = (Button) findViewById(R.id.addNewButton);
        mAddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddNewButtonClicked();
            }
        });
        //Launch the default Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, PatientFragment.newInstance(), PatientFragment.PATIENT_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment){
        super.onAttachFragment(fragment);
        setSupportActionBarTitle(fragment);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(patient == null){
            disableNavigationMenu();
        }
        else{
            enableNavigationMenu();
        }
        getAllUsers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment instanceof MurmurRatingFragment){
            if(onBackPressedFromMurmurRatingFragment((MurmurRatingFragment) fragment)){
                return;
            }

        } //else if check for next condition
        else if(fragment instanceof HeartSoundFragment){
            if(onBackPressedFromHeartSoundFragment((HeartSoundFragment)fragment)){

                return;
            }
        }
        else if(fragment instanceof PatientFragment){
            if(onBackPressedFromPatientFragment((PatientFragment)fragment)){
                return;
            }
        }
        else if(fragment instanceof AudioRecordingFragment){
            if(onBackPressedFromAudioRecordingFragment((AudioRecordingFragment)fragment)){
                return;
            }
        }
        super.onBackPressed();
    }

    public void setSupportActionBarTitle(Fragment fragment){
        if(fragment instanceof PatientFragment){
            mToolbar.setTitle(PATIENT);
        }
        else if(fragment instanceof HeartSoundFragment){
            mToolbar.setTitle(HEART_SOUND_PAGE_TITLE);
        }
        else if(fragment instanceof MurmurRatingFragment){
            mToolbar.setTitle(MURMER_RATING_PAGE_TITLE);
        }
        else if(fragment instanceof AudioRecordingFragment){
            mToolbar.setTitle(VOICE_COMMENT_PAGE_TITLE);
        }
    }

    public Patient getPatient(){

        return patient;
    }

    public void setPatient(Patient patient){
        this.patient = patient;
    }

    public String getUserName(String userId){
        if(userIdToUserMap == null){
            return null;
        }
        else{
            return userIdToUserMap.get(userId).getName();
        }
    }

    public HeartSound getHeartSoundObject(){
        return this.selectedHeartSoundObject;
    }

    public void setHeartSoundObject(HeartSound heartSound){
        this.selectedHeartSoundObject = heartSound;
    }

    public List<User> getAllUserObjects(){
        if(userIdToUserMap != null){
            return new ArrayList<>(userIdToUserMap.values());
        }
        return null;
    }

    //Later on this method will return HeartSound
    public Long getSelectedHeartSound(){
        return selectedHeartSound;
    }

    //Later on this method will return MurmerRating
    public String getSelectedMurmurRating(int position){
        if(selectedHeartSound == null){
            return null;
        }
        return heartSoundToMurmurRating.get(selectedHeartSound).get(position);
    }

    public void setActionBarTitle(String title){
        if(title != null && !title.isEmpty()) {
            mToolbar.setTitle(title);
        }
    }

    public int addNewMurmurRatingToHeartSoundToMurmurRatingMap(String murmurRating){
        int newMurmurRatingPosition = -1;
        if(heartSoundToMurmurRating != null){
            if(heartSoundToMurmurRating.containsKey(selectedHeartSound)){
                boolean result = heartSoundToMurmurRating.get(selectedHeartSound).add(murmurRating);
                if(result){
                    newMurmurRatingPosition = heartSoundToMurmurRating.get(selectedHeartSound).size() - 1;
                }
            }
            else{
                finish();
            }
        }
        return newMurmurRatingPosition;
    }

    public boolean deleteMurmurRatingFromHeartSoundToMurmurRatingMap(@NonNull MurmurRating murmurRating){
        boolean deleted = false;
        if(heartSoundToMurmurRating != null){
            if(heartSoundToMurmurRating.containsKey(selectedHeartSound)){
                deleted = heartSoundToMurmurRating.get(selectedHeartSound).remove(murmurRating.toString());
            }
        }
        return deleted;
    }

    public void setupNavigationDrawerContent(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(patient != null){
            if(fragment instanceof HeartSoundFragment || fragment instanceof MurmurRatingFragment){
                //Store a local copy of the active heart sound and then use that to fetch
                //the murmur rating associated with it
                if(heartSoundToMurmurRating.containsKey(selectedHeartSound)){
                    mNavigationDrawerHeader.setImageResource(R.drawable.murmur_rating_navigation_menu_header);
                    List<String> murmurRatings = heartSoundToMurmurRating.get(selectedHeartSound);
                    if(murmurRatings == null){
                        requestMurmurRatingsForHeartSound(selectedHeartSound.intValue());
                    }
                    navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(getApplicationContext(), heartSoundToMurmurRating.get(selectedHeartSound));
                    mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
                    enableNavigationMenu();
                }
                else{
                    //lock the navigation bar as a new HeartSound is being created
                    disableNavigationMenu();
                }
            }
            else{
                //use the patient id to make a call and get all the Heart Sounds associated with the patient
                mNavigationDrawerHeader.setImageResource(R.drawable.stethoscope_navigation_menu_header);
                requestPatientHeartSounds((int)patient.getPatientId());
            }
        }

        //setup the data for the NavgationView
        //Retrieve a list of heartsounds relating to the patient id form the database
        //and store it in a list which can be used as an adapter to a ListView in the navigation Drawer
        //When the user selects a heartsound, change the contents of the navigation drawer to
        //a list of MurmurRatings

        //A temp method to populate the heartSound and heartSoundToMurmurRating with data
        //Later on both will be populated via the database.
        //setUpData();
        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
        mNavigationViewListContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNavigationViewListItemClick(parent, view, position, id);
            }
        });
    }

    private boolean onBackPressedFromPatientFragment(PatientFragment fragment){
        if(fragment.editModeEnabled()){
            fragment.cancelChanges();
            return true;
        }
        return false;
    }
    //The following onBackPressed Methods return true to suggest to the calling method whether further execution from their end is required
    private boolean onBackPressedFromHeartSoundFragment(HeartSoundFragment fragment){
        mAddNewButton.setText(getResources().getString(R.string.addHeartSoundButtonLabel));
        //if the fragment in the foreground is a HeartSoundFragment, then change the contents of the
        // navigationDrawerContentListAdapter from MurmerRating list to HeartSoundList
        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(this, StringUtil.convertListToListOfString(heartSounds));
        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
        mToolbar.setTitle(PATIENT);
        enableNavigationMenu();
        return false;
    }

    private boolean onBackPressedFromMurmurRatingFragment(MurmurRatingFragment fragment){
        if(fragment.editModeEnabled()){
            fragment.cancelChanges();
            return true;
        }
        int numberOfFragmentsInBackStack = getSupportFragmentManager().getBackStackEntryCount();
        while(numberOfFragmentsInBackStack != NUMBER_OF_FRAGMENT_BEFORE_MURMER_RATING_FRAGMENT){
            getSupportFragmentManager().popBackStack();
            numberOfFragmentsInBackStack--;
        }
        mToolbar.setTitle(HEART_SOUND_PAGE_TITLE);
        return false;
    }

    private boolean onBackPressedFromAudioRecordingFragment(AudioRecordingFragment fragment){
        fragment.cancelChanges();
        //the return statement below is required because the cancelChanges() method in the
        //AudioRecordingFragment, automatically removes the Fragment and hence there is no need to call
        //super.onBackPressed() after.
        mToolbar.setTitle(HEART_SOUND_PAGE_TITLE);
        return true;
    }

    private void setUpData(){
        heartSoundToMurmurRating = new HashMap<>();
        for(String heartSoundId: heartSounds){
            //heartSoundToMurmurRating.put(HeartSound.getIdFromString(heartSoundId), new ArrayList<String>());
            heartSoundToMurmurRating.put(HeartSound.getIdFromString(heartSoundId), null);
        }
        //the following will be deleted later
//        for(int i = 0; i < 10; i++) {
//            heartSounds.add("Heart Sound " + (i + 1));
//            List<String> murmerRating = new ArrayList<>();
//            for (int j = 0; j < 10; j++) {
//                murmerRating.add("Murmer Rating" + (j + 1));
//            }
//            heartSoundToMurmurRating.put(heartSounds.get(i), murmerRating);
//        }
    }

    private void onNavigationViewListItemClick(AdapterView<?> parent, View view, int position, long id){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment instanceof PatientFragment){
            launchHeartSoundFragment(parent, view, position, id);
        }
        else{
            launchMurmurRatingFragment(parent, view, position, id);
        }
    }

    private void launchHeartSoundFragment(AdapterView<?> parent, View view, int position, long id){
        mAddNewButton.setText(getResources().getString(R.string.addMurmurRatingButtonLabel));
        HeartSoundFragment heartSoundFragment = HeartSoundFragment.newInstance();
        mDrawerLayout.closeDrawers();
        selectedHeartSound = HeartSound.getIdFromString(heartSounds.get(position));
        //Change the contents of the NavigationView to a List of MurmurRatings
        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(this, heartSoundToMurmurRating.get(selectedHeartSound));
        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);

        //launch the fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, heartSoundFragment, HeartSoundFragment.HEART_SOUND_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    private void launchMurmurRatingFragment(AdapterView<?> parent, View view, int position, long id){
        //Launch MurmerRating Fragments here
        //To identify which MurmerRating was selected use the "selectedHeartSound" field like this
        // MurmerRating murmerRating = heartSoundToMurmurRating.get(selectedHeartSound);
        MurmurRatingFragment murmurRatingFragment = MurmurRatingFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt(MurmurRatingFragment.SELECTED_MURMER_RATING_TAG, position);
        murmurRatingFragment.setArguments(bundle);
        mDrawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, murmurRatingFragment, MurmurRatingFragment.MURMER_RATING_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }

    private void addNewNavigationListContent(Fragment fragment){
        //Note: after making any changes here make sure to call the notifyDataSetHasChanged()
        if(fragment instanceof PatientFragment){

            //add new HeartSound Fragment in edit mode
        }
        else{
            //add new Murmur Rating Fragment in edit mode
        }
    }

    public void launchWebAPIErrorFragment(){
        Bundle bundle = new Bundle();
        bundle.putString(WebAPIErrorFragment.WEB_API_ERROR_MESSAGE_TAG, webAPIResponse.getMessage());
        WebAPIErrorFragment webAPIErrorFragment = WebAPIErrorFragment.newInstance();
        webAPIErrorFragment.setArguments(bundle);
        this.getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, webAPIErrorFragment, WebAPIErrorFragment.WEB_API_ERROR_FRAGMENT_TAG)
                .commit();
    }

    private void onAddNewButtonClicked(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment instanceof PatientFragment){
            //add new heart sound by launching the HeartSoundFragment
            mDrawerLayout.closeDrawers();
            selectedHeartSound = null;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HeartSoundFragment.newInstance(), HeartSoundFragment.HEART_SOUND_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();

        }
        else{
            //add new MurmurRating by launching the MurmurRating Fragment
            mDrawerLayout.closeDrawers();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, MurmurRatingFragment.newInstance(), MurmurRatingFragment.MURMER_RATING_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();

        }
    }

    // Implementation for NavigationDrawerUtils interface
    @Override
    public void disableNavigationMenu(){
        if(mToolbar.getNavigationIcon() != null){
            mNavigationIcon = mToolbar.getNavigationIcon();
        }
        mToolbar.setNavigationIcon(null);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void enableNavigationMenu(){
        if(mNavigationIcon != null){
            mToolbar.setNavigationIcon(mNavigationIcon);
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    private void requestPatientHeartSounds(int patientId) {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(this);
        StringRequest request = new StringRequest(Request.Method.GET, WebAPI.GET_PATIENT_HEART_SOUNDS_URL + patientId,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        Collection<HeartSound> heartSoundCollection = JsonObjectParser.getHeartSoundListFromJsonString(response.toString());
                        List<HeartSound> heartSoundList = null;
                        if(heartSoundCollection != null){
                            heartSoundList = new ArrayList<>(heartSoundCollection);
                        }
                        heartSounds = StringUtil.convertListToListOfString(heartSoundList);
                        setUpData();
                        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(getApplicationContext(), heartSounds);
                        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
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
                    SharedPreferencesManager.invalidateUserAccessToken(PatientHeartSoundActivity.this);
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
                return WebAPI.prepareAccessTokenHeader(SharedPreferencesManager.getUserAccessToken(PatientHeartSoundActivity.this));
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

        RequestQueueSingleton.getInstance(this).addToRequestQueue(request);

    }

    public void getAllUsers() {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(this);
        StringRequest request = new StringRequest(Request.Method.GET, WebAPI.GET_ALL_USERS_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
                        Collection<User> parsedResponse = JsonObjectParser.getUserListFromJsonString(response.toString());
                        if(parsedResponse != null){
                            userIdToUserMap = new HashMap<>();
                            for(User aUser : parsedResponse){
                                userIdToUserMap.put(aUser.getId(), aUser);
                            }
                            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
                            if(fragment instanceof PatientFragment){
                                ((PatientFragment)fragment).setupViewsWithPatientData();
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
                    SharedPreferencesManager.invalidateUserAccessToken(PatientHeartSoundActivity.this);
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
                return WebAPI.prepareAccessTokenHeader(SharedPreferencesManager.getUserAccessToken(PatientHeartSoundActivity.this));
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

        RequestQueueSingleton.getInstance(PatientHeartSoundActivity.this).addToRequestQueue(request);
    }

    public void requestMurmurRatingsForHeartSound(int heartSoundId) {
        DialogBoxDisplayHandler.dismissProgressDialog();
        DialogBoxDisplayHandler.showIndefiniteProgressDialog(this);
        StringRequest request = new StringRequest(Request.Method.GET, WebAPI.GET_MURMUR_RATING_FOR_HEART_SOUND_URL + heartSoundId,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        //update UI accordingly
                        DialogBoxDisplayHandler.dismissProgressDialog();
                        Collection<MurmurRating> murmurRatingCollection = JsonObjectParser.getMurmurRatingListFromJsonString(response.toString());
                        List<MurmurRating> murmurRatingList = null;
                        if(murmurRatingCollection != null){
                            murmurRatingList = new ArrayList<>(murmurRatingCollection);
                        }
                        heartSoundToMurmurRating.put(selectedHeartSound, StringUtil.convertListToListOfString(murmurRatingList));
                        setupNavigationDrawerContent();
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
                    SharedPreferencesManager.invalidateUserAccessToken(PatientHeartSoundActivity.this);
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
                return WebAPI.prepareAccessTokenHeader(SharedPreferencesManager.getUserAccessToken(PatientHeartSoundActivity.this));
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

        RequestQueueSingleton.getInstance(PatientHeartSoundActivity.this).addToRequestQueue(request);

    }
}
