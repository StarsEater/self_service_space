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

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.activity.SelectSeatActivity;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.StoreInfo;
import com.example.qfz.selfservicespace.object.User;
import com.example.qfz.selfservicespace.object.device;
import com.example.qfz.selfservicespace.object.deviceList;
import com.google.gson.Gson;

import java.util.List;

public class device_adapter extends RecyclerView.Adapter<device_adapter.ViewHolder> {

    private Context mContext;
    private deviceList mdeviceList;
    private User user;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private  TextView device_state,device_id,device_name;
        private Button use_it;
        public ViewHolder(View view) {
            super(view);
            device_state=view.findViewById(R.id.device_state);
            device_id=view.findViewById(R.id.device_id);
            device_name=view.findViewById(R.id.device_name);
            use_it=view.findViewById(R.id.use_it);
        }
    }

    public device_adapter(deviceList deviceList, User user1) {
        mdeviceList=deviceList;
        user = user1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.device_item, viewGroup, false);
        final ViewHolder holder =new ViewHolder(view);
        holder.use_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                device device = mdeviceList.devices.get(position);
                SendRequest(user.getUserID() + "\t" + mdeviceList.reservationID + "\t" + device.getDevice_id());
            }
        });
        return holder;
    }

    private void SendRequest(final String con_inf){
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
                    Log.d("control device", msg.getData().getString("msg"));
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
                    String msg = connection.connectServer(con_inf,"/controlDevice.json");

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
        device de = mdeviceList.devices.get(i);
        viewHolder.device_name.setText(de.getDevice_name());
        viewHolder.device_id.setText(de.getDevice_id());
        viewHolder.device_state.setText(de.getDevice_state());
        //Glide.with(mContext).load(func.getImageId()).into(viewHolder.funcimage);
    }

    @Override
    public int getItemCount() {
        return mdeviceList.devices.size();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_adapter);
    }*/
}

