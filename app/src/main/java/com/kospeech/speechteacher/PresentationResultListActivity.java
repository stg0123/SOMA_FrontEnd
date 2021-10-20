package com.kospeech.speechteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PresentationResultListActivity extends AppCompatActivity {
    TextView resultlist_title_text;
    Button resultlist_to_practice;
    ImageButton resultlist_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_result_list);
        Intent getintent = getIntent();
        PresentationItem presentationItem = (PresentationItem) getintent.getSerializableExtra("presentationItem");
        resultlist_title_text = findViewById(R.id.resultlist_title_text);
        resultlist_title_text.setText(presentationItem.getPresntation_title());
        resultlist_back = findViewById(R.id.resultlist_back);
        resultlist_to_practice = findViewById(R.id.resultlist_to_practice);

        resultlist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        resultlist_to_practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),PresentationPracticeActivity.class);
                intent.putExtra("presentationItem",presentationItem);
                startActivity(intent);
            }
        });



    }
}