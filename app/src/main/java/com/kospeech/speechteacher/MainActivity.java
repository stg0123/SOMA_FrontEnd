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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private long backKeyPressed = 0 ;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentPresentation fragmentPresentation = new FragmentPresentation();
    private FragmentMypage fragmentMypage = new FragmentMypage();
    Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        menu = bottomNav.getMenu();
        menu.findItem(R.id.bottom_presentation).setIcon(R.drawable.ic_mic_on);
        bottomNav.setOnNavigationItemSelectedListener(new ItemSelectedListener());



    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.bottom_presentation:
                    menuItem.setIcon(R.drawable.ic_mic_on);
                    menu.findItem(R.id.bottom_calendar).setIcon(R.drawable.ic_calendar_off);
                    menu.findItem(R.id.bottom_knowhow).setIcon(R.drawable.ic_knowhow_off);
                    menu.findItem(R.id.bottom_mypage).setIcon(R.drawable.ic_mypage_off);
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_calendar:
                    menuItem.setIcon(R.drawable.ic_calendar_on);
                    menu.findItem(R.id.bottom_presentation).setIcon(R.drawable.ic_mic_off);
                    menu.findItem(R.id.bottom_knowhow).setIcon(R.drawable.ic_knowhow_off);
                    menu.findItem(R.id.bottom_mypage).setIcon(R.drawable.ic_mypage_off);
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_knowhow:
                    menuItem.setIcon(R.drawable.ic_knowhow_on);
                    menu.findItem(R.id.bottom_presentation).setIcon(R.drawable.ic_mic_off);
                    menu.findItem(R.id.bottom_calendar).setIcon(R.drawable.ic_calendar_off);
                    menu.findItem(R.id.bottom_mypage).setIcon(R.drawable.ic_mypage_off);
                    transaction.replace(R.id.fragment_container, fragmentPresentation).commitAllowingStateLoss();
                    break;
                case R.id.bottom_mypage:
                    menuItem.setIcon(R.drawable.ic_mypage_on);
                    menu.findItem(R.id.bottom_presentation).setIcon(R.drawable.ic_mic_off);
                    menu.findItem(R.id.bottom_calendar).setIcon(R.drawable.ic_calendar_off);
                    menu.findItem(R.id.bottom_knowhow).setIcon(R.drawable.ic_knowhow_off);
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