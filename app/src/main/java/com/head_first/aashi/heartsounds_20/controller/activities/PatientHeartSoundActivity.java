package com.head_first.aashi.heartsounds_20.controller.activities;

import android.support.design.widget.NavigationView;
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
import android.widget.ListView;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.AudioRecordingFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.HeartSoundFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.MurmerRatingFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.PatientFragment;
import com.head_first.aashi.heartsounds_20.utils.NavigationDrawerContentListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The Default Fragment for this activity will be the PatientFragment
//The edit option should only be visible if the user viewing is the one that created the data
//When edit button is clicked the fields that can not be edited should be greyed out and
//the ones that can be edited should be highlighted.
//The save button for all the fragments in this activity should only be shown when the eidt button is clicked

public class PatientHeartSoundActivity extends AppCompatActivity {
    //the PATIENT_PAGE_TITLE is made public because Android has some limitation right now
    //and it doesnt change the actionbar title for the first attached fragment
    public static final String PATIENT_PAGE_TITLE = "Patient";
    private static final String HEART_SOUND_PAGE_TITLE = "Heart Sound";
    private static final String MURMER_RATING_PAGE_TITLE = "Murmer Rating";
    private static final String VOICE_COMMENT_PAGE_TITLE = "Voice Comment";
    private static final int NUMBER_OF_FRAGMENT_BEFORE_MURMER_RATING_FRAGMENT = 2;

    //Layout and Views
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    private NavigationView mHeartSoundNavigator;
    private ListView mNavigationViewListContent;
    private Button mAddNewButton;

    //Adapters
    NavigationDrawerContentListAdapter<String> navigationDrawerContentListAdapter;

    //Data
    //The user object will be used here, the user object will be passed
    // to this activity from the previous one and will contain all the
    //required data.
    //For now, a List of String will be used to replicate the List of HeartSounds
    //that the patient has and a HashMap<HearSound, List<MurmerRating>> to represent
    //the murmer rating for each HeartSound
    List<String> heartSounds;
    Map<String, List<String>> heartSoundToMurmerRating;
    String selectedHeartSound; //Later on this will be a HeartSound Object that will store the most recent HeartSound that the user selected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_heart_sound);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActionBarDrawerToggle =  new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigationDrawerOpen,  R.string.navigationDrawerClose);
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mActionBarDrawerToggle.syncState();
        mHeartSoundNavigator = (NavigationView) findViewById(R.id.navigationView);
        mNavigationViewListContent = (ListView) findViewById(R.id.navigationViewContent);
        //setup the data for the NavgationView
        //Retrieve a list of heartsounds realting to the patient id form the database
        //and store it in a list which can be used as an adapter to a ListView in the navigation Drawer
        //When the user selects a heartsound, change the contents of the navigation drawer to
        //a list of MurmerRatings

        //A temp method to populate the heartSound and heartSoundToMurmerRating with data
        //Later on both will be populated via the database.
        setUpData();
        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(this, heartSounds);
        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
        mNavigationViewListContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onNavigationViewListItemClick(parent, view, position, id);
            }
        });

        mAddNewButton = (Button) findViewById(R.id.addNewButton);
        mAddNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do stuff here
            }
        });
        //Launch the default Fragment
        PatientFragment patientFragment = new PatientFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, patientFragment, PatientFragment.PATIENT_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment){
        super.onAttachFragment(fragment);
        if(fragment instanceof PatientFragment){
            mToolbar.setTitle(PATIENT_PAGE_TITLE);
        }
        else if(fragment instanceof HeartSoundFragment){
            mToolbar.setTitle(HEART_SOUND_PAGE_TITLE);
        }
        else if(fragment instanceof MurmerRatingFragment){
            mToolbar.setTitle(MURMER_RATING_PAGE_TITLE);
        }
        else if(fragment instanceof AudioRecordingFragment){
            mToolbar.setTitle(VOICE_COMMENT_PAGE_TITLE);
        }
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
        if(fragment instanceof MurmerRatingFragment){
            if(onBackPressedFromMurmurRatingFragment((MurmerRatingFragment) fragment)){
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
        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(this, heartSounds);
        mNavigationViewListContent.setAdapter(navigationDrawerContentListAdapter);
        mToolbar.setTitle(PATIENT_PAGE_TITLE);
        return false;
    }

    private boolean onBackPressedFromMurmurRatingFragment(MurmerRatingFragment fragment){
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
        heartSounds = new ArrayList<>();
        heartSoundToMurmerRating = new HashMap<>();
        //the following will be deleted later
        for(int i = 0; i < 10; i++) {
            heartSounds.add("Heart Sound " + (i + 1));
            List<String> murmerRating = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                murmerRating.add("Murmer Rating" + (j + 1));
            }
            heartSoundToMurmerRating.put(heartSounds.get(i), murmerRating);
        }
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
        //Launch HeartSound Fragments here
        selectedHeartSound = heartSounds.get(position);
        HeartSoundFragment heartSoundFragment = new HeartSoundFragment();
        mDrawerLayout.closeDrawers();
        //Change the contents of the NavigationView to a List of MurmerRatings
        navigationDrawerContentListAdapter = new NavigationDrawerContentListAdapter<>(this, heartSoundToMurmerRating.get(selectedHeartSound));
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
        // MurmerRating murmerRating = heartSoundToMurmerRating.get(selectedHeartSound);
        MurmerRatingFragment murmerRatingFragment = new MurmerRatingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MurmerRatingFragment.SELECTED_MURMER_RATING_TAG, position);
        murmerRatingFragment.setArguments(bundle);
        mDrawerLayout.closeDrawers();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, murmerRatingFragment, MurmerRatingFragment.MURMER_RATING_FRAGMENT_TAG)
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

    //Later on this method will return HeartSound
    public String getSelectedHeartSound(){
        return selectedHeartSound;
    }

    //Later on this method will rerturn MurmerRating
    public String getSelectedMurmerRating(int position){
        if(selectedHeartSound == null){
            return selectedHeartSound;
        }
        return heartSoundToMurmerRating.get(selectedHeartSound).get(position);
    }

    public void setActionBarTitle(String title){
        if(title != null && !title.isEmpty()) {
            mToolbar.setTitle(title);
        }
    }
}
