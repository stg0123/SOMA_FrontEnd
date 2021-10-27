package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.flexbox.FlexboxLayout;

import java.io.File;

public class PresentationMakeKeywordActivity extends AppCompatActivity {
    FlexboxLayout presentation_make_keyword_keyword;
    ImageButton presentation_make_keyword_back;
    PDFView presentation_make_keyword_presentation;
    View presentation_make_keyword_presentation_left,presentation_make_keyword_presentation_right;
    private Uri uri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make_keyword);
        presentation_make_keyword_keyword = findViewById(R.id.presentation_make_keyword_keyword);


        presentation_make_keyword_presentation = findViewById(R.id.presentation_make_keyword_presentation);
        presentation_make_keyword_presentation_left = findViewById(R.id.presentation_make_keyword_presentation_left);
        presentation_make_keyword_presentation_right = findViewById(R.id.presentation_make_keyword_presentation_right);
        uri = getIntent().getParcelableExtra("uri");
        viewPresentation(uri);


        presentation_make_keyword_back = findViewById(R.id.presentation_make_keyword_back);
        presentation_make_keyword_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });






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
                        Log.d(TAG, "onPageChanged: "+Integer.toString(page)+" "+Integer.toString(pageCount));
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


}