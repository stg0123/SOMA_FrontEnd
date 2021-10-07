package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private long backKeyPressed = 0 ;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentPresentation fragmentPresentation = new FragmentPresentation();
    private FragmentMypage fragmentMypage = new FragmentMypage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.bottom_presentation:
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_calendar:
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_knowhow:
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_mypage:
                    transaction.replace(R.id.fragment_container, fragmentMypage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressed + 2500){
            backKeyPressed = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
        }
    }
}