package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresentationResultListActivity extends AppCompatActivity {
    TextView resultlist_title_text;
    Button resultlist_to_practice;
    ImageButton resultlist_back;
    LinearLayout resultlist_list;
    private int resultlistsize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_result_list);
        PresentationItem presentationItem = (PresentationItem) getIntent().getSerializableExtra("presentationItem");


        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        resultlist_title_text = findViewById(R.id.resultlist_title_text);
        resultlist_title_text.setText(presentationItem.getPresntation_title());


        resultlist_back = findViewById(R.id.resultlist_back);
        resultlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resultlist_list = findViewById(R.id.resultlist_list);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        retrofitService.getpresentationresult(presentationItem.getPresentation_id()).enqueue(new Callback<List<PresentationResultInfo>>() {
            @Override
            public void onResponse(Call<List<PresentationResultInfo>> call, Response<List<PresentationResultInfo>> response) {
                if(response.isSuccessful() && response.body()!=null) {
                    List<PresentationResultInfo> presentationResultInfos=response.body();
                    resultlistsize = presentationResultInfos.size();
                    for(int i=0;i<presentationResultInfos.size();i++) {
                        View item = inflater.inflate(R.layout.presentation_result_list_item, null);
                        Button presentation_result_item = item.findViewById(R.id.presentation_result_item);
                        String time = presentationResultInfos.get(i).getPresentation_result_date();
                        presentation_result_item.setText(time.substring(0,10)+" "+time.substring(11,13)+":"+time.substring(14,16));

                        PresentationResult presentationResult = presentationResultInfos.get(i).getPresentation_result();
                        String audiofile_url = presentationResultInfos.get(i).getAudiofile_url();
                        int practice_time = presentationResultInfos.get(i).getPresentation_result_time();

                        presentation_result_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(view.getContext(),AnalysisActivity.class);
                                intent.putExtra("presentationResult",presentationResult);
                                intent.putExtra("presentationItem",presentationItem);
                                intent.putExtra("audiofile_url",audiofile_url);
                                intent.putExtra("practice_time",practice_time);
                                startActivity(intent);
                            }
                        });

                        resultlist_list.addView(item);

                    }
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
            public void onFailure(Call<List<PresentationResultInfo>> call, Throwable t) {
                Log.d(TAG, "onFailure: connection fail");
            }
        });



        resultlist_to_practice = findViewById(R.id.resultlist_to_practice);
        resultlist_to_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PresentationPracticeActivity.class);
                intent.putExtra("presentationItem",presentationItem);
                intent.putExtra("resultlistsize",resultlistsize);
                startActivity(intent);
            }
        });




    }
}