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
        mAdapter = new PresentationViewAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        retrofitService.getpresentation().enqueue(new Callback<List<PresentationItem>>() {
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

    public class PresentationItem{
        @SerializedName("presentation_id")
        private int presentation_id;
        @SerializedName("user_id")
        private String user_id;
        @SerializedName("presentation_title")
        private String presntation_title;
        @SerializedName("presentation_time")
        private String presentation_time;
        @SerializedName("presentation_date")
        private String presentation_date;
        @SerializedName("presentation_ex_dupword")
        private String presentation_ex_dupword;
        @SerializedName("presentation_ex_improper")
        private String presentation_ex_improper;
        @SerializedName("presentation_result_count")
        private int presentation_result_count;

        public int getPresentation_id() {
            return presentation_id;
        }

        public void setPresentation_id(int presentation_id) {
            this.presentation_id = presentation_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPresntation_title() {
            return presntation_title;
        }

        public void setPresntation_title(String presntation_title) {
            this.presntation_title = presntation_title;
        }

        public String getPresentation_time() {
            return presentation_time;
        }

        public void setPresentation_time(String presentation_time) {
            this.presentation_time = presentation_time;
        }

        public String getPresentation_date() {
            return presentation_date;
        }

        public void setPresentation_date(String presentation_date) {
            this.presentation_date = presentation_date;
        }

        public String getPresentation_ex_dupword() {
            return presentation_ex_dupword;
        }

        public void setPresentation_ex_dupword(String presentation_ex_dupword) {
            this.presentation_ex_dupword = presentation_ex_dupword;
        }

        public String getPresentation_ex_improper() {
            return presentation_ex_improper;
        }

        public void setPresentation_ex_improper(String presentation_ex_improper) {
            this.presentation_ex_improper = presentation_ex_improper;
        }

        public int getPresentation_result_count() {
            return presentation_result_count;
        }

        public void setPresentation_result_count(int presentation_result_count) {
            this.presentation_result_count = presentation_result_count;
        }

        @Override
        public String toString() {
            return "PresentationItem{" +
                    "presentation_id=" + presentation_id +
                    ", user_id='" + user_id + '\'' +
                    ", presntation_title='" + presntation_title + '\'' +
                    ", presentation_time='" + presentation_time + '\'' +
                    ", presentation_date='" + presentation_date + '\'' +
                    ", presentation_ex_dupword='" + presentation_ex_dupword + '\'' +
                    ", presentation_ex_improper='" + presentation_ex_improper + '\'' +
                    ", presentation_result_count=" + presentation_result_count +
                    '}';
        }
    }

}