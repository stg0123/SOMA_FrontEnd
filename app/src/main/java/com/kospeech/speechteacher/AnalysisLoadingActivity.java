package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Presentation;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalysisLoadingActivity extends AppCompatActivity {
    private File file;
    private RetrofitService retrofitService;
    LinearLayout analysis_loading;
    private int practice_time;
    private int resultlistsize;
    private String presentation_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_loading);
        analysis_loading = findViewById(R.id.analysis_loading);
        file = new File(getExternalFilesDir(null),"record.m4a");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);
        practice_time = getIntent().getIntExtra("practice_time",0);
        resultlistsize = getIntent().getIntExtra("resultlistsize",0);
        presentation_id = getIntent().getStringExtra("presentation_id");

        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/m4a"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("audio_file", presentation_id+"_"+(resultlistsize+1)+"_"+"record.m4a", requestBody);

        RequestBody presentation_result_time = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(practice_time));
        HashMap<String,RequestBody> data = new HashMap<>();
        data.put("presentation_result_time",presentation_result_time);
        retrofitService.presentationresult(presentation_id,filePart,data).enqueue(new Callback<PresentationResult>() {
            @Override
            public void onResponse(Call<PresentationResult> call, Response<PresentationResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(getApplicationContext(),AnalysisActivity.class);
                    intent.putExtra("presentationResult",response.body());
                    intent.putExtra("practice_time",practice_time);
                    intent.putExtra("presentationItem",getIntent().getSerializableExtra("presentationItem"));
                    startActivity(intent);
                    Log.d(TAG, "onResponse: "+response.body().toString());
                    finish();
                } else {
                    try {
                        Gson gson = new Gson();
                        ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                        Toast.makeText( getApplicationContext() , data.message, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PresentationResult> call, Throwable t) {
                Log.d(TAG, "onFailure: connection fail");
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}