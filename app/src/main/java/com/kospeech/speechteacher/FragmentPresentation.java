package com.kospeech.speechteacher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentPresentation extends Fragment {

    RecyclerView mRecyclerView = null;
    PresentationViewAdapter mAdapter = null;
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

        addItem("기획 발표 준비",3, "1월 12일");
        addItem("중간 발표 준비1",3,"10월 12일");
        addItem("중간 발표 준비2",4,"10월 13일");
        addItem("중간 발표 준비3",5,"10월 14일");
        addItem("중간 발표 준비4",6,"10월 15일");
        addItem("중간 발표 준비5",7,"10월 16일");
        addItem("중간 발표 준비6",8,"10월 17일");


        mAdapter.notifyDataSetChanged();


        return view;
    }
    private void addItem(String title,int number,String date){
        PresentationItem item = new PresentationItem();

        item.setTitle(title);
        item.setNumber(number);
        item.setDate(date);

        mList.add(item);
    }



}