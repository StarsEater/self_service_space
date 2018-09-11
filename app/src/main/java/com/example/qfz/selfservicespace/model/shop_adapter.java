package com.example.qfz.selfservicespace.model;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.activity.MainActivity;
import com.example.qfz.selfservicespace.activity.SelectSeatActivity;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.SearchStoreInfo;
import com.example.qfz.selfservicespace.object.StoreInfo;
import com.example.qfz.selfservicespace.object.User;
import com.example.qfz.selfservicespace.object.shop;
import com.google.gson.Gson;

import java.util.List;

public class shop_adapter extends RecyclerView.Adapter<shop_adapter.ViewHolder> {

    private Context mContext;
    private List<shop> mShopList;
    private User user;
    private SearchStoreInfo searchStoreInfo;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView shopName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            shopName = (TextView) view.findViewById(R.id.func_name);
        }
    }

    public shop_adapter(List<shop> ShopList, User user1, SearchStoreInfo searchStoreInfo1) {
        mShopList = ShopList;
        user = user1;
        searchStoreInfo = searchStoreInfo1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null) {
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.shop_item, viewGroup, false);
        final ViewHolder holder =new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int position=holder.getAdapterPosition();
                shop shop= mShopList.get(position);
                SendRequest(shop.getShopID());
//                mContext.startActivity(new Intent(mContext,ReservationInfoActivity.class));
            }
        });
        return holder;
    }

    private void SendRequest(final String shop_inf){
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
                    Log.d("getSeats", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");

                    Gson gson = new Gson();
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        StoreInfo storeInfo = gson.fromJson(message, StoreInfo.class);

                        if(storeInfo != null) {
                            Intent intent = new Intent(mContext, SelectSeatActivity.class);
                            intent.putExtra("StoreInfo", storeInfo);
                            intent.putExtra("UserInfo", user);
                            intent.putExtra("SearchStoreInfo", searchStoreInfo);
                            mContext.startActivity(intent);
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
                    String msg = connection.connectServer(shop_inf,"/getSeatTable.json");

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
        shop func = mShopList.get(i);
        viewHolder.shopName.setText(func.getName());
        //Glide.with(mContext).load(func.getImageId()).into(viewHolder.funcimage);
    }

    @Override
    public int getItemCount() {
        return mShopList.size();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_adapter);
    }*/
}

