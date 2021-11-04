package com.kospeech.speechteacher;

import static java.lang.Math.max;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalysisActivity extends AppCompatActivity {
    ImageButton analysis_back;
    TextView analysis_practice_time,analysis_presentation_time;
    TextView analysis_dupword_status,analysis_unsuitable_status,analysis_gap_status,analysis_tune_status,analysis_speed_status,analysis_fillerwords_status;
    RadarChart analysis_radar;
    LinearLayout analysis_dupword,analysis_unsuitable,analysis_gap,analysis_tune,analysis_speed,analysis_fillerwords;
    private PresentationResult presentationResult;
    private PresentationItem presentationItem;
    private String audiofile_url;
    private int count=0;
    TextView analysis_grade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Intent intent = getIntent();
        presentationResult = (PresentationResult) intent.getSerializableExtra("presentationResult");
        presentationItem = (PresentationItem) intent.getSerializableExtra("presentationItem");
        audiofile_url = intent.getStringExtra("audiofile_url");
        int practice_time = intent.getIntExtra("practice_time",0);

        analysis_back = findViewById(R.id.analysis_back);
        analysis_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        analysis_practice_time = findViewById(R.id.analysis_practice_time);
        analysis_presentation_time = findViewById(R.id.analysis_presentation_time);
        analysis_practice_time.setText(Integer.toString((int)(practice_time/60)) +"분 "+Integer.toString((int)(practice_time%60))+"초" );
        String presentation_time = presentationItem.getPresentation_time();
        analysis_presentation_time.setText(presentation_time.substring(0,2)+"분 "+presentation_time.substring(3,5)+"초");

        analysis_dupword_status =findViewById(R.id.analysis_dupword_status);
        analysis_unsuitable_status =findViewById(R.id.analysis_unsuitable_status);
        analysis_gap_status =findViewById(R.id.analysis_gap_status);
        analysis_tune_status = findViewById(R.id.analysis_tune_status);
        analysis_speed_status = findViewById(R.id.analysis_speed_status);
        analysis_fillerwords_status = findViewById(R.id.analysis_fillerwords_status);

        analysis_dupword = findViewById(R.id.analysis_dupword);
        analysis_unsuitable = findViewById(R.id.analysis_unsuitable);
        analysis_gap = findViewById(R.id.analysis_gap);
        analysis_tune = findViewById(R.id.analysis_tune);
        analysis_speed = findViewById(R.id.analysis_speed);
        analysis_fillerwords = findViewById(R.id.analysis_fillerwords);

        if(!presentationResult.getDuplicatedWords().isEmpty()){
            count += presentationResult.getDuplicatedWords().size();
            analysis_dupword_status.setText("검출");
            analysis_dupword_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_dupword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",1);
                detailintent.putExtra("presentationResult",presentationResult);
                startActivity(detailintent);
            }
        });

        if(!presentationResult.getUnsuitableWords().isEmpty()){
            count += presentationResult.getUnsuitableWords().size();
            analysis_unsuitable_status.setText("검출");
            analysis_unsuitable_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_unsuitable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",2);
                detailintent.putExtra("presentationResult",presentationResult);
                startActivity(detailintent);
            }
        });

        if(!presentationResult.getGap().isEmpty()){
            count += presentationResult.getGap().size();
            analysis_gap_status.setText("검출");
            analysis_gap_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_gap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",3);
                detailintent.putExtra("presentationResult",presentationResult);
                detailintent.putExtra("audiofile_url",audiofile_url);
                startActivity(detailintent);
            }
        });

        if(!presentationResult.getTune().isEmpty()){
            count += presentationResult.getTune().size();
            analysis_tune_status.setText("검출");
            analysis_tune_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_tune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",4);
                detailintent.putExtra("presentationResult",presentationResult);
                detailintent.putExtra("audiofile_url",audiofile_url);
                startActivity(detailintent);
            }
        });

        if(!presentationResult.getSpeed().isEmpty()){
            count += presentationResult.getSpeed().size();
            analysis_speed_status.setText("검출");
            analysis_speed_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",5);
                detailintent.putExtra("presentationResult",presentationResult);
                detailintent.putExtra("audiofile_url",audiofile_url);
                startActivity(detailintent);
            }
        });
        Map<String,List<Integer>> fillerWords = presentationResult.getFillerWords();
        Set<Integer> fillerwordslist = new HashSet<>();
        for(Integer i : fillerWords.get("음")){
            fillerwordslist.add(i);
        }
        for(Integer i : fillerWords.get("어")){
            fillerwordslist.add(i);
        }
        for(Integer i : fillerWords.get("그")){
            fillerwordslist.add(i);
        }

        if(!fillerwordslist.isEmpty()){
            count += fillerwordslist.size();
            analysis_fillerwords_status.setText("검출");
            analysis_fillerwords_status.setBackground(getDrawable(R.drawable.analysis_status_bad));
        }
        analysis_fillerwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailintent = new Intent(view.getContext(),AnalysisDetailActivity.class);
                detailintent.putExtra("kind",6);
                detailintent.putExtra("presentationResult",presentationResult);
                detailintent.putExtra("audiofile_url",audiofile_url);
                startActivity(detailintent);
            }
        });





        analysis_radar = findViewById(R.id.analysis_radar);
        ArrayList<RadarEntry> visitors = new ArrayList<>();
        int point;
        point = max(12-presentationResult.getDuplicatedWords().size(),0);
        visitors.add(new RadarEntry(point));
        point = max(12-presentationResult.getUnsuitableWords().size(),0);
        visitors.add(new RadarEntry(point));
        point = max(12-presentationResult.getGap().size(),0);
        visitors.add(new RadarEntry(point));
        point = max(12-presentationResult.getTune().size(),0);
        visitors.add(new RadarEntry(point));
        point = max(12-presentationResult.getSpeed().size(),0);
        visitors.add(new RadarEntry(point));
        point = max(12-presentationResult.getFillerWords().size(),0);
        visitors.add(new RadarEntry(point));

        RadarDataSet radarDataSet = new RadarDataSet(visitors,"visitors");
        radarDataSet.setColor(getColor(R.color.primary));
        radarDataSet.setLineWidth(2f);
        radarDataSet.setDrawValues(false);
        radarDataSet.setFillColor(getColor(R.color.primary));
        radarDataSet.setFillAlpha(110);
        radarDataSet.setDrawFilled(true);


        RadarData radarData = new RadarData();
        radarData.addDataSet(radarDataSet);

        String[] labels = {"중복단어","부적절단어","뜸들이기","음정변화","빠르기","Filler Words"};
        analysis_radar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        analysis_radar.getLegend().setEnabled(false);
        analysis_radar.getYAxis().setAxisMinimum(0);
        analysis_radar.getYAxis().setEnabled(false);
        analysis_radar.getDescription().setEnabled(false);
        analysis_radar.setRotationEnabled(false);
        analysis_radar.setData(radarData);


        analysis_grade = findViewById(R.id.analysis_grade);
        if(count <5)
            analysis_grade.setText("😁 좋아요! 참 잘했어요!");
        else if(count <15)
            analysis_grade.setText("😅 조금만 더 노력해보아요!");

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("발표 결과 화면을 종료합니다.")
                .setTitle("발표 결과 화면 끄기")
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