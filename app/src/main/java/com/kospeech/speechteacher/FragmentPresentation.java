package com.kospeech.speechteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPresentation extends Fragment {
    RecyclerView mRecyclerView;
    PresentationViewAdapter mAdapter;
    ArrayList<PresentationItem> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_presentation, container, false);

        mRecyclerView = view.findViewById(R.id.presentation_view);
        mList = new ArrayList<>();
        mAdapter = new PresentationViewAdapter(mList,getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        retrofitService.getallpresentation().enqueue(new Callback<List<PresentationItem>>() {
            @Override
            public void onResponse(Call<List<PresentationItem>> call, Response<List<PresentationItem>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    mList.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<PresentationItem>> call, Throwable t) {
                Toast.makeText(getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


}