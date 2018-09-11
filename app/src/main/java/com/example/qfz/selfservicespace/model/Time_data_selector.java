package com.example.qfz.selfservicespace.model;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Time_data_selector extends Dialog {
    private TextView sure_btn,cancel_btn;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private sure_listener mysure_listener;
    private cancel_listener mycancel_listener;
    private  Calendar calendar;
    final SimpleDateFormat format = new SimpleDateFormat(
            "yyyy年MM月dd日  HH:mm");
    public Time_data_selector(@NonNull Context context) {
        super(context);
    }

    public Time_data_selector(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Time_data_selector(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_data_selector);
        sure_btn=findViewById(R.id.sure_time);
        cancel_btn=findViewById(R.id.cancel_time);
        timePicker=findViewById(R.id.tpPicker);
        datePicker=findViewById(R.id.dpPicker);
        //对时间的选择
        datePicker.init(2000, 8, 20, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);

                Toast.makeText(getContext(),
                        format.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
        });

//        timePicker.setIs24HourView(true);
//        timePicker
//                .setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//                    @Override
//                    public void onTimeChanged(TimePicker view, int hourOfDay,
//                                              int minute) {
//                       Toast.makeText(getContext(),
//                                hourOfDay + "小时" + minute + "分钟",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                });
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mysure_listener!=null)
                    mysure_listener.sure(Time_data_selector.this);
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mycancel_listener!=null)
                    mycancel_listener.cancel(Time_data_selector.this);
                dismiss();
            }
        });


    }
    public Time_data_selector setsure(sure_listener sure_listener){
        this.mysure_listener=sure_listener;
        return this;
    }
    public Time_data_selector setcancel(cancel_listener cancel_listener){
        this.mycancel_listener= cancel_listener;
        return this;
    }
    public String getDate(){
        int month=datePicker.getMonth();
        int year=datePicker.getYear();
        int day=datePicker.getDayOfMonth();
        String s=year+"-"+month+"-"+day;
        return  s;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public String getTime(){
        int hour=timePicker.getHour();
        int min=timePicker.getMinute();
        String s=hour+":"+min;
        return s;

    }
    public interface  sure_listener{
        void sure(Time_data_selector dialog);
    }
    public interface cancel_listener{
        void cancel(Time_data_selector dialog);
    }

}