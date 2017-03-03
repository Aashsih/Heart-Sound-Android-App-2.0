package com.head_first.aashi.heartsounds_20.controller.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.PatientListFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.UserProfileFragment;
import com.head_first.aashi.heartsounds_20.model.User;

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

    //Data
    private User user; //User could be a Doctor, Student or a

    //Views
    private Toolbar mToolbar;
    private Fragment activeMenuItemFragment;
    private BottomNavigationView mBottomNavigationView;

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
        if(fragment instanceof UserProfileFragment){
            if(((UserProfileFragment) fragment).editModeEnabled()){
                ((UserProfileFragment) fragment).cancelChanges();
                return;
            }

        }
        super.onBackPressed();
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
        switch(item.getItemId()){
            case R.id.userProfile:

                activeMenuItemFragment = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, UserProfileFragment.USER_PROFILE_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.myPatients:

                activeMenuItemFragment = new PatientListFragment();
                ((PatientListFragment) activeMenuItemFragment).setMyPatientClicked(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.otherPatients:
//                When the student user is implemented, change this to Other patients

                activeMenuItemFragment = new PatientListFragment();
                ((PatientListFragment) activeMenuItemFragment).setMyPatientClicked(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, activeMenuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;

            default:
                mToolbar.setTitle(MY_PATIENTS_PAGE_TITLE);
                activeMenuItemFragment = new PatientListFragment();
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

}
