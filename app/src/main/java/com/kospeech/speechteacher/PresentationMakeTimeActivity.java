package com.kospeech.speechteacher;

import static java.lang.Math.max;
import static java.lang.Math.min;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PresentationMakeTimeActivity extends AppCompatActivity {
    ImageButton presentation_make_time_back;

    TextView presentation_make_time_min,presentation_make_time_sec,presentation_make_time_finish;
    Button presentation_make_time_plus_5min,presentation_make_time_plus_1min,presentation_make_time_plus_30sec;
    Button presentation_make_time_minus_5min,presentation_make_time_minus_1min,presentation_make_time_minus_30sec;
    private int cur_min=0,cur_sec=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make_time);
        String time = getIntent().getStringExtra("time");
        presentation_make_time_min = findViewById(R.id.presentation_make_time_min);
        presentation_make_time_sec = findViewById(R.id.presentation_make_time_sec);
        if(time!= null) {
            presentation_make_time_min.setText(time.substring(0, 2));
            cur_min = Integer.parseInt(time.substring(0,2));
            presentation_make_time_sec.setText(time.substring(3));
            cur_sec = Integer.parseInt(time.substring(3));
        }
        presentation_make_time_plus_5min = findViewById(R.id.presentation_make_time_plus_5min);
        presentation_make_time_plus_5min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_min +=5;
                cur_min = min(cur_min,99);
                settingTime();
            }
        });
        presentation_make_time_plus_1min = findViewById(R.id.presentation_make_time_plus_1min);
        presentation_make_time_plus_1min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_min++;
                cur_min = min(cur_min,99);
                settingTime();
            }
        });
        presentation_make_time_plus_30sec = findViewById(R.id.presentation_make_time_plus_30sec);
        presentation_make_time_plus_30sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_sec+=30;
                if(cur_sec==60){
                    cur_sec=0;
                    cur_min++;
                    cur_min = min(cur_min,99);
                }
                settingTime();
            }
        });
        presentation_make_time_minus_5min = findViewById(R.id.presentation_make_time_minus_5min);
        presentation_make_time_minus_5min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_min-=5;
                cur_min = max(cur_min,0);
                settingTime();

            }
        });
        presentation_make_time_minus_1min = findViewById(R.id.presentation_make_time_minus_1min);
        presentation_make_time_minus_1min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cur_min--;
                cur_min = max(cur_min,0);
                settingTime();
            }
        });
        presentation_make_time_minus_30sec = findViewById(R.id.presentation_make_time_minus_30sec);
        presentation_make_time_minus_30sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur_sec>0)
                    cur_sec-=30;
                else if(cur_min>0){
                    cur_min--;
                    cur_sec=30;
                }
                settingTime();
            }
        });


        presentation_make_time_finish = findViewById(R.id.presentation_make_time_finish);
        presentation_make_time_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String minute = Integer.toString(cur_min);
                String second = Integer.toString(cur_sec);
                if(cur_min<10)
                    minute = "0"+minute;
                if(cur_sec<10)
                    second = "0"+cur_sec;
                Intent intent = new Intent();
                intent.putExtra("time",minute+":"+second);
                setResult(RESULT_OK,intent);
                finish();

            }
        });




        presentation_make_time_back = findViewById(R.id.presentation_make_time_back);
        presentation_make_time_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 발표시간 수정내용이 반영되지 않습니다.")
                .setTitle("발표시간 입력을 중시하시겠습니까?")
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

    protected void settingTime(){
        String minute = Integer.toString(cur_min);
        if(cur_min<10)
            minute="0"+minute;
        presentation_make_time_min.setText(minute);
        String second = Integer.toString(cur_sec);
        if(cur_sec <10)
            second = "0"+second;
        presentation_make_time_sec.setText(second);

    }


}

