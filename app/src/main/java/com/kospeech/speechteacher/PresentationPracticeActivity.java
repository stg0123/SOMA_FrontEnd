package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class PresentationPracticeActivity extends AppCompatActivity {

    ImageView practice_out,practice_analysis;
    ImageButton practice_record;
    TextView practice_script_text,practice_change_statetext;
    FlexboxLayout practice_keyword_flexbox;
    LinearLayout practice_layout,practice_keyword_layout,practice_script_layout;
    ConstraintLayout practice_change_layout;
    private int change =0;

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
        practice_script_text = findViewById(R.id.practice_script_text);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        File file = new File(getExternalFilesDir(null),"record.mp3");

        practice_keyword_flexbox = findViewById(R.id.practice_keyword_flexbox);
        String str[] = {"발표","어플리케이션","개인화","아이스크림","강아지"};
        for(int i=0;i<5;i++){
            TextView tmp = new TextView(this);
            tmp.setText(str[i]);
            tmp.setTextColor(getColor(R.color.primary));
            tmp.setBackground(getDrawable(R.drawable.round_select_border2));
            tmp.setTextSize(16);
            tmp.setPadding(10,15,10,15);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(300,FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
            tmp.setLayoutParams(params);
            tmp.setGravity(Gravity.CENTER);
            practice_keyword_flexbox.addView(tmp);
        }
        practice_layout = findViewById(R.id.practice_layout);
        practice_keyword_layout = findViewById(R.id.practice_keyword_layout);
        practice_script_layout = findViewById(R.id.practice_script_layout);
        practice_change_layout = findViewById(R.id.practice_change_layout);
        practice_change_statetext = findViewById(R.id.practice_change_statetext);

        practice_change_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change == 0){
                    practice_change_statetext.setText("현재 상태 : 키워드만 보이기");
                    practice_layout.removeView(practice_script_layout);
                }
                else if(change ==1){
                    practice_change_statetext.setText("현재 상태 : 대본만 보이기");
                    practice_layout.addView(practice_script_layout);
                    practice_layout.removeView(practice_keyword_layout);
                }
                else if(change == 2){
                    practice_change_statetext.setText("현재 상태 : 아무것도 보지않기");
                    practice_layout.removeView(practice_script_layout);
                }
                else{
                    practice_change_statetext.setText("현재 상태 : 둘다 보이기");
                    practice_layout.addView(practice_keyword_layout);
                    practice_layout.addView(practice_script_layout);
                }
                change=(change+1)%4;
            }
        });



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
                    practice_record.setImageDrawable(getDrawable(R.drawable.ic_start));
                    mediaRecorder.pause();
                }
                else{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        isRecording = true;
                        practice_record.setImageDrawable(getDrawable(R.drawable.ic_pause));
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
                        practice_record.setImageDrawable(getDrawable(R.drawable.ic_start));
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
                                practice_script_text.setText(response.body().toString());

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