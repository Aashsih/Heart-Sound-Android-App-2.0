package com.head_first.aashi.heartsounds_20.controller.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.MurmerRatingFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.PatientListFragment;
import com.head_first.aashi.heartsounds_20.controller.fragment.UserProfileFragment;
import com.head_first.aashi.heartsounds_20.model.User;

public class UserPatientActivity extends AppCompatActivity {

    private static final int DEFAULT_MENU_ITEM = R.id.myPatients;
    private static final String PROFILE_PAGE_TITLE = "User Profile";
    private static final String MY_PATIENTS_PAGE_TITLE = "My Patients";
    private static final String SHARED_PATIENTS_PAGE_TITLE = "Shared Patients";
    private static final String OTHER_PATIENTS_PAGE_TITLE = "";

    //Data
    private User user; //User could be a Doctor, Student or a

    //Views
    private Toolbar mToolbar;
    private Fragment menuItemFragment;
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

    private void launchSelectedMenuFragment(MenuItem item){
        Fragment menuItemFragment;
        switch(item.getItemId()){
            case R.id.userProfile:
                mToolbar.setTitle(PROFILE_PAGE_TITLE);
                menuItemFragment = new UserProfileFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, menuItemFragment, UserProfileFragment.USER_PROFILE_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.myPatients:
                mToolbar.setTitle(MY_PATIENTS_PAGE_TITLE);
                menuItemFragment = new PatientListFragment();
                ((PatientListFragment) menuItemFragment).setMyPatientClicked(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, menuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.otherPatients:
//                When the student user is implemented, change this to Other patients
                mToolbar.setTitle(SHARED_PATIENTS_PAGE_TITLE);
                menuItemFragment = new PatientListFragment();
                ((PatientListFragment) menuItemFragment).setMyPatientClicked(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, menuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();
                break;

            default:
                mToolbar.setTitle(MY_PATIENTS_PAGE_TITLE);
                menuItemFragment = new PatientListFragment();
                ((PatientListFragment) menuItemFragment).setMyPatientClicked(true);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, menuItemFragment, PatientListFragment.PATIENT_LIST_FRAGMENT_TAG)
                        .commit();

                break;
        }
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragmentContainer, menuItemFragment)
//                .commit();
    }
}
