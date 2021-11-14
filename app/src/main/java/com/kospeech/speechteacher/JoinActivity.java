package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

public class JoinActivity extends AppCompatActivity {
    TextView join_to_login;
    EditText join_id,join_pw,join_pw2,join_nickname;
    Button join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_to_login = findViewById(R.id.join_to_login);
        join_id = findViewById(R.id.join_id);
        join_pw = findViewById(R.id.join_pw);
        join_pw2 = findViewById(R.id.join_pw2);
        join_nickname = findViewById(R.id.join_nickname);
        join_button = findViewById(R.id.join_button);

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RetrofitService retrofitService = RetrofitClient.getClient("").create(RetrofitService.class);
                if(join_id.getText().toString().replace(" ","").equals("")){
                    Toast.makeText(view.getContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!join_pw.getText().toString().equals(join_pw2.getText().toString()) || join_pw.getText().toString().replace(" ","").equals("") ) {
                    Toast.makeText(view.getContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(join_nickname.getText().toString().replace(" ","").equals("")){
                    Toast.makeText(view.getContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    retrofitService.join(join_id.getText().toString()
                            ,join_pw.getText().toString()
                            , join_nickname.getText().toString())
                    .enqueue(new Callback<JoinData>() {
                        @Override
                        public void onResponse(Call<JoinData> call, Response<JoinData> response) {
                            if(response.isSuccessful() && response.body()!=null){
                                Toast.makeText(view.getContext(), "회원가입 성공", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                try {
                                    Gson gson = new Gson();
                                    ErrorData data =  gson.fromJson(response.errorBody().string(),ErrorData.class);
                                    Toast.makeText(view.getContext(), "회원가입 실패"+data.message, Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JoinData> call, Throwable t) {
                            Toast.makeText(view.getContext(), "인터넷 연결을 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });



        join_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("뒤로 가기 시 이전 내용이 모두 사라집니다. 뒤로 가시겠습니까?")
                .setTitle("회원가입을 중지하시겠습니까?")
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

    public class JoinData{
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