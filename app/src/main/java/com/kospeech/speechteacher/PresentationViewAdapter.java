package com.kospeech.speechteacher;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PresentationViewAdapter extends RecyclerView.Adapter<PresentationViewAdapter.ViewHolder> {
    private ArrayList<PresentationItem> mData= null;

    public PresentationViewAdapter(ArrayList<PresentationItem> data) {
        mData=data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.presentation_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PresentationItem item = mData.get(position);

        holder.titleView.setText(item.getTitle());
        String num = Integer.toString(item.getNumber())+"회";
        holder.numberView.setText(num);
        holder.dateView.setText("발표일: "+item.getDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView numberView;
        TextView dateView;
        public ViewHolder(View view) {
            super(view);

            titleView = view.findViewById(R.id.titleText);
            numberView = view.findViewById(R.id.numberText);
            dateView = view.findViewById(R.id.dateText);

        }

    }


}


