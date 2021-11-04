package com.kospeech.speechteacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentKnowhow extends Fragment {

    private SharedPreferences sharedPreferences;
    private RetrofitService retrofitService;
    LinearLayout knowhow_list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_knowhow, container, false);

        knowhow_list = view.findViewById(R.id.knowhow_list);

        sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);
        retrofitService.getknowhow().enqueue(new Callback<List<KnowhowItem>>() {
            @Override
            public void onResponse(Call<List<KnowhowItem>> call, Response<List<KnowhowItem>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    List<KnowhowItem> knowhowItems = response.body();
                    for(KnowhowItem knowhow : knowhowItems){
                        View item = inflater.inflate(R.layout.knowhow_item, null);
                        TextView knowhow_title = item.findViewById(R.id.knowhow_title);
                        knowhow_title.setText(knowhow.getKnowhow_title());
                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(view.getContext(),KnowhowActivity.class);
                                intent.putExtra("knowhowitem",knowhow);
                                startActivity(intent);
                            }
                        });

                        knowhow_list.addView(item);
                    }
                }
                else {
                    try {
                        Gson gson = new Gson();
                        ErrorData data = gson.fromJson(response.errorBody().string(), ErrorData.class);
                        Toast.makeText(getContext(), data.message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<KnowhowItem>> call, Throwable t) {
                Toast.makeText(getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }



}