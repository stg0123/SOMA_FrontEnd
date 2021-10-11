package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresentationPracticeActivity extends AppCompatActivity {

    ImageView practice_out,practice_analysis;
    ImageButton practice_record;
    TextView anlysis_text;

    private boolean isRecording = false;
    private int RECORD_PERMISSION_CODE = 21;
    AudioRecord audioRecord = null;
    private int mSampleRate = 44100;
    private int mChannelConfig = AudioFormat.CHANNEL_IN_STEREO;
    private int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int mBufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelConfig, mAudioFormat);

    private MediaRecorder recorder = null;
    private String filename;
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

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "recorded.mp4");
        filename = file.getAbsolutePath();

        practice_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        practice_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording){
                    isRecording = false;
                    practice_record.setImageDrawable(getDrawable(R.drawable.start_ic));
                    audioRecord.stop();
                }
                else{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        isRecording= true;
                        practice_record.setImageDrawable(getDrawable(R.drawable.stop_ic));
//                        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, mSampleRate, mChannelConfig, mAudioFormat, mBufferSize);
//                        audioRecord.startRecording();

                        recorder = new MediaRecorder();
                        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        recorder.setOutputFile(filename);
                        try {
                            recorder.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        recorder.start();
                    } else {
                        ActivityCompat.requestPermissions(PresentationPracticeActivity.this , new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_PERMISSION_CODE);
                    }
                }
            }
        });

        practice_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording){
                    isRecording=false;
                    practice_record.setImageDrawable(getDrawable(R.drawable.start_ic));
                    recorder.stop();
                }
                recorder.release();
                recorder = null;
                RequestBody requestBody = RequestBody.create(MediaType.parse("audio/*"), file );

                MultipartBody.Part filePart = MultipartBody.Part.createFormData("audio_file", "recorded.mp4", requestBody);
                retrofitService.presentationresult(filePart).enqueue(new Callback<PresentationResult>() {
                    @Override
                    public void onResponse(Call<PresentationResult> call, Response<PresentationResult> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            anlysis_text.setText(response.body().toString());

                        }else {
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
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 연습 내용이 모두 사라집니다. 뒤로 가시겠습니까?")
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

    public class PresentationResult{
        @SerializedName("duplicatedWords")
        private List<Map<String,Integer>> duplicatedWords;
        @SerializedName("unsuitableWords")
        private List<Map<String,List<String>>> unsuitableWords;
        @SerializedName("gap")
        private List<List<Float>> gap;
        @SerializedName("speed")
        private List<List<Float>> speed;
        @SerializedName("tune")
        private List<Float> tune;

        @Override
        public String toString() {
            return "PresentationResult{" +
                    "duplicatedWords=" + duplicatedWords +
                    ", unsuitableWords=" + unsuitableWords +
                    ", gap=" + gap +
                    ", speed=" + speed +
                    ", tune=" + tune +
                    '}';
        }
    }



}