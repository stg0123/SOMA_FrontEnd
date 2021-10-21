package com.kospeech.speechteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AnalysisDetailActivity extends AppCompatActivity {
    ImageButton analysis_detail_back;
    LinearLayout analysis_detail_barlist,analysis_detail_itemlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        analysis_detail_barlist = findViewById(R.id.analysis_detail_barlist);
        analysis_detail_itemlist = findViewById(R.id.analysis_detail_itemlist);

        analysis_detail_back = findViewById(R.id.analysis_detail_back);
        analysis_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        for(int i=0;i<5;i++) {
            View barItemView = inflater.inflate(R.layout.analysis_detail_item_bar, null);
            TextView textView = barItemView.findViewById(R.id.detail_item_bar_count);
            textView.setText(Integer.toString(i)+"회");
            barItemView.setPadding(40, 20, 40, 20);
            analysis_detail_barlist.addView(barItemView);
        }
        for(int i=0;i<5;i++){
            View item = inflater.inflate(R.layout.analysis_detail_item_normal,null);
            TextView textView = item.findViewById(R.id.detail_item_count);
            textView.setText(i+"회");
            item.setPadding(0,30,0,30);
            analysis_detail_itemlist.addView(item);

        }


    }
}