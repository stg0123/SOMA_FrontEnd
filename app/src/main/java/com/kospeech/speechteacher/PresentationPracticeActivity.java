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
import android.content.Intent;
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
    private File file;
    private MediaRecorder mediaRecorder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_practice);
        practice_out = findViewById(R.id.practice_out);
        practice_record = findViewById(R.id.practice_record);
        practice_analysis = findViewById(R.id.practice_analysis);
        practice_script_text = findViewById(R.id.practice_script_text);

        file = new File(getExternalFilesDir(null),"record.mp3");

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
        practice_script_text.setText("형사피해자는 법률이 정하는 바에 의하여 당해 사건의 재판 절차에서 진술할 수 있다. 대통령은 조국의 평화적 통일을 위한 성실한 의무를 진다. 국무회의는 대통령·국무총리와15인 이상 30인 이하의 국무위원으로 구성한다.\n" +
                "국회에 제출된 법률안 기타의 의안은 회기중에 의결되지 못한 이유로 폐기되지 아니한다.");


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
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // ACC MPEG_4
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
                startanalysis(view);
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

    public void startanalysis(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("발표연습을 종료하고 분석을 시작합니다.")
                .setTitle("발표연습을 완료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mediaRecorder!=null) {
                            if(isRecording) {
                                isRecording = false;
                                practice_record.setImageDrawable(getDrawable(R.drawable.ic_start));
                            }
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            view.getContext().startActivity(new Intent(view.getContext(),AnalysisLoadingActivity.class));

                        }
                        else{
                            Toast.makeText(view.getContext(), "아직 발표를 시작하지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }
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
