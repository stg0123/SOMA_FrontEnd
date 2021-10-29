package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresentationMakeActivity extends AppCompatActivity {
    ImageButton presentation_make_back;
    ConstraintLayout presentation_make_file,presentation_make_keyword,presentation_make_script,presentation_make_time,presentation_make_date,presentation_make_exword;
    TextView presentation_make_file_setting,presentation_make_keyword_setting,presentation_make_script_setting,presentation_make_time_setting,presentation_make_date_setting;
    Button presentation_make_start;
    PDFView presentation_make_presentation;
    View presentation_make_presentation_left,presentation_make_presentation_right;
    EditText presentation_make_title_text;
    private PresentationMakeItem presentationMakeItem;
    private String presentation_id =null;
    private Uri uri = null;
    private String filename= null;
    private int mYear,mMonth,mDay;
    private static final int CODE_FILE = 1, CODE_KEYWORD=2, CODE_SCRIPT=3, CODE_TIME=4;
    private boolean flag1=false,flag2=false,flag3=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation_make);
        presentationMakeItem = new PresentationMakeItem();


        presentation_make_file_setting = findViewById(R.id.presentation_make_file_setting);
        presentation_make_presentation = findViewById(R.id.presentation_make_presentation);
        presentation_make_presentation_left = findViewById(R.id.presentation_make_presentation_left);
        presentation_make_presentation_right = findViewById(R.id.presentation_make_presentation_right);

        presentation_make_keyword_setting = findViewById(R.id.presentation_make_keyword_setting);
        presentation_make_script_setting = findViewById(R.id.presentation_make_script_setting);
        presentation_make_time_setting = findViewById(R.id.presentation_make_time_setting);
        presentation_make_date_setting = findViewById(R.id.presentation_make_date_setting);

        presentation_make_title_text = findViewById(R.id.presentation_make_title_text);
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
                if(uri != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("발표자료 변경시 다른 설정이 초기화됩니다.")
                            .setTitle("발표자료 변경시 주의")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("application/pdf");
                                    startActivityForResult(intent, CODE_FILE);
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
                else {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("application/pdf");
                    startActivityForResult(intent, CODE_FILE);
                }
            }
        });

        presentation_make_keyword = findViewById(R.id.presentation_make_keyword);
        presentation_make_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null) {
                    Intent intent = new Intent(view.getContext(), PresentationMakeKeywordActivity.class);
                    intent.putExtra("uri", uri);
                    intent.putExtra("presentationmakeitem",presentationMakeItem);
                    startActivityForResult(intent, CODE_KEYWORD);
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

        presentation_make_script = findViewById(R.id.presentation_make_script);
        presentation_make_script.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uri != null) {
                    Intent intent = new Intent(view.getContext(), PresentationMakeScriptActivity.class);
                    intent.putExtra("uri", uri);
                    intent.putExtra("presentationmakeitem",presentationMakeItem);
                    startActivityForResult(intent, CODE_SCRIPT);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("발표자료를 등록해야 대본을 설정할 수 있습니다.")
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

        presentation_make_time =findViewById(R.id.presentation_make_time);
        presentation_make_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PresentationMakeTimeActivity.class);
                intent.putExtra("time",presentationMakeItem.time);
                startActivityForResult(intent, CODE_TIME);
            }
        });

        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MARCH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        presentation_make_date = findViewById(R.id.presentation_make_date);
        presentation_make_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),R.style.MySpinnerDatePickerStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = year+"-"+(month+1)+"-"+day;
                        presentation_make_date_setting.setText(date);
                        presentation_make_date_setting.setTextColor(getColor(R.color.accent));
                        presentationMakeItem.date =date;
                        mYear =year;
                        mMonth = month;
                        mDay =day;
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        presentation_make_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(presentation_make_title_text.getText().toString().equals("") || presentationMakeItem.time==null || presentationMakeItem.date == null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("발표제목, 발표시간, 발표일은 필수 설정입니다")
                            .setTitle("설정을 완료해주세요!")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    retrofitService.makepresentation(presentation_make_title_text.getText().toString(),presentationMakeItem.time,presentationMakeItem.date).enqueue(new Callback<MakePresentation>() {
                        @Override
                        public void onResponse(Call<MakePresentation> call, Response<MakePresentation> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                presentation_id = response.body().presentation_id;
                                if(uri != null) {
                                    MultipartBody.Part filePart = uriToMultipart(uri,"file",presentation_id,getContentResolver());
                                    retrofitService.makepresentationfile(response.body().presentation_id, filePart).enqueue(new Callback<MakePresentationFile>() {
                                        @Override
                                        public void onResponse(Call<MakePresentationFile> call, Response<MakePresentationFile> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                Toast.makeText(view.getContext(), "파일 등록 성공", Toast.LENGTH_SHORT).show();
                                            } else {
                                                try {
                                                    Gson gson = new Gson();
                                                    ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                                                    Toast.makeText(view.getContext(), data.message, Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<MakePresentationFile> call, Throwable t) {
                                            Toast.makeText(view.getContext(), "파일전송 실패", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
//                                if(presentationMakeItem.keywords != null) {
//                                    Map<String,String> map = new HashMap<>();
//                                    for(int i=0;i<presentationMakeItem.keywords.size();i++){
//                                        String keyword = "";
//                                        for(String str : presentationMakeItem.keywords.get(i)){
//                                            if(!keyword.equals(""))
//                                                keyword+=",";
//                                            keyword+=str;
//                                        }
//                                        if(!keyword.equals(""))
//                                            map.put(Integer.toString(i),keyword);
//                                    }
//                                    retrofitService.makepresentationkeyword(presentation_id,map).enqueue(new Callback<MakePresentationECT>() {
//                                        @Override
//                                        public void onResponse(Call<MakePresentationECT> call, Response<MakePresentationECT> response) {
//                                            if (response.isSuccessful() && response.body() != null) {
//                                                Toast.makeText(view.getContext(), "키워드 등록 성공", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                try {
//                                                    Gson gson = new Gson();
//                                                    ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
//                                                    Toast.makeText(view.getContext(), data.message, Toast.LENGTH_SHORT).show();
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                        @Override
//                                        public void onFailure(Call<MakePresentationECT> call, Throwable t) {
//                                            Toast.makeText(view.getContext(), "connection is failed", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                                if(presentationMakeItem.script !=null){
//                                    Map<String,String> map = new HashMap<>();
//                                    for(int i=0;i<presentationMakeItem.script.size();i++){
//                                        if(!presentationMakeItem.script.get(i).equals(""))
//                                            map.put(Integer.toString(i),presentationMakeItem.script.get(i));
//                                    }
//                                    retrofitService.makepresentationscript(presentation_id,map).enqueue(new Callback<MakePresentationECT>() {
//                                        @Override
//                                        public void onResponse(Call<MakePresentationECT> call, Response<MakePresentationECT> response) {
//                                            if (response.isSuccessful() && response.body() != null) {
//                                                Toast.makeText(view.getContext(), "대본 등록 성공", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                try {
//                                                    Gson gson = new Gson();
//                                                    ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
//                                                    Toast.makeText(view.getContext(), data.message, Toast.LENGTH_SHORT).show();
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                        @Override
//                                        public void onFailure(Call<MakePresentationECT> call, Throwable t) {
//                                            Toast.makeText(view.getContext(), "connection is failed", Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
                            }
                            else{
                                try {
                                    Gson gson = new Gson();
                                    ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                                    Toast.makeText(view.getContext(), data.message, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MakePresentation> call, Throwable t) {
                            Toast.makeText(view.getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
                        }
                    });
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
            filename = getName(uri);
            presentation_make_file_setting.setText("설정완료 "+filename);
            presentation_make_file_setting.setTextColor(getColor(R.color.accent));
            presentation_make_presentation.fromUri(uri)
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .pageFling(true)
                    .enableDoubletap(false)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .autoSpacing(true)
                    .enableSwipe(false)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            presentationMakeItem.keywords = new ArrayList<>();
                            presentationMakeItem.script = new ArrayList<>();
                            for(int i=0;i<nbPages;i++) {
                                presentationMakeItem.keywords.add(new ArrayList<>());
                                presentationMakeItem.script.add("");
                            }
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
        else if(requestCode == CODE_KEYWORD && resultCode == RESULT_OK){
            presentationMakeItem = (PresentationMakeItem) data.getSerializableExtra("presentationmakeitem");
            presentation_make_keyword_setting.setText("설정완료");
            presentation_make_keyword_setting.setTextColor(getColor(R.color.accent));

        }
        else if(requestCode == CODE_SCRIPT && resultCode == RESULT_OK){
            presentationMakeItem = (PresentationMakeItem) data.getSerializableExtra("presentationmakeitem");
            presentation_make_script_setting.setText("설정완료");
            presentation_make_script_setting.setTextColor(getColor(R.color.accent));
        }
        else if(requestCode == CODE_TIME && resultCode == RESULT_OK){
            presentationMakeItem.time = data.getStringExtra("time");
            presentation_make_time_setting.setText("설정완료");
            presentation_make_time_setting.setTextColor(getColor(R.color.accent));
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

    public class MakePresentation{
        @SerializedName("message")
        private String message;
        @SerializedName("presentation_id")
        private String presentation_id;

        @Override
        public String toString() {
            return "MakePresentation{" +
                    "message='" + message + '\'' +
                    ", presentation_id='" + presentation_id + '\'' +
                    '}';
        }
    }

    public class MakePresentationFile{
        @SerializedName("message")
        private String message;
        @SerializedName("file_id")
        private String file_id;

        @Override
        public String toString() {
            return "MakePresentationFile{" +
                    "message='" + message + '\'' +
                    ", file_id='" + file_id + '\'' +
                    '}';
        }
    }
    public class MakePresentationECT{
        @SerializedName("message")
        private String message;
        @Override
        public String toString() {
            return "MakePresentationECT{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    public static MultipartBody.Part uriToMultipart(final Uri uri, String name,String presentation_id, final ContentResolver contentResolver) {
        final Cursor c = contentResolver.query(uri, null, null, null, null);
        if (c != null) {
            if(c.moveToNext()) {
                @SuppressLint("Range") final String displayName = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                RequestBody requestBody = new RequestBody() {
                    @Override
                    public MediaType contentType() {
                        return MediaType.parse(contentResolver.getType(uri));
                    }

                    @Override
                    public void writeTo(BufferedSink sink) {
                        try {
                            sink.writeAll(Okio.source(contentResolver.openInputStream(uri)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                c.close();
                return MultipartBody.Part.createFormData(name, presentation_id+"_"+displayName, requestBody);
            } else {
                c.close();
                return null;
            }
        } else {
            return null;
        }
    }





}