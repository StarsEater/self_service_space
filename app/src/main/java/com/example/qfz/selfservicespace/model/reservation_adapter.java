package com.example.qfz.selfservicespace.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.activity.ReservationFinishedActivity;
import com.example.qfz.selfservicespace.activity.ReservationWaitingActivity;
import com.example.qfz.selfservicespace.activity.ReservationWaitingPayedActivity;
import com.example.qfz.selfservicespace.activity.SelectSeatActivity;
import com.example.qfz.selfservicespace.activity.reservation2device;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationInfo;
import com.example.qfz.selfservicespace.object.StoreInfo;
import com.example.qfz.selfservicespace.object.User;
import com.example.qfz.selfservicespace.object.deviceList;
import com.google.gson.Gson;

import java.util.List;

public class reservation_adapter extends RecyclerView.Adapter<reservation_adapter.ViewHolder> {

    private Context mContext;
    private List<Reservation> mReservationList;
    private User user;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView Reservation_state,Reservation_id,Reservation_shopname;
        private Button know_more;

        public ViewHolder(View view) {
            super(view);
            Reservation_state=view.findViewById(R.id.reseveration_state);
            Reservation_id=view.findViewById(R.id.reseveration_id);
            Reservation_shopname=view.findViewById(R.id.reseveration_shopname);
            know_more=view.findViewById(R.id.know_more);
        }
    }

    public reservation_adapter(List<Reservation> ReservationList, User user1) {
        mReservationList=ReservationList;
        user = user1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.reversation_item, viewGroup, false);
        final ViewHolder holder =new ViewHolder(view);
        holder.know_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                Reservation temp = mReservationList.get(position);
                if(temp.getReservation_state().equals("等待中")) {
                    SendRequest(user.getUserID() + "\t" + temp.getReservation_id(), 1);
                } else if(temp.getReservation_state().equals("使用中")) {
                    SendRequest(user.getUserID() + "\t" + temp.getReservation_id(), 2);
                } else if(temp.getReservation_state().equals("已完成")) {
                    SendRequest(user.getUserID() + "\t" + temp.getReservation_id(), 3);
                } else if(temp.getReservation_state().equals("待付款")) {
                    SendRequest(user.getUserID() + "\t" + temp.getReservation_id(), 4);
                } else {
                    Log.d("get one reservation", temp.getReservation_state());
                }
            }
        });
        return holder;
    }

    private void SendRequest(final String device_inf, final int state){
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
                    Log.d("Reservation state", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");
                    Gson gson = new Gson();
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        switch (state) {
                            case 1:
                                ReservationInfo reservation1 = gson.fromJson(message, ReservationInfo.class);
                                if(reservation1 != null) {
                                    Intent intent1 = new Intent(mContext, ReservationWaitingActivity.class);
                                    intent1.putExtra("Reservation", reservation1);
                                    intent1.putExtra("UserInfo", user);
                                    mContext.startActivity(intent1);
                                }
                                break;
                            case 2:
                                deviceList deviceList1 = gson.fromJson(message, deviceList.class);
                                if(deviceList1 != null) {
                                    Intent intent2 = new Intent(mContext, reservation2device.class);
                                    intent2.putExtra("deviceList", deviceList1);
                                    intent2.putExtra("UserInfo", user);
                                    mContext.startActivity(intent2);
                                }
                                break;
                            case 3:
                                ReservationInfo reservation3 = gson.fromJson(message, ReservationInfo.class);
                                if(reservation3 != null) {
                                    Intent intent3 = new Intent(mContext, ReservationFinishedActivity.class);
                                    intent3.putExtra("Reservation", reservation3);
                                    intent3.putExtra("UserInfo", user);
                                    mContext.startActivity(intent3);
                                }
                                break;
                            case 4:
                                ReservationInfo reservation4 = gson.fromJson(message, ReservationInfo.class);
                                if(reservation4 != null) {
                                    Intent intent4 = new Intent(mContext, ReservationWaitingPayedActivity.class);
                                    intent4.putExtra("Reservation", reservation4);
                                    intent4.putExtra("UserInfo", user);
                                    mContext.startActivity(intent4);
                                }
                                break;
                        }
                    } else {
                        Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
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

                    String msg = new String();

                    switch (state) {
                        case 1:
                            msg = connection.connectServer(device_inf,"/selectSeat.json");
                            break;
                        case 2:
                            msg = connection.connectServer(device_inf,"/getDevices.json");
                            break;
                        case 3:
                            msg = connection.connectServer(device_inf,"/selectSeat.json");
                            break;
                        case 4:
                            msg = connection.connectServer(device_inf,"/selectSeat.json");
                            break;
                    }


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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Reservation res = mReservationList.get(i);
        viewHolder.Reservation_shopname.setText(res.getReservation_name());
        viewHolder.Reservation_id.setText(res.getReservation_id());
        viewHolder.Reservation_state.setText(res.getReservation_state());
        //Glide.with(mContext).load(func.getImageId()).into(viewHolder.funcimage);
    }

    @Override
    public int getItemCount() {
        return mReservationList.size();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_adapter);
    }*/
}

