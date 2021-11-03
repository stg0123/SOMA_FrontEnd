package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import static java.lang.Math.max;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AnalysisDetailActivity extends AppCompatActivity {
    ImageButton analysis_detail_back;
    LinearLayout analysis_detail_barlist,analysis_detail_itemlist;
    TextView analysis_detail_content,analysis_detail_explain_title,analysis_detail_explain,analysis_detail_count;

    private PresentationResult presentationResult;
    private String audiofile_url;
    private int kind;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        analysis_detail_barlist = findViewById(R.id.analysis_detail_barlist);
        analysis_detail_itemlist = findViewById(R.id.analysis_detail_itemlist);
        Intent intent = getIntent();
        presentationResult = (PresentationResult) intent.getSerializableExtra("presentationResult");
        audiofile_url = intent.getStringExtra("audiofile_url");
        kind = intent.getIntExtra("kind",0);

        mediaPlayer = new MediaPlayer();

        if(audiofile_url == null)
            audiofile_url = getExternalFilesDir(null) + "/record.m4a";

        Log.d(TAG, "onCreate: "+audiofile_url);

        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(audiofile_url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }





        analysis_detail_back = findViewById(R.id.analysis_detail_back);
        analysis_detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        analysis_detail_content = findViewById(R.id.analysis_detail_content);
        analysis_detail_explain_title = findViewById(R.id.analysis_detail_explain_title);
        analysis_detail_explain = findViewById(R.id.analysis_detail_explain);
        analysis_detail_count = findViewById(R.id.analysis_detail_count);




        if(kind == 1) {
            Map<String,Integer> dupwords = presentationResult.getDuplicatedWords();
            analysis_detail_content.setText("※ 중복 단어 사용");
            analysis_detail_explain_title.setText("중복 단어 사용이란?");
            analysis_detail_explain.setText("발표 연습 진행중 3회 이상 사용된 단어의 종류와 횟수를 확인해 볼 수 있습니다.");
            if(dupwords.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            }
            else {
                analysis_detail_count.setText("중복 단어 사용 검출 개수 : " + dupwords.size() + "개");
                ArrayList<dupword> sortdup = new ArrayList<>();
                for (String key : dupwords.keySet()) {
                    sortdup.add(new dupword(dupwords.get(key), key));
                }
                Collections.sort(sortdup, Collections.reverseOrder());
                int i = 0;
                for (dupword dup : sortdup) {
                    if (i >= 5)
                        break;

                    View barItemView = inflater.inflate(R.layout.analysis_detail_item_bar, null);
                    TextView detail_item_bar_text = barItemView.findViewById(R.id.detail_item_bar_text);
                    detail_item_bar_text.setText((i + 1) + ". " + dup.dup_word);

                    TextView detail_item_bar_count = barItemView.findViewById(R.id.detail_item_bar_count);
                    detail_item_bar_count.setText(dup.dup_num + "회");
                    ImageView detail_item_bar_img = barItemView.findViewById(R.id.detail_item_bar_img);
                    if (i == 0) {
                        detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar1));
                    } else if (i == 1) {
                        detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar2));
                    } else if (i == 2) {
                        detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar3));
                    } else if (i == 3) {
                        detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar4));
                    } else if (i == 4) {
                        detail_item_bar_img.setBackground(getDrawable(R.drawable.detail_bar5));
                    }
                    barItemView.setPadding(40, 20, 40, 20);
                    analysis_detail_barlist.addView(barItemView);
                    i++;
                }
                i = 1;
                for (String key : dupwords.keySet()) {
                    View item = inflater.inflate(R.layout.analysis_detail_item_normal, null);
                    TextView detail_item_count = item.findViewById(R.id.detail_item_count);
                    detail_item_count.setText(dupwords.get(key) + "회");
                    String number = Integer.toString(i);
                    if (i < 10)
                        number = "0" + number;
                    TextView detail_item_num = item.findViewById(R.id.detail_item_num);
                    detail_item_num.setText(number + ".");
                    TextView detail_item_text = item.findViewById(R.id.detail_item_text);
                    detail_item_text.setText(key);

                    item.setPadding(0, 30, 0, 30);
                    analysis_detail_itemlist.addView(item);
                    i++;
                }
            }
        }
        else if(kind == 2){
            Map<String, List<String>> unsuitableWords = presentationResult.getUnsuitableWords();
            analysis_detail_content.setText("※ 부적절 단어 사용");
            analysis_detail_explain_title.setText("부적절 단어 사용이란?");
            analysis_detail_explain.setText("발표 연습 진행중 부적절하게 사용된 단어의 종류와 추천 대체 단어를 확인해 볼 수 있습니다.");
            if(unsuitableWords.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            }
            else {
                analysis_detail_count.setText("부적절단어 사용 검출 개수 : " + unsuitableWords.size() + "개");
                int i = 1;
                for (String key : unsuitableWords.keySet()) {
                    View item = inflater.inflate(R.layout.analysis_detail_item_link, null);
                    TextView detail_item_link_text = item.findViewById(R.id.detail_item_link_text);
                    detail_item_link_text.setText(key);
                    String number = String.valueOf(i);
                    if (i < 10)
                        number = "0" + number;
                    TextView detail_item_link_num = item.findViewById(R.id.detail_item_link_num);
                    detail_item_link_num.setText(number + ".");

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage(unsuitableWords.get(key).toString())
                                    .setTitle("추천 대체 단어")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    item.setPadding(0, 30, 0, 30);
                    analysis_detail_itemlist.addView(item);
                    i++;
                }
            }
        }
        else if(kind == 3){
            List<List<Integer>> gap = presentationResult.getGap();
            analysis_detail_content.setText("※ 뜸들이기");
            analysis_detail_explain_title.setText("뜸들이기란?");
            analysis_detail_explain.setText("발표 연습 진행중 일정 시간 말을 하지 않고 뜸들인 구간의 횟수와 검출시간을 확인해 볼 수 있습니다.");
            if(gap.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            }
            else {
                for (int i = 0; i < gap.size(); i++) {
                    View item = inflater.inflate(R.layout.analysis_detail_item_link, null);
                    TextView detail_item_link_text = item.findViewById(R.id.detail_item_link_text);
                    int start = max(gap.get(i).get(0) - 1, 0);
                    String second = gap.get(i).get(0).toString() + "~" + gap.get(i).get(1).toString() + "초";
                    detail_item_link_text.setText(second);
                    String number = String.valueOf(i + 1);
                    if (i < 9)
                        number = "0" + number;
                    TextView detail_item_link_num = item.findViewById(R.id.detail_item_link_num);
                    detail_item_link_num.setText(number + ".");

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            mediaPlayer.seekTo(start * 1000);
                            mediaPlayer.start();
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("해당구간 -1초부터 다시 듣습니다")
                                    .setTitle("발표연습 다시듣기")
                                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mediaPlayer.pause();
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    item.setPadding(0, 30, 0, 30);
                    analysis_detail_itemlist.addView(item);
                }
            }
        }
        else if(kind == 4){
            List<Integer> tune = presentationResult.getTune();
            analysis_detail_content.setText("※ 음정급격변화");
            analysis_detail_explain_title.setText("음정급격변화란?");
            analysis_detail_explain.setText("발표 연습 진행중 급격하게 음정이 높아지거나 낮아진 부분의 횟수와 검출시간을 확인해 볼 수 있습니다.");
            if(tune.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            }
            else {
                analysis_detail_count.setText("음정급격변화 검출 개수 : " + tune.size() + "개");
                for (int i = 0; i < tune.size(); i++) {
                    View item = inflater.inflate(R.layout.analysis_detail_item_link, null);
                    TextView detail_item_link_text = item.findViewById(R.id.detail_item_link_text);
                    int start = max(tune.get(i) - 1, 0);
                    detail_item_link_text.setText(tune.get(i).toString() + "초");
                    String number = String.valueOf(i + 1);
                    if (i < 9)
                        number = "0" + number;
                    TextView detail_item_link_num = item.findViewById(R.id.detail_item_link_num);
                    detail_item_link_num.setText(number + ".");

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mediaPlayer.seekTo(start * 1000);
                            mediaPlayer.start();
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("해당구간 -1초부터 다시 듣습니다")
                                    .setTitle("발표연습 다시듣기")
                                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mediaPlayer.pause();
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    item.setPadding(0, 30, 0, 30);
                    analysis_detail_itemlist.addView(item);
                }
            }
        }
        else if(kind == 5) {
            List<List<Integer>> speed = presentationResult.getSpeed();
            analysis_detail_content.setText("※ 말빠르기");
            analysis_detail_explain_title.setText("말빠르기란?");
            analysis_detail_explain.setText("발표 연습 진행중 말이 빨라지거나 느려진 횟수와 검출시간을 확인해 볼 수 있습니다.");
            if (speed.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            } else {
                analysis_detail_count.setText("말빠르기 검출 개수 : " + speed.size() + "개");
                for (int i = 0; i < speed.size(); i++) {
                    View item = inflater.inflate(R.layout.analysis_detail_item_link, null);
                    TextView detail_item_link_text = item.findViewById(R.id.detail_item_link_text);
                    int start = max(speed.get(i).get(0) - 1, 0);
                    String second = speed.get(i).get(0).toString() + "초 ";
                    if (speed.get(i).get(1).equals(0))
                        second += "(빠름)";
                    else
                        second += "(느림)";

                    detail_item_link_text.setText(second);
                    String number = String.valueOf(i + 1);
                    if (i < 9)
                        number = "0" + number;
                    TextView detail_item_link_num = item.findViewById(R.id.detail_item_link_num);
                    detail_item_link_num.setText(number + ".");

                    item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mediaPlayer.seekTo(start * 1000);
                            mediaPlayer.start();
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setMessage("해당구간 -1초부터 다시 듣습니다")
                                    .setTitle("발표연습 다시듣기")
                                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mediaPlayer.pause();
                                            dialogInterface.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                    item.setPadding(0, 30, 0, 30);
                    analysis_detail_itemlist.addView(item);
                }
            }
        }
        else if(kind ==6){
            Map<String,List<Integer>> fillerWords = presentationResult.getFillerWords();
            analysis_detail_content.setText("※ filler words");
            analysis_detail_explain_title.setText("filler words 란?");
            analysis_detail_explain.setText("발표 연습 진행중 '음..' '어..' '그..' 등의 빈공간을 채우기 위해 습관적으로 발음을 끄는 부분을 확인할 수 있습니다.");
            if (fillerWords.isEmpty()) {
                analysis_detail_count.setText("검출 내용이 없습니다");
            }else{
                TextView item =new TextView(this);
                item.setText(fillerWords.toString());
                analysis_detail_itemlist.addView(item);

            }



        }



    }

    public class dupword implements Comparable<dupword> {
        private Integer dup_num;
        private String dup_word;

        public dupword(Integer dup_num, String dup_word) {
            this.dup_num = dup_num;
            this.dup_word = dup_word;
        }

        @Override
        public int compareTo(dupword dupword) {
            return this.dup_num.compareTo(dupword.dup_num);
        }
    }


}