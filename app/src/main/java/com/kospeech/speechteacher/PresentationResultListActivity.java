package com.kospeech.speechteacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class PresentationResultListActivity extends AppCompatActivity {
    TextView resultlist_title_text;
    Button resultlist_to_practice,resultlist_item;
    ImageButton resultlist_back;
    LinearLayout resultlist_list;
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
                finish();
            }
        });


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        resultlist_list = findViewById(R.id.resultlist_list);

        for (List<String> resultItem : presentationItem.getPresentation_result_info()) {
            View resultItemView = inflater.inflate(R.layout.presentation_result_list_item, null);
            Button Item = resultItemView.findViewById(R.id.detail_item_bar_count);
            Item.setText(resultItem.get(1));
            resultItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage(resultItem.get(0))
                            .setTitle("해당 객체 id")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            resultlist_list.addView(resultItemView);
        }




    }
}