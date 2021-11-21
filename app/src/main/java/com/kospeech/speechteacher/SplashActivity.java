package com.kospeech.speechteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getString("auto_login","").equals(""))
                    startActivity(new Intent(getApplication(),LoginActivity.class));
                else
                    startActivity(new Intent(getApplication(),MainActivity.class));
                finish();
            }
        }, 1500);
    }
}