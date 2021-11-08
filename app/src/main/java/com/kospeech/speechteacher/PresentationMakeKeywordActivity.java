package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class PresentationMakeKeywordActivity extends AppCompatActivity {
    FlexboxLayout presentation_make_keyword_keyword;
    ImageButton presentation_make_keyword_back;
    PDFView presentation_make_keyword_presentation;
    View presentation_make_keyword_presentation_left,presentation_make_keyword_presentation_right;
    ConstraintLayout presentation_make_keyword_add;

    TextView presentation_make_keyword_finish,presentation_make_keyword_pagenum;
    private PresentationMakeItem presentationMakeItem;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make_keyword);
        presentationMakeItem = (PresentationMakeItem) getIntent().getSerializableExtra("presentationmakeitem");
        presentation_make_keyword_keyword = findViewById(R.id.presentation_make_keyword_keyword);


        presentation_make_keyword_presentation = findViewById(R.id.presentation_make_keyword_presentation);
        presentation_make_keyword_presentation_left = findViewById(R.id.presentation_make_keyword_presentation_left);
        presentation_make_keyword_presentation_right = findViewById(R.id.presentation_make_keyword_presentation_right);
        presentation_make_keyword_pagenum = findViewById(R.id.presentation_make_keyword_pagenum);
        uri = getIntent().getParcelableExtra("uri");
        viewPresentation(uri);

        presentation_make_keyword_back = findViewById(R.id.presentation_make_keyword_back);
        presentation_make_keyword_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        presentation_make_keyword_add = findViewById(R.id.presentation_make_keyword_add);
        presentation_make_keyword_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = new EditText(view.getContext());
                editText.setBackground(null);
                editText.setHint("키워드를 입력해주세요");
                editText.setHintTextColor(getColor(R.color.textprimaryinverse));
                editText.setTextColor(getColor(R.color.textprimary));
                editText.setSingleLine(true);
                editText.setTextSize(20);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(editText)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                presentationMakeItem.keywords.get(presentation_make_keyword_presentation.getCurrentPage()).add(editText.getText().toString());
                                refreshKeyword(presentation_make_keyword_presentation.getCurrentPage());
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();



            }
        });


        presentation_make_keyword_finish = findViewById(R.id.presentation_make_keyword_finish);
        presentation_make_keyword_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("presentationmakeitem",presentationMakeItem);
                setResult(RESULT_OK,intent);
                finish();
            }
        });




    }

    protected void refreshKeyword(int currentPage){
        presentation_make_keyword_keyword.removeAllViews();
        if(presentationMakeItem.keywords.get(currentPage).isEmpty())
            return ;
        for(int i = 0; i< presentationMakeItem.keywords.get(currentPage).size(); i++) {
            TextView tmp = new TextView(this);
            tmp.setText(presentationMakeItem.keywords.get(currentPage).get(i));
            tmp.setTextColor(getColor(R.color.primary));
            tmp.setBackground(getDrawable(R.drawable.round_select_border2));
            tmp.setTextSize(16);
            tmp.setPadding(10, 15, 10, 15);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(300, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            tmp.setLayoutParams(params);
            tmp.setGravity(Gravity.CENTER);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("키워드 삭제")
                            .setMessage("삭제하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    presentationMakeItem.keywords.get(currentPage).remove(tmp.getText().toString());
                                    refreshKeyword(currentPage);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            presentation_make_keyword_keyword.addView(tmp);
        }


    }

    protected void viewPresentation(Uri uri){
        String fileUri = uri.getPath().substring(uri.getPath().indexOf(":")+1);
        File file = new File(fileUri);
        presentation_make_keyword_presentation.fromUri(uri)
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
                        presentation_make_keyword_pagenum.setText((page+1)+"/"+pageCount);

                        refreshKeyword(page);
                    }
                })
                .load();
        presentation_make_keyword_presentation_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentation_make_keyword_presentation.jumpTo(presentation_make_keyword_presentation.getCurrentPage()-1);
                presentation_make_keyword_presentation.performPageSnap();
            }
        });
        presentation_make_keyword_presentation_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentation_make_keyword_presentation.jumpTo(presentation_make_keyword_presentation.getCurrentPage()+1);
                presentation_make_keyword_presentation.performPageSnap();
            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 키워드 수정내용이 반영되지 않습니다.")
                .setTitle("키워드 입력을 중시하시겠습니까?")
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
}