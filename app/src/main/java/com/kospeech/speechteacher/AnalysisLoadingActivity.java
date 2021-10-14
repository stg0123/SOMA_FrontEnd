package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

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
    TextView analysis_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_loading);
        analysis_text = findViewById(R.id.analysis_text);
        analysis_loading = findViewById(R.id.analysis_loading);
        file = new File(getExternalFilesDir(null),"record.mp3");
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);


        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("audio_file", "record.mp3", requestBody);
        retrofitService.presentationresult(filePart).enqueue(new Callback<PresentationResult>() {
            @Override
            public void onResponse(Call<PresentationResult> call, Response<PresentationResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    analysis_text.setText(response.body().toString());
                } else {
                    try {
                        Gson gson = new Gson();
                        ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                        Toast.makeText( getApplicationContext() , data.message, Toast.LENGTH_SHORT).show();
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
}