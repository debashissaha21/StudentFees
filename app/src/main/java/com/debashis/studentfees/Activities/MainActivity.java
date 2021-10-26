package com.debashis.studentfees.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.debashis.studentfees.Fragments.EmptyFragment;
import com.debashis.studentfees.Fragments.HomeFragment;
import com.debashis.studentfees.Fragments.LoginFragment;
import com.debashis.studentfees.Fragments.ManageFragment;
import com.debashis.studentfees.Fragments.ProfileFragment;
import com.debashis.studentfees.R;
import com.debashis.studentfees.SharedPrefManager;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    ChipNavigationBar bottomNav;
    FragmentManager fragmentManager;
    SharedPrefManager sharedPrefManager;
    Toolbar toolbar;
    private static final String Tag = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_nav_bar);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());

        if (savedInstanceState == null) {
            bottomNav.setItemSelected(R.id.bottom_nav_dashboard, true);
            fragmentManager = getSupportFragmentManager();
            HomeFragment homeFragment = new HomeFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        }
        bottomNav.setItemSelected(R.id.bottom_nav_dashboard, true);
        bottomNav.setOnItemSelectedListener(
                new ChipNavigationBar.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int id) {
                        Fragment fragment = null;
                        switch (id) {
                            case R.id.bottom_nav_dashboard:
                                fragment = new HomeFragment();
                                break;
                            case R.id.bottom_nav_manage:
                                if (sharedPrefManager.isLoggedIn()) {
                                    fragment = new ManageFragment();
                                } else {
                                    fragment = new EmptyFragment();
                                }

                                break;
                            case R.id.bottom_nav_profile:
                                if (sharedPrefManager.isLoggedIn()) {
                                    fragment = new ProfileFragment();
                                } else {
                                    fragment = new LoginFragment();
                                }
                                break;
                        }
                        if (fragment != null) {
                            fragmentManager = getSupportFragmentManager();
                            fragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .commit();

                        } else {
                            Log.e(Tag, "Error in creating fragment");
                        }
                    }
                });
    }
}
