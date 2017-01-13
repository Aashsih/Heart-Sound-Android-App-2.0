package com.head_first.aashi.heartsounds_20.controller.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.head_first.aashi.heartsounds_20.R;
import com.head_first.aashi.heartsounds_20.controller.fragment.Patients;
import com.head_first.aashi.heartsounds_20.controller.fragment.UserProfileFragment;
import com.head_first.aashi.heartsounds_20.model.User;

public class UserPatientActivity extends AppCompatActivity {

    private static final int DEFAULT_MENU_ITEM = R.id.myPatients;

    //Data
    private User user; //User could be a Doctor, Student or a

    //Views
    private Fragment menuItemFragment;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_patient);

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
                menuItemFragment = new UserProfileFragment();
                break;
            case R.id.myPatients:
                menuItemFragment = new Patients();
                ((Patients) menuItemFragment).setMyPatientClicked(true);
                break;
            case R.id.otherPatients:
                menuItemFragment = new Patients();
                ((Patients) menuItemFragment).setMyPatientClicked(false);
                break;

            default:
                menuItemFragment = new Patients();
                ((Patients) menuItemFragment).setMyPatientClicked(true);
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, menuItemFragment)
                .addToBackStack(null)
                .commit();
    }
}
