package com.kospeech.speechteacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AnalysisActivity extends AppCompatActivity {
    ImageButton analysis_back;
    TextView analysis_practice_time,analysis_presentation_time;
    RadarChart analysis_radar;
    private PresentationResult presentationResult;
    private PresentationItem presentationItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        Intent intent = getIntent();
        presentationResult = (PresentationResult) intent.getSerializableExtra("presentationResult");
        presentationItem = (PresentationItem) intent.getSerializableExtra("presentationItem");
        int practice_time = intent.getIntExtra("practice_time",0);

        analysis_back = findViewById(R.id.analysis_back);
        analysis_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        analysis_practice_time = findViewById(R.id.analysis_practice_time);
        analysis_presentation_time = findViewById(R.id.analysis_presentation_time);
        analysis_practice_time.setText(Integer.toString((int)(practice_time/60)) +"분 "+Integer.toString((int)(practice_time%60))+"초" );
        String presentation_time = presentationItem.getPresentation_time();
        analysis_presentation_time.setText(presentation_time.substring(0,2)+"시 "+presentation_time.substring(3,5)+"분 00초");


        analysis_radar = findViewById(R.id.analysis_radar);
        ArrayList<RadarEntry> visitors = new ArrayList<>();
        visitors.add(new RadarEntry(10));
        visitors.add(new RadarEntry(5));
        visitors.add(new RadarEntry(13));
        visitors.add(new RadarEntry(8));
        visitors.add(new RadarEntry(11));
        visitors.add(new RadarEntry(3));

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
        analysis_radar.getYAxis().setEnabled(false);
        analysis_radar.getDescription().setEnabled(false);
        analysis_radar.setRotationEnabled(false);
        analysis_radar.setData(radarData);





    }
}