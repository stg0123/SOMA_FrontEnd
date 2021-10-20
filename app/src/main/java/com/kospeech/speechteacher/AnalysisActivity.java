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

import org.w3c.dom.Text;

public class AnalysisActivity extends AppCompatActivity {
    ImageButton analysis_back;
    TextView analysis_practice_time,analysis_presentation_time;
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

    }
}