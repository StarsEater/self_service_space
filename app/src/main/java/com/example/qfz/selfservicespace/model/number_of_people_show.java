package com.example.qfz.selfservicespace.model;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.qfz.selfservicespace.R;

public class number_of_people_show extends Dialog
       implements NumberPicker.OnValueChangeListener,
        NumberPicker.Formatter, NumberPicker.OnScrollListener {
    private NumberPicker numberPicker;
    private ImageView imageView_more;
    private ImageView imageView_less;
    private TextView sure_btn,cancel_btn;
    private sure_listener mysure_listener;
    private cancel_listener mycancel_listener;
    String devil="https://cdn.dribbble.com/users/1304163/screenshots/4839096/devilish_1x.jpg";
    String angel="https://cdn.dribbble.com/users/35950/screenshots/2524467/cupid.png";

    public number_of_people_show(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_people_show);
        sure_btn=findViewById(R.id.sure_people);
        cancel_btn=findViewById(R.id.cancel_people);

        numberPicker=findViewById(R.id.show_num_picker);
        numberPicker.setFormatter(this); //格式化数字，需重写format方法，详情见下面的代码
        numberPicker.setOnValueChangedListener(this); //值变化监听事件
        numberPicker.setOnScrollListener(this); //滑动监听事件
        numberPicker.setMinValue(0);//最小值
        numberPicker.setMaxValue(20);//最大值
        numberPicker.setValue(1);//设置初始选定值

        imageView_more=findViewById(R.id.more_people);
        Glide.with(getContext()).load(angel).into(imageView_more);
        imageView_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(imageView_more);
                int i = Integer.parseInt(String.valueOf(numberPicker.getValue()));
                numberPicker.setValue(i+1);
            }
        });


        imageView_less=findViewById(R.id.less_people);
        Glide.with(getContext()).load(devil).into(imageView_less);
        imageView_less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation(imageView_less);
                int i = Integer.parseInt(String.valueOf(numberPicker.getValue()));
                numberPicker.setValue(i-1);
            }
        });
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mysure_listener!=null)
                mysure_listener.sure(number_of_people_show.this);
                dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mycancel_listener!=null)
                    mycancel_listener.cancel(number_of_people_show.this);
                dismiss();
            }
        });
    }

    public String getnum(){
        int num=numberPicker.getValue();
        return String.valueOf(num);
    }

    public number_of_people_show setsure(sure_listener sure_listener){
        this.mysure_listener=sure_listener;
        return this;
    }
    public number_of_people_show setcancel(cancel_listener cancel_listener){
        this.mycancel_listener=cancel_listener;
        return this;
    }










    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {
        switch (i) {
            case NumberPicker.OnScrollListener.SCROLL_STATE_FLING:
                Toast.makeText(getContext(), "scroll state fling", Toast.LENGTH_LONG)
                        .show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_IDLE:
                Toast.makeText(getContext(), "scroll state idle", Toast.LENGTH_LONG).show();
                break;
            case NumberPicker.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Toast.makeText(getContext(), "scroll state touch scroll", Toast.LENGTH_LONG)
                        .show();
                break;
        }
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(getContext(), "值发生变化 ，原值： " + i + " ， 新值:"+ i1, Toast.LENGTH_SHORT).show();
    }


    public interface sure_listener{
        void sure(number_of_people_show dialog);
    }
    public interface cancel_listener{
        void cancel(number_of_people_show dialog);
    }

    private void startAnimation(ImageView imageView) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(imageView, "translationY", 0, 400,0);
        // 加速减速插值器
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        //加速插值器
        //AccelerateInterpolator interpolator = new AccelerateInterpolator(0.8f);
        // 回荡秋千插值器
        //AnticipateInterpolator interpolator = new AnticipateInterpolator(0.8f);
        oa.setInterpolator(interpolator);
        oa.start();
    }
}
