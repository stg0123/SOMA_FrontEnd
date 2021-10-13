package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresentationPracticeActivity extends AppCompatActivity {

    ImageView practice_out,practice_analysis;
    ImageButton practice_record;
    TextView anlysis_text;

    private boolean isRecording = false;
    private int RECORD_PERMISSION_CODE = 21;

    private MediaRecorder mediaRecorder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_practice);
        practice_out = findViewById(R.id.practice_out);
        practice_record = findViewById(R.id.practice_record);
        practice_analysis = findViewById(R.id.practice_analysis);
        anlysis_text = findViewById(R.id.analysis_text);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        File file = new File(getExternalFilesDir(null),"record.mp3");


        practice_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        practice_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(isRecording){
                    isRecording = false;
                    practice_record.setImageDrawable(getDrawable(R.drawable.start_ic));
                    mediaRecorder.pause();
                }
                else{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        isRecording = true;
                        practice_record.setImageDrawable(getDrawable(R.drawable.pause_ic));
                        if(mediaRecorder == null){
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                            mediaRecorder.setOutputFile(file.getAbsolutePath());
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                            mediaRecorder.resume();
                    } else {
                        ActivityCompat.requestPermissions(PresentationPracticeActivity.this , new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_CODE);
                    }
                }
            }
        });

        practice_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaRecorder!=null) {
                    if(isRecording) {
                        isRecording = false;
                        practice_record.setImageDrawable(getDrawable(R.drawable.start_ic));
                    }
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    RequestBody requestBody = RequestBody.create(MediaType.parse("audio/mp3"), file);

                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("audio_file", "record.mp3", requestBody);
                    retrofitService.presentationresult(filePart).enqueue(new Callback<PresentationResult>() {
                        @Override
                        public void onResponse(Call<PresentationResult> call, Response<PresentationResult> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                anlysis_text.setText(response.body().toString());

                            } else {
                                try {
                                    Gson gson = new Gson();
                                    ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                                    Toast.makeText(view.getContext(), data.message, Toast.LENGTH_SHORT).show();
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
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 연습 내용이 모두 사라집니다.")
                .setTitle("연습을 중지하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



}