package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RegisterActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//
//
//            int itemID = item.getItemId();
//
//            if(itemID == R.id.navigation_next){
//               replaceFragment(new OTPFragment());
//            }
//
//
//            if (itemID == R.id.navigation_previous){
//                replaceFragment(new RegisterFragment());
//            }
//
//            return true;
//        });
//    }
//
//    private void replaceFragment(Fragment fragment){
//
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentContainerView, RegisterFragment.class, null);
//        ft.addToBackStack(fragment.toString())
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit();
    }

//    private void updateBottomNavVisibility(Fragment fragment) {
//        boolean isRegisterFragment = fragment instanceof RegisterFragment;
//        bottomNavigationView.getMenu().findItem(R.id.navigation_previous).setVisible(!isRegisterFragment);
//    }
}