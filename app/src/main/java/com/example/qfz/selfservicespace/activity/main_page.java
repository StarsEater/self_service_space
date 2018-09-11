package com.example.qfz.selfservicespace.activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qfz.selfservicespace.R;
import com.example.qfz.selfservicespace.object.Connection;
import com.example.qfz.selfservicespace.object.ConnectionRes;
import com.example.qfz.selfservicespace.object.Reservation;
import com.example.qfz.selfservicespace.object.ReservationList;
import com.example.qfz.selfservicespace.object.StoreList;
import com.example.qfz.selfservicespace.object.User;
import com.google.gson.Gson;

public class main_page extends AppCompatActivity {

    public StoreList storeList = new StoreList();
    public ReservationList reservationList=new ReservationList();
    public User user = new User();

    private fragment_search f1 = new fragment_search();
    private fragment_order f2 = new fragment_order();
    private fragment_my f3 = new fragment_my();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d("search","hi,search");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_rel,f1).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_dashboard:
                    Log.d("form","hi,form");
                    SendRequest(user.getUserID());
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_rel,f2).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_notifications:
                    System.out.print("333");
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_rel,f3).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };
    //加载搜素fragment中的顶端Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        //处理从服务器收到的数据
        Intent intent = getIntent();
        storeList = (StoreList) intent.getSerializableExtra("stores");
        reservationList = (ReservationList) intent.getSerializableExtra("reservations");
        user = (User) intent.getSerializableExtra("user");

        //用搜索页面fragment初始化主页面的布局
        getSupportFragmentManager().beginTransaction().add(R.id.main_rel, f1).commitAllowingStateLoss();
        Log.d("main_page","hi,main_page");
        //导航栏监听
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //进入画面效果
        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setExitTransition(explode);
        getWindow().setEnterTransition(explode);

        //隐藏状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void SendRequest(final String requestMsg) {
        final Handler myhandler = new Handler() {
            public void handleMessage(Message msg) {
                //isNetError = msg.getData().getBoolean("isNetError");
                if(msg.what==0x123)
                {
                    //更新界面
                    Log.d("get reservationList", msg.getData().getString("msg"));
                    String message = msg.getData().getString("msg");

                    Gson gson = new Gson();
                    ConnectionRes connectionRes = gson.fromJson(message, ConnectionRes.class);

                    if(connectionRes.state == 0) {
                        ReservationList reservationList1 = gson.fromJson(message, ReservationList.class);
                        if(reservationList1 != null) {
                            reservationList = reservationList1;
                        }
                    } else {
                        Toast.makeText(main_page.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        new Thread(){
            public void run(){
                try {
                    //请求服务器返回用户数据
                    Connection connection = new Connection();
                    String msg = connection.connectServer(requestMsg, "/getReservationList.json");

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