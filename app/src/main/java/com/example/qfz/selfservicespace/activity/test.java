package com.example.qfz.selfservicespace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.StoreInfo;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class test extends AppCompatActivity {

    @InjectView(R.id.button)
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.inject(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendRequest();
            }
        });
    }

    private void SendRequest(){
        /**
         * Handler
         * @note 用于子线程和主线程之间的数据传递
         * 解决了andr4.0以上主线程无法访问网络的问题。
         */
        final Handler myhandler = new Handler() {
            public void handleMessage(Message msg) {
                //isNetError = msg.getData().getBoolean("isNetError");
                if(msg.what==0x123)
                {
                    //更新界面
                    Log.d("Get reservation", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");

                    Gson gson = new Gson();
                    Reservation reservation = gson.fromJson(message, Reservation.class);

                    if(reservation != null) {
                        Intent intent = new Intent(test.this, ReservationWaitingPayedActivity.class);
                        intent.putExtra("Reservation", reservation);
                        startActivity(intent);
                    }
                }
            }
        };

        /**
         * 新建子线程
         * @note 用于建立网络连接，从服务器获取IMEI对应的用户信息
         * 解决了andr4.0以上主线程无法访问网络的问题。
         */
        new Thread(){
            public void run(){
                try {
                    //请求服务器返回用户数据
                    Connection connection = new Connection();
                    //Location location = getLocation();
                    //String msg = connection.connectServer(etUsername.getText().toString() + "\t" + etPassword.getText().toString()
                    //        + "\t" + location.getLatitude() + "\t" + location.getLongitude(), "/getInfo.json");
                    String msg = connection.connectServer("getReservation","/selectSeat.json");

                    //如果成功返回
                    //将子线程中获得的用户信息传递到主线程 myhandler 中
                    Bundle bundle = new Bundle();
                    Message message = new Message();

                    bundle.putString("msg", msg);

                    message.setData(bundle);
                    message.what=0x123;
                    myhandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
