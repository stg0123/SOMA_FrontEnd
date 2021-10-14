package com.kospeech.speechteacher;


import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentMypage extends Fragment {
    TextView mypage_logout,mypage_delete;
    TextView mypage_email,mypage_nickname;
    TextView mypage_toupdate;
    MyInfo myInfo;
    int update_requestcode =1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_mypage, container, false);
        mypage_logout = view.findViewById(R.id.mypage_logout);
        mypage_delete = view.findViewById(R.id.mypage_delete);
        mypage_email = view.findViewById(R.id.mypage_email);
        mypage_nickname = view.findViewById(R.id.mypage_nickname);
        mypage_toupdate = view.findViewById(R.id.mypage_toupdate);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        retrofitService.myinfo().enqueue(new Callback<MyInfo>() {
            @Override
            public void onResponse(Call<MyInfo> call, Response<MyInfo> response) {
                if(response.isSuccessful() && response.body()!=null){
                    myInfo = response.body();
                    mypage_email.setText(myInfo.user_email);
                    mypage_nickname.setText(myInfo.user_nickname);

                }
                else{
                    try {
                        Gson gson = new Gson();
                        ErrorData data = gson.fromJson(response.errorBody().string(),ErrorData.class);
                        Toast.makeText(getContext(),"Error"+ data.message, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                        getActivity().finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyInfo> call, Throwable t) {
                Toast.makeText(getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
            }
        });


        mypage_toupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),MyinfoUpdateActivity.class);
                intent.putExtra("user_email",mypage_email.getText().toString());
                intent.putExtra("user_nickname",mypage_nickname.getText().toString());
                startActivityForResult(intent,update_requestcode);
            }
        });


        mypage_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getActivity(),LoginActivity.class));
                getActivity().finish();

            }
        });

        mypage_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("회원 탈퇴 시 모든 정보가 삭제되며, 복구가 불가능합니다. \n정말 탈퇴하시겠습니까? ")
                        .setTitle("정말 탈퇴하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                retrofitService.deleteaccount().enqueue(new Callback<DeleteAccount>() {
                                    @Override
                                    public void onResponse(Call<DeleteAccount> call, Response<DeleteAccount> response) {
                                        if(response.isSuccessful() && response.body()!=null){
                                            Toast.makeText(view.getContext(), response.body().message, Toast.LENGTH_SHORT).show();
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.clear();
                                            editor.commit();
                                            startActivity(new Intent(getActivity(),LoginActivity.class));
                                            getActivity().finish();
                                        }
                                        else{
                                            try {
                                                Gson gson = new Gson();
                                                ErrorData data = gson.fromJson(response.errorBody().string(),ErrorData.class);
                                                Toast.makeText(getContext(),"Error"+ data.message, Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<DeleteAccount> call, Throwable t) {
                                        Toast.makeText(getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
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
        });
        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==update_requestcode && resultCode==RESULT_OK){
            mypage_nickname.setText(data.getStringExtra("user_nickname"));
        }
    }

    public class DeleteAccount{
        @SerializedName("message")
        private String message;

        @Override
        public String toString() {
            return "joinData{" +
                    "message='" + message + '\'' +
                    '}';
        }
    }

    public class MyInfo{
        @SerializedName("user_id")
        private int user_id;
        @SerializedName("user_email")
        private String user_email;
        @SerializedName("user_nickname")
        private String user_nickname;

        @Override
        public String toString() {
            return "MyInfo{" +
                    "user_id=" + user_id +
                    ", user_email='" + user_email + '\'' +
                    ", user_nickname='" + user_nickname + '\'' +
                    '}';
        }
    }





}