package com.kospeech.speechteacher;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText edit_id,edit_pw;
    Button login_button,join_button;
    CheckBox auto_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_id = findViewById(R.id.edit_id);
        edit_pw = findViewById(R.id.edit_pw);
        login_button = findViewById(R.id.login_button);
        auto_login = findViewById(R.id.auto_login);
        join_button = findViewById(R.id.join_button);
        RetrofitService retrofitService = RetrofitClient.getClient("").create(RetrofitService.class);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_id.getText().toString().equals(""))
                    Toast.makeText(view.getContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                else if(edit_pw.getText().toString().equals(""))
                    Toast.makeText(view.getContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                else {
                    retrofitService.login(edit_id.getText().toString(), edit_pw.getText().toString()).enqueue(new Callback<TokenData>() {
                        @Override
                        public void onResponse(@NonNull Call<TokenData> call, @NonNull Response<TokenData> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                TokenData data = response.body();
                                Log.d(TAG, "onResponse: " + data.token);
                                editor.putString("login_token", data.token);
                                if (auto_login.isChecked()) {
                                    editor.putString("auto_login", "ok");
                                }
                                editor.commit();
                                startActivity(new Intent(view.getContext(), MainActivity.class));
                                finish();

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
                        public void onFailure(Call<TokenData> call, Throwable t) {
                            Log.d(TAG, "onFailure: fail");
                        }
                    });
                }
            }
        });

        join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),JoinActivity.class));
            }
        });

    }
    public class TokenData{
        @SerializedName("message")
        private String message;
        @SerializedName("access_token")
        private String token;

        @Override
        public String toString() {
            return "TokenData{" +
                    "message='" + message + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }
    }


}