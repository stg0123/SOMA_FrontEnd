package com.kospeech.speechteacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Map;

public class AnalysisDetailActivity extends AppCompatActivity {
    ImageButton analysis_detail_back;
    LinearLayout analysis_detail_barlist,analysis_detail_itemlist;
    private PresentationResult presentationResult;
    private int kind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        analysis_detail_barlist = findViewById(R.id.analysis_detail_barlist);
        analysis_detail_itemlist = findViewById(R.id.analysis_detail_itemlist);
        Intent intent = getIntent();
        presentationResult = (PresentationResult) intent.getSerializableExtra("presentationResult");
        kind = intent.getIntExtra("kind",0);

        analysis_detail_back = findViewById(R.id.analysis_detail_back);
        analysis_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(kind == 1) {
            Map<String,Integer> dupwords = presentationResult.getDuplicatedWords();
            int i=0;
            for (String key : dupwords.keySet()) {
                if(i>=5)
                    break;

                View barItemView = inflater.inflate(R.layout.analysis_detail_item_bar, null);
                TextView detail_item_bar_text = barItemView.findViewById(R.id.detail_item_bar_text);
                detail_item_bar_text.setText(Integer.toString(i+1)+". "+key);

                TextView detail_item_bar_count = barItemView.findViewById(R.id.detail_item_bar_count);
                detail_item_bar_count.setText(Integer.toString(dupwords.get(key)) + "회");
                ImageView detail_item_bar_img= barItemView.findViewById(R.id.detail_item_bar_img);
                if(i==0) {
                    detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar1));
                }
                else if(i==1){
                    detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar2));
                }
                else if(i==2){
                    detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar3));
                }
                else if(i==3){
                    detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar4));
                }
                else if(i==4){
                    detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar5));
                }
                barItemView.setPadding(40, 20, 40, 20);
                analysis_detail_barlist.addView(barItemView);
                i++;
            }
            i=0;
            for (String key : dupwords.keySet()){
                View item = inflater.inflate(R.layout.analysis_detail_item_normal,null);
                TextView detail_item_count = item.findViewById(R.id.detail_item_count);
                detail_item_count.setText(Integer.toString(dupwords.get(key)) + "회");
                String number = Integer.toString(i+1);
                if(i<10)
                    number = "0"+number;
                TextView detail_item_num = item.findViewById(R.id.detail_item_num);
                detail_item_num.setText(number+".");
                TextView detail_item_text = item.findViewById(R.id.detail_item_text);
                detail_item_text.setText(key);

                item.setPadding(0,30,0,30);
                analysis_detail_itemlist.addView(item);
                i++;
            }
        }
        else{


        }


    }
}