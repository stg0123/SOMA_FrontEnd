package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PresentationPracticeActivity extends AppCompatActivity {

    ImageView practice_out,practice_analysis;
    ImageButton practice_record;
    TextView practice_script_text,practice_change_statetext,practice_pagenum;
    FlexboxLayout practice_keyword_flexbox;
    LinearLayout practice_layout,practice_keyword_layout,practice_script_layout;
    ConstraintLayout practice_change_layout;
    ScrollView practice_script_scroll,practice_scroll;

    private int change =0;

    private boolean isRecording = false;
    private int RECORD_PERMISSION_CODE = 21;
    private File file;
    private MediaRecorder mediaRecorder = null;

    ConstraintLayout practice_presentation;
    TextView practice_time;
    Switch practice_switch;

    Timer timer;
    TimerTask timerTask;
    private Handler timerHandler;

    private int time=0;

    private PresentationItem presentationItem,presentation;
    private List<List<String>> presentation_keyword=null;
    private List<String> presentation_script =null;
    PDFView practice_presentation_pdf;

    View practice_presentation_left,practice_presentation_right;
    private RetrofitService retrofitService;
    private String presentation_id;
    private int resultlistsize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_practice);
        practice_out = findViewById(R.id.practice_out);
        practice_record = findViewById(R.id.practice_record);
        practice_analysis = findViewById(R.id.practice_analysis);

        file = new File(getExternalFilesDir(null),"record.m4a");
        presentationItem = (PresentationItem) getIntent().getSerializableExtra("presentationItem");
        presentation_id = presentationItem.getPresentation_id();

        resultlistsize = getIntent().getIntExtra("resultlistsize",0);

        practice_presentation_pdf = findViewById(R.id.practice_presentation_pdf);
        practice_presentation_left = findViewById(R.id.practice_presentation_left);
        practice_presentation_right = findViewById(R.id.practice_presentation_right);

        practice_keyword_flexbox = findViewById(R.id.practice_keyword_flexbox);
        practice_script_text = findViewById(R.id.practice_script_text);
        practice_pagenum = findViewById(R.id.practice_pagenum);

        practice_scroll = findViewById(R.id.practice_scroll);
        practice_script_scroll =findViewById(R.id.practice_script_scroll);
        practice_script_scroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                practice_scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        retrofitService.getpresentation(presentation_id).enqueue(new Callback<PresentationItem>() {
            @Override
            public void onResponse(Call<PresentationItem> call, Response<PresentationItem> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    presentation = response.body();
                    if(presentation.getPresentation_file_url()!=null)
                        new RetrivePDFfromUrl().execute(presentation.getPresentation_file_url());
                }
                else{
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
            public void onFailure(Call<PresentationItem> call, Throwable t) {
                Log.d(TAG, "onFailure: connection fail");
            }
        });



        practice_layout = findViewById(R.id.practice_layout);
        practice_keyword_layout = findViewById(R.id.practice_keyword_layout);
        practice_script_layout = findViewById(R.id.practice_script_layout);
        practice_change_layout = findViewById(R.id.practice_change_layout);
        practice_change_statetext = findViewById(R.id.practice_change_statetext);

        practice_change_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change == 0){
                    practice_change_statetext.setText("?????? ?????? : ???????????? ?????????");
                    practice_layout.removeView(practice_script_layout);
                }
                else if(change ==1){
                    practice_change_statetext.setText("?????? ?????? : ????????? ?????????");
                    practice_layout.addView(practice_script_layout);
                    practice_layout.removeView(practice_keyword_layout);
                }
                else if(change == 2){
                    practice_change_statetext.setText("?????? ?????? : ???????????? ????????????");
                    practice_layout.removeView(practice_script_layout);
                }
                else{
                    practice_change_statetext.setText("?????? ?????? : ?????? ?????????");
                    practice_layout.addView(practice_keyword_layout);
                    practice_layout.addView(practice_script_layout);
                }
                change=(change+1)%4;
            }
        });



        practice_presentation = findViewById(R.id.practice_presentation);
        practice_time = findViewById(R.id.practice_time);
        practice_switch = findViewById(R.id.practice_switch);
        practice_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    practice_presentation.addView(practice_time);
                }else{
                    practice_presentation.removeView(practice_time);
                }

            }
        });

        timer = new Timer();
        timerHandler = new Handler();
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
                    practice_switch.getThumbDrawable().setTint(getColor(R.color.primary));

                    timerTask.cancel();
                }
                else{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        isRecording = true;
                        practice_record.setImageDrawable(getDrawable(R.drawable.ic_pause));
                        practice_switch.getThumbDrawable().setTint(getColor(R.color.error));
                        timerTask = createTimerTask();
                        timer.schedule(timerTask,1000,1000);
                        if(mediaRecorder == null){
                            mediaRecorder = new MediaRecorder();
                            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); // ACC MPEG_4
                            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                            mediaRecorder.setAudioSamplingRate(44100);
                            mediaRecorder.setAudioEncodingBitRate(99600);
                            mediaRecorder.setOutputFile(file.getAbsolutePath());
                            try {
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            practice_analysis.setImageDrawable(getDrawable(R.drawable.ic_analysis_ok));
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
        builder.setMessage("?????? ?????? ??? ?????? ????????? ?????? ???????????????.")
                .setTitle("????????? ?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void refreshKeyword(int curpage){
        practice_keyword_flexbox.removeAllViews();
        if(presentation_keyword != null){
            if(!presentation_keyword.get(curpage).isEmpty()) {
                for (String str : presentation_keyword.get(curpage)) {
                    TextView tmp = new TextView(this);
                    tmp.setText(str);
                    tmp.setTextColor(getColor(R.color.primary));
                    tmp.setBackground(getDrawable(R.drawable.round_select_border2));
                    tmp.setTextSize(16);
                    tmp.setPadding(10, 15, 10, 15);
                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(300, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10, 10, 10, 10);
                    tmp.setLayoutParams(params);
                    tmp.setGravity(Gravity.CENTER);
                    practice_keyword_flexbox.addView(tmp);
                }
            }
        }
    }

    public void refreshScript(int curpage){
        practice_script_text.setText("");
        if(presentation_script != null){
            if(!presentation_script.get(curpage).equals(""))
                practice_script_text.setText(presentation_script.get(curpage));
        }
    }


    public void startanalysis(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("??????????????? ???????????? ????????? ???????????????.")
                .setTitle("??????????????? ?????????????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(mediaRecorder!=null) {
                            if(isRecording) {
                                isRecording = false;
                                practice_record.setImageDrawable(getDrawable(R.drawable.ic_start));
                                practice_switch.getThumbDrawable().setTint(getColor(R.color.primary));
                                timer.cancel();
                            }
                            mediaRecorder.stop();
                            mediaRecorder.release();
                            mediaRecorder = null;
                            Intent intent = new Intent(view.getContext(),AnalysisLoadingActivity.class);
                            intent.putExtra("presentationItem",presentationItem);
                            intent.putExtra("practice_time",time);
                            intent.putExtra("presentation_id",presentation_id);
                            intent.putExtra("resultlistsize",resultlistsize);
                            view.getContext().startActivity(intent);
                            finish();

                        }
                        else{
                            Toast.makeText(view.getContext(), "?????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private TimerTask createTimerTask() {
        TimerTask tT = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                timerHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        int m = time/60;
                        int s = time%60;
                        String localtime =  LocalTime.of(m,s).toString();
                        practice_time.setText(localtime);
                    }
                });
            }
        };
        return tT;
    }


    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            practice_presentation_pdf.fromStream(inputStream)
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .pageFling(true)
                    .enableDoubletap(false)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .autoSpacing(true)
                    .enableSwipe(false)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            practice_pagenum.setText((page+1)+"/"+pageCount);
                            refreshKeyword(page);
                            refreshScript(page);
                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            presentation_keyword = new ArrayList<>();
                            presentation_script = new ArrayList<>();
                            for(int i=0;i<nbPages;i++) {
                                presentation_keyword.add(new ArrayList<>());
                                presentation_script.add("");
                            }
                            retrofitService.getpresentationkeyword(presentation_id).enqueue(new Callback<List<PresentationKeyword>>() {
                                @Override
                                public void onResponse(Call<List<PresentationKeyword>> call, Response<List<PresentationKeyword>> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        List<PresentationKeyword> presentationKeywords = response.body();
                                        for(int i=0;i<presentationKeywords.size();i++){
                                            ArrayList<String> keywords_list =new ArrayList<>();
                                            String[] splitStr = presentationKeywords.get(i).keyword.split(",");
                                            for(String str : splitStr)
                                                keywords_list.add(str);
                                            presentation_keyword.set(presentationKeywords.get(i).keyword_page,keywords_list);
                                        }
                                        refreshKeyword(0);
                                    }
                                    else{
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
                                public void onFailure(Call<List<PresentationKeyword>> call, Throwable t) {
                                    Log.d(TAG, "onFailure: connection fail");
                                }
                            });

                            retrofitService.getpresentationscript(presentation_id).enqueue(new Callback<List<PresentationScript>>() {
                                @Override
                                public void onResponse(Call<List<PresentationScript>> call, Response<List<PresentationScript>> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        List<PresentationScript> presentationScripts = response.body();
                                        for(int i=0;i<presentationScripts.size();i++){
                                            presentation_script.set(presentationScripts.get(i).script_page,presentationScripts.get(i).script);
                                        }
                                        refreshScript(0);
                                    }
                                    else{
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
                                public void onFailure(Call<List<PresentationScript>> call, Throwable t) {
                                    Log.d(TAG, "onFailure: connection fail");
                                }
                            });

                        }

                    })
                    .load();

            practice_presentation_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    practice_presentation_pdf.jumpTo(practice_presentation_pdf.getCurrentPage()-1);
                    practice_presentation_pdf.performPageSnap();
                }
            });
            practice_presentation_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    practice_presentation_pdf.jumpTo(practice_presentation_pdf.getCurrentPage()+1);
                    practice_presentation_pdf.performPageSnap();
                }
            });
        }
    }

    public class PresentationKeyword{
        @SerializedName("keyword_page")
        private int keyword_page;
        @SerializedName("keyword_contents")
        private String keyword;

        @Override
        public String toString() {
            return "presentationKeyword{" +
                    "keyword_page=" + keyword_page +
                    ", keyword='" + keyword + '\'' +
                    '}';
        }
    }

    public class PresentationScript{
        @SerializedName("script_page")
        private int script_page;
        @SerializedName("script_contents")
        private String script;

        @Override
        public String toString() {
            return "presentationScript{" +
                    "keyword_page=" + script_page +
                    ", script='" + script + '\'' +
                    '}';
        }
    }



}
