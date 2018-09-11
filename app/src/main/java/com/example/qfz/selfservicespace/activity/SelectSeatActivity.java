package com.example.qfz.selfservicespace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.model.SeatTable;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.MakeAReservation;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationInfo;
import com.example.qfz.selfservicespace.object.SearchStoreInfo;
import com.example.qfz.selfservicespace.object.Seat;
import com.example.qfz.selfservicespace.object.StoreInfo;
import com.example.qfz.selfservicespace.object.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class SelectSeatActivity extends AppCompatActivity {

    public SeatTable seatTableView;
    public Button confirmBtn;
    public MakeAReservation makeAReservation;
    public User user;
    public SearchStoreInfo searchStoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_seat);

        Intent intent = getIntent();
        final StoreInfo storeInfo = (StoreInfo) intent.getSerializableExtra("StoreInfo");
        user = (User) intent.getSerializableExtra("UserInfo");
        searchStoreInfo = (SearchStoreInfo) intent.getSerializableExtra("SearchStoreInfo");
        makeAReservation = new MakeAReservation(user.getUserID(), storeInfo.storeID, searchStoreInfo.beginTime, searchStoreInfo.endTime);
        //final StoreInfo storeInfo = new StoreInfo(new HashMap<String, String>(), 10, 10);

        seatTableView = (SeatTable) findViewById(R.id.seatView);
        seatTableView.setScreenName("门");//设置屏幕名称
        seatTableView.setMaxSelected(999);//设置最多选中

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                if(!storeInfo.allSeats.containsKey(row + "+" + column)) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {

                if(storeInfo.allSeats.get(row + "+" + column).equals("ordered")) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean isTable(int row, int column) {
                if(storeInfo.allSeats.get(row + "+" + column).equals("table")) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {
                makeAReservation.orderedSeats.add(new Seat(row, column, "ordered"));
            }

            @Override
            public void unCheck(int row, int column) {
                Seat target = new Seat(row, column, "ordered");
                Iterator<Seat> iter = makeAReservation.orderedSeats.iterator();
                while (iter.hasNext()) {
                    Seat item = iter.next();
                    if (item.equal(target)) {
                        iter.remove();
                    }
                }
            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(storeInfo.rowNum,storeInfo.columnNum);

        confirmBtn = (Button) findViewById(R.id.button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String requestMsg = gson.toJson(makeAReservation);;
                SendRequest(requestMsg);
            }
        });
    }

    private final void SendRequest(final String requestMsg) {
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
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        ReservationInfo reservation = gson.fromJson(message, ReservationInfo.class);

                        if(reservation != null) {
                            Intent intent = new Intent(SelectSeatActivity.this, ReservationInfoActivity.class);
                            intent.putExtra("Reservation", reservation);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(SelectSeatActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    String msg = connection.connectServer(requestMsg,"/selectSeat.json");

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
