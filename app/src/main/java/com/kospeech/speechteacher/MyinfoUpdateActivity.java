package com.kospeech.speechteacher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyinfoUpdateActivity extends AppCompatActivity {
    TextView update_email,update_cancel;
    EditText update_pw,update_pw2,update_nickname;
    Button update_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_update);

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        Intent intent = getIntent();

        update_email = findViewById(R.id.update_email);
        update_nickname = findViewById(R.id.update_nickname);
        update_button = findViewById(R.id.update_button);
        update_pw = findViewById(R.id.update_pw);
        update_pw2 = findViewById(R.id.update_pw2);

        update_email.setText(intent.getExtras().getString("user_email"));
        update_nickname.setText(intent.getExtras().getString("user_nickname"));

        update_cancel = findViewById(R.id.update_cancel);
        update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!update_pw.getText().toString().equals(update_pw2.getText().toString()))
                    Toast.makeText(view.getContext(), "??????????????? ???????????????.", Toast.LENGTH_SHORT).show();
                else if(update_nickname.getText().toString().replace(" ","").equals(""))
                    Toast.makeText(view.getContext(), "???????????? ???????????????", Toast.LENGTH_SHORT).show();
                else {

                    retrofitService.myinfoupdate(update_email.getText().toString()
                            , update_pw.getText().toString()
                            , update_nickname.getText().toString())
                            .enqueue(new Callback<UpdateData>() {
                                @Override
                                public void onResponse(Call<UpdateData> call, Response<UpdateData> response) {
                                    if(response.isSuccessful() && response.body()!=null){
                                        Toast.makeText(view.getContext(), response.body().message, Toast.LENGTH_SHORT).show();
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("user_nickname",update_nickname.getText().toString());
                                        setResult(RESULT_OK,resultIntent);
                                        finish();
                                    }
                                    else{
                                        try {
                                            Gson gson = new Gson();
                                            ErrorData data = gson.fromJson(response.errorBody().string(),ErrorData.class);
                                            Toast.makeText(view.getContext(),"Error"+ data.message, Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                                @Override
                                public void onFailure(Call<UpdateData> call, Throwable t) {
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
        builder.setMessage("?????? ?????? ??? ?????? ????????? ?????? ???????????????. ?????? ???????????????????")
                .setTitle("???????????? ????????? ?????? ???????????????????")
                .setPositiveButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public class UpdateData{
        @SerializedName("message")
        private String message;

        @Override
        public String toString() {
            return "joinData{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }




}