package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class KnowhowActivity extends AppCompatActivity {
    TextView knowhow_detail_title,knowhow_detail_contents;
    ImageButton knowhow_detail_back;
    ImageView knowhow_detail_img;
    KnowhowItem knowhowItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowhow);

        knowhowItem = (KnowhowItem) getIntent().getSerializableExtra("knowhowitem");


        knowhow_detail_title = findViewById(R.id.knowhow_detail_title);
        knowhow_detail_title.setText(knowhowItem.getKnowhow_title());

        knowhow_detail_back = findViewById(R.id.knowhow_detail_back);
        knowhow_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        knowhow_detail_img = findViewById(R.id.knowhow_detail_img);
        Glide.with(this).load(knowhowItem.getKnowhow_img_url()).into(knowhow_detail_img);

        knowhow_detail_contents = findViewById(R.id.knowhow_detail_contents);
        knowhow_detail_contents.setText(knowhowItem.getKnowhow_contents());




    }
}