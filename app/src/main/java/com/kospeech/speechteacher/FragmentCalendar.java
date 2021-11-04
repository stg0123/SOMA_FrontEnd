package com.kospeech.speechteacher;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCalendar extends Fragment {

    private SharedPreferences sharedPreferences;
    private RetrofitService retrofitService;
    MaterialCalendarView calendar_calendar;
    List<PresentationItem> presentationItems;
    LinearLayout calendar_presentationlist;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_calendar, container, false);
        calendar_presentationlist = view.findViewById(R.id.calendar_presentationlist);

        sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        retrofitService = RetrofitClient.getClient(sharedPreferences.getString("login_token","")).create(RetrofitService.class);

        calendar_calendar = view.findViewById(R.id.calendar_calendar);
        calendar_calendar.setSelectedDate(CalendarDay.today());
        calendar_calendar.addDecorators(new SaturdayDecorator(),
                new SundayDecorator(),
                new OnedayDecorator());

        retrofitService.getallpresentation().enqueue(new Callback<List<PresentationItem>>() {
            @Override
            public void onResponse(Call<List<PresentationItem>> call, Response<List<PresentationItem>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    presentationItems = response.body();
                    calendar_calendar.addDecorator(new EventDecorator(presentationItems));
                    calendar_calendar.setOnDateChangedListener(new OnDateSelectedListener() {
                        @Override
                        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                            calendar_presentationlist.removeAllViews();
                            for(PresentationItem p : presentationItems){
                                String mdate = p.getPresentation_date();
                                int year = Integer.parseInt(mdate.substring(0,4));
                                int month = Integer.parseInt(mdate.substring(5,7));
                                int day = Integer.parseInt(mdate.substring(8,10));
                                if(date.equals(CalendarDay.from(year,month,day))){
                                    View item = inflater.inflate(R.layout.presentation_item, null);

                                    TextView titleText = item.findViewById(R.id.titleText);
                                    TextView numberText = item.findViewById(R.id.numberText);
                                    TextView dateText = item.findViewById(R.id.dateText);
                                    LinearLayout presentation_item_delete = item.findViewById(R.id.presentation_item_delete);

                                    titleText.setText(p.getPresntation_title());
                                    numberText.setText(p.getPresentation_result_info()+"회");
                                    dateText.setText("발표일: " + mdate);
                                    presentation_item_delete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getContext(), "발표연습 탭에서 삭제해주세요", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    calendar_presentationlist.addView(item);

                                }


                            }

                        }
                    });
                }
                else{
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
            public void onFailure(Call<List<PresentationItem>> call, Throwable t) {
                Toast.makeText(getContext(),"connection is failed",Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }


    class SaturdayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();
        public SaturdayDecorator(){}


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(getContext().getColor(R.color.accent)));
        }
    }
    class SundayDecorator implements DayViewDecorator{
        private final Calendar calendar = Calendar.getInstance();
        public SundayDecorator(){}


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(getContext().getColor(R.color.error)));
        }
    }
    class OnedayDecorator implements DayViewDecorator{
        private CalendarDay date;
        public OnedayDecorator(){
            date = CalendarDay.today();
        }


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.2f));
        }
    }
    class EventDecorator implements DayViewDecorator{
        private HashSet<CalendarDay> dates;
        public  EventDecorator(List<PresentationItem> items){
            dates = new HashSet<>();
            for(PresentationItem p : items) {
                String date = p.getPresentation_date();
                int year = Integer.parseInt(date.substring(0,4));
                int month = Integer.parseInt(date.substring(5,7));
                int day = Integer.parseInt(date.substring(8,10));
                dates.add(CalendarDay.from(year,month,day));
            }
        }


        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(7,getContext().getColor(R.color.error)));
        }
    }
}