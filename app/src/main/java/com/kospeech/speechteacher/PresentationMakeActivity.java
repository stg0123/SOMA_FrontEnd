package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class PresentationMakeActivity extends AppCompatActivity {
    ImageButton presentation_make_back;
    ConstraintLayout presentation_make_file,presentation_make_keyword;
    TextView presentation_make_file_setting;
    Button presentation_make_start;
    PDFView presentation_make_presentation;
    View presentation_make_presentation_left,presentation_make_presentation_right;

    private Uri uri = null;
    private static final int CODE_FILE = 1, CODE_KEYWORD=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make);

        presentation_make_file_setting = findViewById(R.id.presentation_make_file_setting);

        presentation_make_presentation = findViewById(R.id.presentation_make_presentation);
        presentation_make_presentation_left = findViewById(R.id.presentation_make_presentation_left);
        presentation_make_presentation_right = findViewById(R.id.presentation_make_presentation_right);


        presentation_make_start =findViewById(R.id.presentation_make_start);

        presentation_make_back = findViewById(R.id.presentation_make_back);
        presentation_make_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        presentation_make_file =findViewById(R.id.presentation_make_file);
        presentation_make_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, CODE_FILE);
            }
        });

        presentation_make_keyword = findViewById(R.id.presentation_make_keyword);
        presentation_make_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null) {
                    Intent intent = new Intent(view.getContext(), PresentationMakeKeywordActivity.class);
                    intent.putExtra("uri", uri);
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("발표자료를 등록해야 키워드를 설정할 수 있습니다.")
                            .setTitle("발표자료를 등록해주세요")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 이전 내용이 모두 사라집니다. 뒤로 가시겠습니까?")
                .setTitle("발표연습 만들기를 중지하시겠습니까?")
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_FILE && resultCode == RESULT_OK){
            uri = data.getData();
            String fileUri = uri.getPath().substring(uri.getPath().indexOf(":")+1);
            File file = new File(fileUri);
            presentation_make_file_setting.setText("설정완료 "+getName(uri));
            presentation_make_file_setting.setTextColor(getColor(R.color.accent));
            presentation_make_presentation.fromUri(uri)
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .pageFling(true)
                    .enableDoubletap(false)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .autoSpacing(true)
                    .enableSwipe(false)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            Log.d(TAG, "onPageChanged: "+Integer.toString(page)+" "+Integer.toString(pageCount));
                        }
                    })
                    .load();

            presentation_make_presentation_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentation_make_presentation.jumpTo(presentation_make_presentation.getCurrentPage()-1);
                    presentation_make_presentation.performPageSnap();
                }
            });
            presentation_make_presentation_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentation_make_presentation.jumpTo(presentation_make_presentation.getCurrentPage()+1);
                    presentation_make_presentation.performPageSnap();
                }
            });


        }

        super.onActivityResult(requestCode,resultCode,data);


    }


    private String getName(Uri uri) {
        String[] projection = { MediaStore.Images.ImageColumns.DISPLAY_NAME };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }



}