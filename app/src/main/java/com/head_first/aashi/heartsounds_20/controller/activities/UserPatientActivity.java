package com.head_first.aashi.heartsounds_20.controller.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.LoginFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.PatientListFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.UserProfileFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.WebAPIErrorFragment;
import com.head_first.aashi.heartsounds_20.web_api.RequestQueueSingleton;
import com.head_first.aashi.heartsounds_20.web_api.WebAPIResponse;

import org.json.JSONObject;

import java.util.HashMap;

public class UserPatientActivity extends AppCompatActivity {
    /**
     * In order to implement multiple themes for the app, create different styles for light and dark combinations
     * and create a class that contains all those options. Store user preference in SharedPreference and then dynamically
     * set the theme for the different views.
     *
     * For now basic background colour has been set in activities. (Login Fragment is an exception due to the colour of the gif)
     */
    private static final int DEFAULT_MENU_ITEM = R.id.myPatients;
    private static final String PROFILE_PAGE_TITLE = "User Profile";
    //the MY_PATIENTS_PAGE_TITLE is made public because Android has some limitation right now
    //and it doesnt change the actionbar title for the first attached fragment
    public static final String MY_PATIENTS_PAGE_TITLE = "My Patients";
    private static final String SHARED_PATIENTS_PAGE_TITLE = "Shared Patients";
    private static final String OTHER_PATIENTS_PAGE_TITLE = "";

    //Views
    private Toolbar mToolbar;
    private Fragment activeMenuItemFragment;
    private BottomNavigationView mBottomNavigationView;

    //Web APi
    WebAPIResponse webAPIResponse = new WebAPIResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_patient);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mBottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationBar);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                launchSelectedMenuFragment(item);
                return true;
            }
        });
        mBottomNavigationView.inflateMenu(R.menu.activity_user_patient_bottom_navigation_bar_menu_items);
        mBottomNavigationView.getMenu().findItem(DEFAULT_MENU_ITEM).setChecked(true);
        launchSelectedMenuFragment(mBottomNavigationView.getMenu().findItem(DEFAULT_MENU_ITEM));
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment instanceof WebAPIErrorFragment || fragment instanceof LoginFragment){
            super.onBackPressed();
        }
        else{
            this.finishAffinity();
        }

    }

    @Override
    public void onAttachFragment(Fragment fragment){
        super.onAttachFragment(fragment);
        if(fragment instanceof UserProfileFragment){
            mToolbar.setTitle(PROFILE_PAGE_TITLE);
        }
        else if(fragment instanceof PatientListFragment){
            if(((PatientListFragment) fragment).isMyPatientClicked()){
                mToolbar.setTitle(MY_PATIENTS_PAGE_TITLE);
            }
            else{
                mToolbar.setTitle(SHARED_PATIENTS_PAGE_TITLE);
            }
        }
    }


    private void launchSelectedMenuFragment(MenuItem item){
        Bundle bundle = null;
        switch(item.getItemId()){
            case R.id.userProfile:

                activeMenuItemFragment = UserProfileFragment.newInstance();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, UserProfileFragment.USER_PROFILE_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.myPatients:

                activeMenuItemFragment = PatientListFragment.newInstance();
                bundle = new Bundle();
                bundle.putBoolean(PatientListFragment.MY_PATIENT_SELECTED, true);
                activeMenuItemFragment.setArguments(bundle);
                //((PatientListFragment) activeMenuItemFragment).setMyPatientClicked(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.otherPatients:
//                When the student user is implemented, change this to Other patients
                bundle = new Bundle();
                bundle.putBoolean(PatientListFragment.MY_PATIENT_SELECTED, false);
                activeMenuItemFragment = PatientListFragment.newInstance();
                activeMenuItemFragment.setArguments(bundle);
                //((PatientListFragment) activeMenuItemFragment).setMyPatientClicked(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;

            default:
                mToolbar.setTitle(MY_PATIENTS_PAGE_TITLE);
                activeMenuItemFragment = PatientListFragment.newInstance();
                ((PatientListFragment) activeMenuItemFragment).setMyPatientClicked(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();

                break;
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, activeMenuItemFragment)
//                .commit();
    }

    public void setActionBarTitle(String title){
        if(title != null && !title.isEmpty()) {
            mToolbar.setTitle(title);
        }
    }

    private void launchWebAPIErrorFragment(){
        Bundle bundle = new Bundle();
        bundle.putString(WebAPIErrorFragment.WEB_API_ERROR_MESSAGE_TAG, webAPIResponse.getMessage());
        WebAPIErrorFragment webAPIErrorFragment = WebAPIErrorFragment.newInstance();
        webAPIErrorFragment.setArguments(bundle);
        this.getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainer, webAPIErrorFragment, WebAPIErrorFragment.WEB_API_ERROR_FRAGMENT_TAG)
                .commit();
    }

}
