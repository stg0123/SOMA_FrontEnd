package com.kospeech.speechteacher;



import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PresentationViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PresentationItem> mData= null;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;
    private Activity activity;
    private FragmentPresentation fragment;
    public PresentationViewAdapter(ArrayList<PresentationItem> data,FragmentPresentation curfragment) {
        mData=data;
        fragment = curfragment;
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


            itemViewHolder.numberView.setText(item.getPresentation_result_info()+"회");
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
                    view.getContext().startActivity(new Intent(view.getContext(),PresentationMakeActivity.class));
                }
            });


        }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView titleView,numberView,dateView;
        LinearLayout presentation_item_delete;
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
            presentation_item_delete = view.findViewById(R.id.presentation_item_delete);
            presentation_item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());
                    builder.setMessage("발표연습을 삭제하면 복구가 불가능합니다. 정말 삭제하시겠습니까?")
                            .setTitle("발표연습을 삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                    RetrofitService retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);
                                    retrofitService.deletepresentation(mData.get(getAdapterPosition()).getPresentation_id()).enqueue(new Callback<DeletePresentation>() {
                                        @Override
                                        public void onResponse(Call<DeletePresentation> call, Response<DeletePresentation> response) {
                                            if(response.isSuccessful() && response.body()!=null){
                                                Toast.makeText(view.getContext(),response.body().message, Toast.LENGTH_SHORT).show();
                                                fragment.onRefreshFragment();
//                                                Intent intent = activity.getIntent();
//                                                activity.finish();
//                                                activity.overridePendingTransition(0,0);
//                                                activity.startActivity(intent);
//                                                activity.overridePendingTransition(0,0);
                                            }
                                            else{
                                                try {
                                                    Gson gson = new Gson();
                                                    ErrorData data = gson.fromJson(response.errorBody().string(),ErrorData.class);
                                                    Toast.makeText(view.getContext(),"Error"+ data.message, Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<DeletePresentation> call, Throwable t) {
                                            Toast.makeText(view.getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
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
        }

    }

    public class DeletePresentation{
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


