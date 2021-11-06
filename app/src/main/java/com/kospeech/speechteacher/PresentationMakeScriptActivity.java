package com.kospeech.speechteacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class PresentationMakeScriptActivity extends AppCompatActivity {
    ImageButton presentation_make_script_back;
    PDFView presentation_make_script_presentation;
    View presentation_make_script_presentation_left,presentation_make_script_presentation_right;

    TextView presentation_make_script_finish,presentation_make_script_pagenum;
    EditText presentation_make_script_script;
    private PresentationMakeItem presentationMakeItem;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make_script);
        presentationMakeItem = (PresentationMakeItem) getIntent().getSerializableExtra("presentationmakeitem");
        uri = getIntent().getParcelableExtra("uri");

        presentation_make_script_script = findViewById(R.id.presentation_make_script_script);

        presentation_make_script_presentation =findViewById(R.id.presentation_make_script_presentation);
        presentation_make_script_presentation_left = findViewById(R.id.presentation_make_script_presentation_left);
        presentation_make_script_presentation_right = findViewById(R.id.presentation_make_script_presentation_right);
        presentation_make_script_pagenum = findViewById(R.id.presentation_make_script_pagenum);
        viewPresentation(uri);



        presentation_make_script_finish = findViewById(R.id.presentation_make_script_finish);
        presentation_make_script_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentationMakeItem.script.set(presentation_make_script_presentation.getCurrentPage(),presentation_make_script_script.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("presentationmakeitem",presentationMakeItem);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        presentation_make_script_back = findViewById(R.id.presentation_make_script_back);
        presentation_make_script_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected void refreshScript(int currentPage){
        presentation_make_script_script.setText("");
        if(presentationMakeItem.script.get(currentPage).equals(""))
            return ;
        presentation_make_script_script.setText(presentationMakeItem.script.get(currentPage));

    }


    protected void viewPresentation(Uri uri){
        String fileUri = uri.getPath().substring(uri.getPath().indexOf(":")+1);
        File file = new File(fileUri);
        presentation_make_script_presentation.fromUri(uri)
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
                        presentation_make_script_pagenum.setText((page+1)+"/"+pageCount);
                        refreshScript(page);
                    }
                })
                .load();
        presentation_make_script_presentation_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentationMakeItem.script.set(presentation_make_script_presentation.getCurrentPage(),presentation_make_script_script.getText().toString());
                presentation_make_script_presentation.jumpTo(presentation_make_script_presentation.getCurrentPage()-1);
                presentation_make_script_presentation.performPageSnap();
            }
        });
        presentation_make_script_presentation_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentationMakeItem.script.set(presentation_make_script_presentation.getCurrentPage(),presentation_make_script_script.getText().toString());
                presentation_make_script_presentation.jumpTo(presentation_make_script_presentation.getCurrentPage()+1);
                presentation_make_script_presentation.performPageSnap();
            }
        });
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 대본 수정내용이 반영되지 않습니다.")
                .setTitle("대본 입력을 중시하시겠습니까?")
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