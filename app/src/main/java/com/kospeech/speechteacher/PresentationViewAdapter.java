package com.kospeech.speechteacher;



import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PresentationViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PresentationItem> mData= null;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;
    public PresentationViewAdapter(ArrayList<PresentationItem> data) {
        mData=data;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == TYPE_ITEM){
            view = inflater.inflate(R.layout.presentation_item, parent, false);
            holder= new ItemViewHolder(view);
        }
        else {
            view = inflater.inflate(R.layout.presentation_footer, parent, false);
            holder = new FooterViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            PresentationItem item = mData.get(position);
            itemViewHolder.titleView.setText(item.getPresntation_title());
            itemViewHolder.numberView.setText(Integer.toString(item.getPresentation_result_count())+"회");
            itemViewHolder.dateView.setText("발표일: "+item.getPresentation_date());
        }
        else{
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
        }

    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size())
            return TYPE_FOOTER;
        else
            return TYPE_ITEM;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{
        TextView presentationmakebutton;
        public FooterViewHolder(@NonNull View view) {
            super(view);

            presentationmakebutton = view.findViewById(R.id.presentation_make_button);
            presentationmakebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(view.getContext());
                    ad.setIcon(R.drawable.mini_logo);
                    ad.setMessage("원소의 개수는 "+Integer.toString(mData.size())+"개 입니다.");
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    ad.show();
                }
            });


        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView numberView;
        TextView dateView;
        public ItemViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                    builder.setMessage(mData.get(getAdapterPosition()).toString())
//                            .setTitle("해당 객체 정보")
//                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
                    Intent intent = new Intent(view.getContext(),PresentationResultListActivity.class);
                    intent.putExtra("presentationItem",mData.get(getAdapterPosition()));
                    view.getContext().startActivity(intent);

                }
            });

            titleView = view.findViewById(R.id.titleText);
            numberView = view.findViewById(R.id.numberText);
            dateView = view.findViewById(R.id.dateText);
            
            numberView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), numberView.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


}


