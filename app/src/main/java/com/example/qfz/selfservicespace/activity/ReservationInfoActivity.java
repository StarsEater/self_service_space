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
import android.widget.EditText;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationInfo;
import com.example.qfz.selfservicespace.object.ReservationList;
import com.example.qfz.selfservicespace.object.StoreList;
import com.example.qfz.selfservicespace.object.User;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReservationInfoActivity extends AppCompatActivity {

    @InjectView(R.id.form_id)
    EditText reservationID;
    @InjectView(R.id.form_shopname)
    EditText shopName;
    @InjectView(R.id.form_times)
    EditText time;
    @InjectView(R.id.form_content)
    EditText tables;
    @InjectView(R.id.form_cheap)
    EditText discount;
    @InjectView(R.id.form_gua)
    EditText bail;
    @InjectView(R.id.form_total)
    EditText total;
    @InjectView(R.id.form_phone)
    EditText phoneNumber;

    @InjectView(R.id.form_sure)
    Button confirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        final Intent intent = getIntent();
        final ReservationInfo reservation = (ReservationInfo) intent.getSerializableExtra("Reservation");

        reservationID.setText(reservation.reservationID);
        shopName.setText(reservation.storeName);
        time.setText(reservation.time);
        tables.setText(reservation.tables.toString());
        discount.setText(reservation.discount+"");
        bail.setText(reservation.bail + "");
        total.setText(reservation.total + "");
        phoneNumber.setText(reservation.phoneNumber);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendRequest(reservation.reservationID);
            }
        });
    }

    private void SendRequest(final String requestMsg) {
        final Handler myhandler = new Handler() {
            public void handleMessage(Message msg) {
                //isNetError = msg.getData().getBoolean("isNetError");
                if(msg.what==0x123)
                {
                    //更新界面
                    Log.d("Pay for reservation", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");

                    Gson gson = new Gson();
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        User user = gson.fromJson(message, User.class);
                        StoreList storeList = gson.fromJson(message, StoreList.class);
                        ReservationList reservationList = gson.fromJson(message, ReservationList.class);

                        if(user == null)
                        {
                            Log.d("Pay for reservation", "User non-exist");
                        }
                        else if(storeList == null)
                        {
                            Log.d("Pay for reservation", "StoreList non-exist");
                        }
                        else if(reservationList == null)
                        {
                            Log.d("Pay for reservation", "ReservationList non-exist");
                        }
                        else
                        {
                            Intent intent = new Intent(ReservationInfoActivity.this, main_page.class);
                            intent.putExtra("user", user);
                            intent.putExtra("stores", storeList);
                            intent.putExtra("reservations", reservationList);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(ReservationInfoActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        new Thread(){
            public void run(){
                try {
                    //请求服务器返回用户数据
                    Connection connection = new Connection();
                    String msg = connection.connectServer(requestMsg, "/getInfo.json");

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
