package com.kospeech.speechteacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PresentationAdapter extends BaseAdapter {
    private Context mContext;
    LayoutInflater mLayoutInflater = null;
    private ArrayList<PresentationItem> mdata = new ArrayList<PresentationItem>();

    public PresentationAdapter(Context context, ArrayList<PresentationItem> data){
        mContext = context;
        mdata= data;
        mLayoutInflater = LayoutInflater.from(mContext);

    }


    @Override
    public int getCount() {
        return mdata.size();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = mLayoutInflater.inflate(R.layout.presentation_item, null);
        TextView titleView = view.findViewById(R.id.titleText);
        TextView numberView = view.findViewById(R.id.numberText);
        TextView dateView = view.findViewById(R.id.dateText);

        titleView.setText(mdata.get(position).getTitle());
        numberView.setText(Integer.toString(mdata.get(position).getNumber())+"회");
        dateView.setText("발표일: "+mdata.get(position).getDate());

        return view;
    }
}



